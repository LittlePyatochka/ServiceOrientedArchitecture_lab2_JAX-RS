package kamysh.util;


import lombok.SneakyThrows;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.FileInputStream;
import java.security.KeyStore;

public class ClientFactoryBuilder {
    private static Client client;

    @SneakyThrows
    public static Client getClient() {
        if (client != null) return client;
        String keystoreLocation = System.getProperty("KEYSTORE_PATH");
        String keystorePassword = System.getProperty("KEYSTORE_PASS");
        System.out.println("KEYSTORE_PATH = " + keystoreLocation);
        System.out.println("KEYSTORE_PASS = " + keystorePassword);
        FileInputStream is = new FileInputStream(keystoreLocation);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, keystorePassword.toCharArray());
        client = ClientBuilder.newBuilder().trustStore(keystore).build();
        return client;
    }

    public static String getStorageServiceUrl() {
        return System.getProperty("STORAGE_SERVICE_URL");
    }
}
