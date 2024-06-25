package org.zeasn;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.ListAlbumsRequest;
import org.apache.http.util.TextUtils;
import org.zeasn.factories.PhotoLibraryClientFactory;

public class Album {
    public static void main(String[] args) {
        try {
            PhotosLibraryClient client = PhotoLibraryClientFactory.getClient();
            getAlbums(client,10,"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取影集列表
     * @param client Photo请求客户端
     * @param pageSize 每页数量，每页默认25条数据，最多支持100条
     * @param pageToken 下一页的Token
     */
    public static void getAlbums(PhotosLibraryClient client, int pageSize, String pageToken) throws Exception {
        ListAlbumsRequest listAlbumsRequest =
                ListAlbumsRequest.newBuilder()
                        .setPageSize(pageSize) //每页默认25条数据，最多支持100条
                        .setPageToken(pageToken)
                        .build();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse response = client.listAlbums(listAlbumsRequest);
        response.iterateAll().forEach(item -> System.out.println(item.toString()));
        if (!TextUtils.isEmpty(response.getNextPageToken())) {
            // 如果存在下一页的Token，则继续请求下一页
            getAlbums(client,pageSize,response.getNextPageToken());
        }
    }

    /**
     * 创建影集
     * @param client Photo请求客户端
     */
    public static void createAlbum(PhotosLibraryClient client) {
        com.google.photos.types.proto.Album response = client.createAlbum("frame_backup");
        System.out.println(response.toString());
    }
}
