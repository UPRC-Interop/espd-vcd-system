package eu.esens.espdvcd.designer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    private static final String propFileName = "config.properties";
    private static Properties prop;
    private static Config instance = null;

    private Config() throws IOException {
        prop = new Properties();

        if (Files.exists(Paths.get("./" + propFileName))) {
            try(InputStream is = new FileInputStream("./" + propFileName)){
                if (is != null)
                    prop.load(is);
                else
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } else{
            try(InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName)){
                if (is != null)
                    prop.load(is);
                else
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }
    }

    public static String getToopConnectorDPUrl() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("toop.connector.dp.url");
    }

    public static String getKeystorePath() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("toop.keystore.path");
    }

    public static String getKeystorePassword() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("toop.keystore.password");
    }

    public static String getKeystoreKeyAlias() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("toop.keystore.key.alias");
    }

    public static String getKeystoreKeyPassword() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("toop.keystore.key.password");
    }
}
