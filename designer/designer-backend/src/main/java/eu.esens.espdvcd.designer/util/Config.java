/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer.util;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;

import java.io.*;
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
//                 ;OutputStream out = new FileOutputStream("./"+propFileName)) {
                if (is != null) {
                    prop.load(is);
//                    prop.store(out, "Automatically generated ESPD Designer configuration file");
                }
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
            return Integer.parseInt(prop.getProperty("espd.designer.port")
                    .trim());
        } catch (NumberFormatException e) {
            Logger.getLogger(Config.class.getName())
                    .warning("Could not load port setting from config.properties, defaulting to 8080.");
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
            return EULanguageCodeEnum.valueOf(prop.getProperty("espd.designer.lang")
                    .toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            Logger.getLogger(Config.class.getName())
                    .warning("Could not load language setting from config.properties, defaulting to EN.");
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

    public static boolean isSelectionCriteriaPreselected() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(prop.getProperty("espd.designer.criteria.selection.selected"));
    }

    public static int getMaxFileSize() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return Integer.parseInt(prop.getProperty("espd.designer.maxFileSizeUpload")
                    .trim());
        } catch (NumberFormatException e) {
            Logger.getLogger(Config.class.getName())
                    .warning("Defaulting to 4MB file upload limit");
            return 4;
        }
    }
}
