package org.zeasn.factories;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;

public class PhotoLibraryClientFactory {
    private static final String serverAuthCode = "4/0ATx3LY6oceO0q-CvyYSeHNuI1xeoMACGXcAC8baQHR5XzSWLX2aE0FZJpM7rPrgJ8bj2tA";

    public static PhotosLibraryClient getClient() throws Exception {
        Credentials credentials = OAuthFactory.getCredentials(serverAuthCode);
        PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return PhotosLibraryClient.initialize(settings);
    }
}
