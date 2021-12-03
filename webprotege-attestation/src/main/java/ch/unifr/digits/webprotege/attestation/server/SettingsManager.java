package ch.unifr.digits.webprotege.attestation.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingsManager {

    public static final String PROVIDER_HOST;
    public static final String PROVIDER_PORT;
    public static final String ADDRESS_ATTESTATION;
    public static final String ADDRESS_CHANGETRACKING;
    public static final String SERVER_SECRET;

    static {
        Properties properties = new Properties();
        try (InputStream stream = SettingsManager.class.getClassLoader()
                .getResourceAsStream("configuration/config.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            PROVIDER_HOST = properties.getProperty("PROVIDER_HOST");
            PROVIDER_PORT = properties.getProperty("PROVIDER_PORT");
            ADDRESS_ATTESTATION = properties.getProperty("ADDRESS_ATTESTATION");
            ADDRESS_CHANGETRACKING = properties.getProperty("ADDRESS_CHANGETRACKING");
            SERVER_SECRET = properties.getProperty("SERVER_SECRET");
        }
    }

}
