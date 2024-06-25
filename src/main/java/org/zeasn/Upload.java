package org.zeasn;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsRequest;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import org.zeasn.factories.PhotoLibraryClientFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Upload {

    public static void main(String[] args) {
        try {
            PhotosLibraryClient client = PhotoLibraryClientFactory.getClient();
            File folder = new File("./test");
            upload(client,folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件
     * @param client Photo请求客户端
     * @param parentFile 文件夹，文件夹内的文件就是上传的内容
     */
    public static void upload(PhotosLibraryClient client,File parentFile) throws FileNotFoundException {
        // 用于上传的内容，最多不能超过50个
        List<List<NewMediaItem>> newMediaItemList = new ArrayList<>();
        newMediaItemList.add(new ArrayList<>());

        for (File file : Objects.requireNonNull(parentFile.listFiles())) {
            UploadMediaItemRequest uploadMediaItemRequest = UploadMediaItemRequest.newBuilder()
                    .setDataFile(new RandomAccessFile(file,"r"))
                    .setMimeType(URLConnection.guessContentTypeFromName(file.getName()))
                    .build();
            // 获取上传需要用的uploadToken
            UploadMediaItemResponse response = client.uploadMediaItem(uploadMediaItemRequest);
            if (response.getUploadToken().isPresent()) {
                // 将uploadToken添加到newMediaItemList，供下面在Google Photo创建媒体使用
                System.out.println(file.getAbsolutePath() + "_upload_token=:" + response.getUploadToken().get());
                if (newMediaItemList.get(newMediaItemList.size()-1).size()>=50) {
                    newMediaItemList.add(new ArrayList<>());
                }
                newMediaItemList.get(newMediaItemList.size()-1).add(
                        NewMediaItemFactory.createNewMediaItem(response.getUploadToken().get(),file.getName())
                );
                continue;
            }
            if (response.getError().isPresent()) {
                if (response.getError().get().getResumeUrl().isPresent()) {
                    System.out.println(file.getAbsolutePath() + "_resume_url=:" + response.getError().get().getResumeUrl().get());
                }

                if (response.getError().get().getCause() != null) {
                    System.out.println(file.getAbsolutePath() + "_throwable=:" + response.getError().get().getCause().getMessage());
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (List<NewMediaItem> newMediaItems : newMediaItemList) {
            executor.submit(()->{
                // 在Google Photo创建媒体数据
                BatchCreateMediaItemsRequest request = BatchCreateMediaItemsRequest.newBuilder().addAllNewMediaItems(newMediaItems).build();
                BatchCreateMediaItemsResponse createResponse = client.batchCreateMediaItems(request);
                System.out.println(createResponse.toString());
            });
        }
        executor.shutdown();
    }
}
