package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {

    private static final String propFileName = "config.properties";
    private static Properties prop;
    private static Config instance = null;

    private Config() throws IOException {
        prop = new Properties();

        if (Files.exists(Paths.get("./" + propFileName))) {
            try (InputStream is = new FileInputStream("./" + propFileName)) {
                if (is != null)
                    prop.load(is);
                else
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } else {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName)) {
                if (is != null)
                    prop.load(is);
                else
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }
    }

    public static boolean isDebugEnabled() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.debug"));
    }

    public static int getServerPort() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return Integer.parseInt(prop.getProperty("espd.designer.port"));
        } catch (NumberFormatException e) {
            Logger.getLogger(Config.class.getName())
                    .severe("Could not load port setting from config.properties, defaulting to 8080.");
            return 8080;
        }
    }

//    public static String getPath() {
//        if (instance == null) {
//            try {
//                instance = new Config();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return prop.getProperty("espd.designer.path");
//    }

    public static EULanguageCodeEnum getDefaultLang() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return EULanguageCodeEnum.valueOf(prop.getProperty("espd.designer.lang").toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.getLogger(Config.class.getName())
                    .severe("Could not load language setting from config.properties, defaulting to EN.");
            return EULanguageCodeEnum.EN;
        }
    }

    public static boolean isValidatorEnabled() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.validator.enabled"));
    }

    public static boolean isArtefactDumpingEnabled() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.artefactDump.enabled"));
    }

    public static String dumpIncomingArtefactsLocation() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty("espd.designer.artefactDump.location");
    }

    public static boolean isCORSEnabled() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.CORS.enabled"));
    }

    public static boolean isEnchancedSecurityEnabled() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.enchancedSecurity.enabled"));
    }

    public static boolean isFramingAllowed() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.allowFraming"));
    }
}
