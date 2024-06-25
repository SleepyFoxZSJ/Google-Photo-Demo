package org.zeasn.constants;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    String CLIENT_ID = "102626496449-qsfsh1ooa48h2t59c448jtndqd3q3c8n.apps.googleusercontent.com";

    String CREDENTIAL_PATH = "./client_secret.json";
    List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/photoslibrary.readonly",
            "https://www.googleapis.com/auth/photoslibrary.appendonly"
    );
}
