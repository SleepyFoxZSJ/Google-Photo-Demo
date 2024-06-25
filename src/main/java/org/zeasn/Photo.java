package org.zeasn;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.FeatureFilter;
import com.google.photos.library.v1.proto.Filters;
import com.google.photos.library.v1.proto.ListMediaItemsRequest;
import com.google.photos.library.v1.proto.SearchMediaItemsRequest;
import org.apache.http.util.TextUtils;
import org.zeasn.factories.PhotoLibraryClientFactory;

public class Photo {

    public static void main(String[] args) {
        try {
            PhotosLibraryClient client = PhotoLibraryClientFactory.getClient();
            getMedias(client,10,"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取全部媒体
     * @param client Photo请求客户端
     * @param pageSize 每页数量，每页默认25条数据，最多支持100条
     * @param pageToken 下一页的Token
     */
    public static void getMedias(PhotosLibraryClient client, int pageSize, String pageToken) throws Exception {
        ListMediaItemsRequest listMediaItemsRequest =
                ListMediaItemsRequest.newBuilder()
                        .setPageSize(pageSize) //每页默认25条数据，最多支持100条
                        .setPageToken(pageToken)
                        .build();
        InternalPhotosLibraryClient.ListMediaItemsPagedResponse response = client.listMediaItems(listMediaItemsRequest);
        response.iterateAll().forEach(item -> System.out.println(item.toString()));
        if (!TextUtils.isEmpty(response.getNextPageToken())) {
            // 如果存在下一页的Token，则继续请求下一页
            getMedias(client,pageSize,response.getNextPageToken());
        }
    }

    /**
     * 获取对应影集的媒体
     * @param client Photo请求客户端
     * @param albumId 影集Id
     * @param pageSize 每页数量，每页默认25条数据，最多支持100条
     * @param pageToken 下一页的Token
     */
    public static void getMedias(PhotosLibraryClient client,int pageSize, String pageToken,String albumId,Filters filters) throws Exception {
        SearchMediaItemsRequest.Builder searchBuilder = SearchMediaItemsRequest.newBuilder()
                        .setPageSize(pageSize)
                        .setPageToken(pageToken);
        if (albumId!=null) {
            searchBuilder.setAlbumId(albumId);
        }
        if (filters!=null) {
            searchBuilder.setFilters(filters);
        }

        SearchMediaItemsRequest searchMediaItemsRequest = searchBuilder.build();
        InternalPhotosLibraryClient.SearchMediaItemsPagedResponse response = client.searchMediaItems(searchMediaItemsRequest);
        response.iterateAll().forEach(item -> System.out.println(item.toString()));
        if (!TextUtils.isEmpty(response.getNextPageToken())) {
            // 如果存在下一页的Token，则继续请求下一页
            getMedias(client, pageSize, response.getNextPageToken(),albumId,filters);
        }
    }

    /**
     * 获取收藏夹搜索类型
     * @return Filters
     */
    public static Filters getFavoritesFilter(){
        FeatureFilter featureFilter = FeatureFilter.newBuilder()
                .addIncludedFeatures(FeatureFilter.Feature.FAVORITES)
                .build();
        return Filters.newBuilder()
                .setFeatureFilter(featureFilter )
                .build();
    }
}
