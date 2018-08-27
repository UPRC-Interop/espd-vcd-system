package eu.esens.espdvcd.builder.util;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.schema.EDMVersion;

import java.io.*;
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
     * Identify Exchange Data Model (EDM) version of given ESPD artefact String representation
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The EDM version
     */
    public static EDMVersion findEDMVersion(final String partOfTheArtefact) {
        String smallerPartOfTheArtefact = partOfTheArtefact.substring(0, 99); // 100 chars

        final String v1ArtefactRegex = "<\\S*(ESPDRequest|ESPDResponse)";
        final String v2ArtefactRegex = "<\\S*(QualificationApplicationRequest|QualificationApplicationResponse)";

        boolean isV1Artefact = Pattern.compile(v1ArtefactRegex).matcher(smallerPartOfTheArtefact).find();

        if (isV1Artefact) {
            return EDMVersion.V1;
        }

        boolean isV2Artefact = Pattern.compile(v2ArtefactRegex).matcher(smallerPartOfTheArtefact).find();

        if (isV2Artefact) {
            return EDMVersion.V2;
        }

        return null;
    }

    /**
     * Identify Exchange Data Model (EDM) version of given ESPD XML artefact
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The EDM version
     */
    public static EDMVersion findEDMVersion(InputStream xmlESPD) {

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 128); //  better stay below 256
            return findEDMVersion(partOfTheArtefact);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * Identify Exchange Data Model (EDM) version of given ESPD XML artefact
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The EDM version
     */
    public static EDMVersion findEDMVersion(File xmlESPD) {

        try {
            return findEDMVersion(new FileInputStream(xmlESPD));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
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
    public static ProfileExecutionIDEnum findProfileExecutionID(InputStream xmlESPD) {
        ProfileExecutionIDEnum profileExecutionID = ProfileExecutionIDEnum.UNKNOWN;

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 2048);

            switch (findEDMVersion(partOfTheArtefact)) {

                case V1:
                    profileExecutionID = ProfileExecutionIDEnum.ESPD_EDM_V1_0_2;
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
                        profileExecutionID = Arrays.stream(ProfileExecutionIDEnum.values())
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

        return profileExecutionID;
    }

    /**
     * Identify type of given ESPD artefact String representation (request or response)
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The artefact type
     */
    public static ArtefactType findArtefactType(final String partOfTheArtefact) {
        String smallerPartOfTheArtefact = partOfTheArtefact.substring(0, 99); // 100 chars

        final String requestRegex = "<\\S*(ESPDRequest|QualificationApplicationRequest)";
        final String responseRegex = "<\\S*(ESPDResponse|QualificationApplicationResponse)";

        boolean isRequest = Pattern.compile(requestRegex).matcher(smallerPartOfTheArtefact).find();

        if (isRequest) {
            return ArtefactType.ESPD_REQUEST;
        }

        boolean isResponse = Pattern.compile(responseRegex).matcher(smallerPartOfTheArtefact).find();

        if (isResponse) {
            return ArtefactType.ESPD_RESPONSE;
        }

        return ArtefactType.UNKNOWN;
    }

    /**
     * Identify type of given ESPD XML artefact (request or response)
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The artefact type
     */
    public static ArtefactType findArtefactType(InputStream xmlESPD) {

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 128); //  better stay below 256
            return findArtefactType(partOfTheArtefact);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return ArtefactType.UNKNOWN;
    }

    /**
     * Identify type of given ESPD XML artefact (request or response)
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The artefact type
     */
    public static ArtefactType findArtefactType(File xmlESPD) {

        try {
            return findArtefactType(new FileInputStream(xmlESPD));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return ArtefactType.UNKNOWN;
    }

}
