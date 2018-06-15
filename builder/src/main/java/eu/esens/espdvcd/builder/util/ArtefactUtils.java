package eu.esens.espdvcd.builder.util;

import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtefactUtils {

    private static final Logger LOGGER = Logger.getLogger(ArtefactUtils.class.getName());

    public static InputStream getBufferedInputStream(InputStream xmlESPD) {
        // We require a marked input stream
        InputStream bis;
        if (xmlESPD.markSupported()) {
            bis = xmlESPD;
        } else {
            bis = new BufferedInputStream(xmlESPD);
        }
        return bis;
    }

    /**
     * Identify schema version of given ESPD XML artefact
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The schema version
     */
    public static SchemaVersion findSchemaVersion(InputStream xmlESPD) {
        SchemaVersion version = SchemaVersion.UNKNOWN;

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 128); //  better stay below 256

            final String v1ArtefactRegex = "<\\S*(ESPDRequest|ESPDResponse)";
            final String v2ArtefactRegex = "<\\S*(QualificationApplicationRequest|QualificationApplicationResponse)";

            boolean isV1Artefact = Pattern.compile(v1ArtefactRegex).matcher(partOfTheArtefact).find();

            if (isV1Artefact) {
                version = SchemaVersion.V1;
            } else {

                boolean isV2Artefact = Pattern.compile(v2ArtefactRegex).matcher(partOfTheArtefact).find();

                if (isV2Artefact) {
                    version = SchemaVersion.V2;
                }
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return version;
    }

    public static String getPartOfTheArtefact(InputStream xmlESPD, int bytesToRead) throws IOException {
        InputStream bis = getBufferedInputStream(xmlESPD);
        StringBuilder partOfTheArtefact = new StringBuilder();

        byte[] contents = new byte[bytesToRead];
        int bytesRead;
        bis.mark(bytesToRead);
        while ((bytesRead = bis.read(contents)) != -1) {
            partOfTheArtefact.append(new String(contents, 0, bytesRead));
            if (bytesRead >= bytesToRead) {
                break;
            }
        }
        bis.reset();

        return partOfTheArtefact.toString();
    }

    /**
     * Identify profile execution id of given ESPD XML artefact.
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The profile execution id
     */
    public static ProfileExecutionIDEnum findEDMVersion(InputStream xmlESPD) {
        ProfileExecutionIDEnum profileExecutionIDEnum = ProfileExecutionIDEnum.UNKNOWN;

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 2048);

            switch (findSchemaVersion(xmlESPD)) {
                case V1:
                    profileExecutionIDEnum = ProfileExecutionIDEnum.ESPD_EDM_V1_0_2;
                    break;
                case V2:
                    /**
                     * in v2.0.x artefacts <cbc:ProfileExecutionID> is mandatory element
                     * and get values from: {@link eu.esens.espdvcd.codelist.CodelistsV2#ProfileExecutionID}
                     */
                    String profileExecutionIDExtractionRegex = ".*<cbc:ProfileExecutionID.*?>(.*?)</cbc:ProfileExecutionID>.*";
                    Matcher m = Pattern.compile(profileExecutionIDExtractionRegex,
                            Pattern.DOTALL & Pattern.MULTILINE)
                            .matcher(partOfTheArtefact);
                    if (m.find()) {
                        // extract <cbc:ProfileExecutionID> value
                        final String theId = m.group(1);
                        profileExecutionIDEnum = Arrays.stream(ProfileExecutionIDEnum.values())
                                .filter(id -> id.getValue().equals(theId))
                                .findAny().orElseThrow(() -> new IOException("Error... ProfileExecutionID element value doesn't match with any ProfileExecutionID Codelist value."));
                    } else {
                        throw new IOException("Error... Matcher couldn't find profile execution id value, by using regular expression.");
                    }
                    break;
                default:
                    throw new IOException("Error... Imported artefact could not be identified as either v1 or v2.");
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return profileExecutionIDEnum;
    }

}
