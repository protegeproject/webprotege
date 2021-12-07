package ch.unifr.digits.webprotege.attestation.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class SettingsManager {

    public static final String PROVIDER_HOST;
    public static final String PROVIDER_PORT;
    public static final String ADDRESS_ATTESTATION;
    public static final String ADDRESS_CHANGETRACKING;
    public static final String SERVER_SECRET;

    private static final String PROVIDER_HOST_VARNAME = "PROVIDER_HOST";
    private static final String PROVIDER_PORT_VARNAME = "PROVIDER_PORT";
    private static final String ADDRESS_ATTESTATION_VARNAME = "ADDRESS_ATTESTATION";
    private static final String ADDRESS_CHANGETRACKING_VARNAME = "ADDRESS_CHANGETRACKING";
    private static final String SERVER_SECRET_VARNAME = "SERVER_SECRET";

    static {
        final Map<String, String> env = System.getenv();
        final Properties properties = new Properties();
        try (InputStream stream = SettingsManager.class.getClassLoader()
                .getResourceAsStream("configuration/config.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            PROVIDER_HOST = env.getOrDefault(PROVIDER_HOST_VARNAME, properties.getProperty(PROVIDER_HOST_VARNAME));
            PROVIDER_PORT = env.getOrDefault(PROVIDER_PORT_VARNAME, properties.getProperty(PROVIDER_PORT_VARNAME));
            ADDRESS_ATTESTATION = env.getOrDefault(ADDRESS_ATTESTATION_VARNAME, properties.getProperty(ADDRESS_ATTESTATION_VARNAME));
            ADDRESS_CHANGETRACKING = env.getOrDefault(ADDRESS_CHANGETRACKING_VARNAME, properties.getProperty(ADDRESS_CHANGETRACKING_VARNAME));
            SERVER_SECRET = env.getOrDefault(SERVER_SECRET_VARNAME, properties.getProperty(SERVER_SECRET_VARNAME));
        }
    }

}
