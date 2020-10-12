/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder.util;

import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.codelist.enums.internal.ArtefactType;
import eu.esens.espdvcd.model.DocumentDetails;
import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import eu.esens.espdvcd.schema.enums.EDMVersion;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtefactUtils {

    private static final Logger LOGGER = Logger.getLogger(ArtefactUtils.class.getName());

    public static String clearCRLF(String stringToClear) {
        return stringToClear != null
                ? stringToClear
                .replace("\n", "")
                .replace("\r", "")
                : stringToClear;
    }

    public static String clearAllWhitespaces(String stringToClear) {
        return stringToClear != null
                ? stringToClear
                .replaceAll("\\s+", "")
                : stringToClear;
    }

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
     * Identify Exchange Data Model (EDM) version of given ESPD artefact String representation.
     * <p>
     * Warning!!! Do not make this method public or package private.
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The EDM version
     */
    private static EDMVersion findEDMVersion(final String partOfTheArtefact) {
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

        throw new IllegalStateException("Error... Imported XML Artefact EDM Version cannot be classified either as V1 nor as V2.");
    }

    /**
     * Identify Exchange Data Model (EDM) sub-version of given ESPD artefact String representation.
     * <p>
     * Warning!!! Do not make this method public or package private.
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The EDM version
     */
    private static EDMSubVersion findEDMSubVersion(final String partOfTheArtefact) {

        switch (findEDMVersion(partOfTheArtefact)) {

            case V1:
                return EDMSubVersion.V102;

            case V2:

                switch (findProfileExecutionID(partOfTheArtefact)) {

                    case ESPD_EDM_V200_REGULATED:
                    case ESPD_EDM_V200_SELFCONTAINED:

                        return EDMSubVersion.V200;

                    case ESPD_EDM_V201_REGULATED:
                    case ESPD_EDM_V201_SELFCONTAINED:

                        return EDMSubVersion.V201;

                    case ESPD_EDM_V202_REGULATED:
                    case ESPD_EDM_V202_SELFCONTAINED:

                        return EDMSubVersion.V202;

                    case ESPD_EDM_V210_REGULATED:
                    case ESPD_EDM_V210_SELFCONTAINED:

                        return EDMSubVersion.V210;

                    default:

                        throw new IllegalStateException("Error... Imported XML Artefact EDM Version classified as V2 but"
                                + " failed to identify the specific sub-version (e.g. 2.0.0, 2.1.0 etc).");
                }

            default:
                throw new IllegalStateException("Error... Imported XML Artefact EDM Sub-Version "
                        + "cannot be classified.");
        }

    }

    public static EDMSubVersion findEDMSubVersion(InputStream xmlESPD) {

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 4056); //  better stay below 256
            return findEDMSubVersion(partOfTheArtefact);
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

    /**
     * Identify Exchange Data Model (EDM) sub-version of given ESPD XML artefact
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The EDM sub-version
     */
    public static EDMSubVersion findEDMSubVersion(File xmlESPD) {

        try {
            return findEDMSubVersion(new FileInputStream(xmlESPD));
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

        return clearCRLF(partOfTheArtefact.toString());
    }

    /**
     * Identify profile execution id of given ESPD XML artefact.
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The profile execution id
     */
    public static ProfileExecutionIDEnum findProfileExecutionID(InputStream xmlESPD) {

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 2048);
            return findProfileExecutionID(partOfTheArtefact);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

        throw new IllegalStateException("Error... Imported XML Artefact Profile Execution ID cannot be classified.");
    }

    /**
     * Identify profile execution id of given ESPD XML artefact.
     * <p>
     * Warning!!! Do not make this method public or package private.
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The profile execution id
     */
    private static ProfileExecutionIDEnum findProfileExecutionID(String partOfTheArtefact) {

        switch (findEDMVersion(partOfTheArtefact)) {

            case V1:
                return ProfileExecutionIDEnum.ESPD_EDM_V102;

            case V2:
                /**
                 * in v2.x.x artefacts <ProfileExecutionID> is mandatory element
                 * and get values from: {@link eu.esens.espdvcd.codelist.CodelistsV2#ProfileExecutionID}
                 */
                String profileExecutionIDExtractionRegex = ".*<\\S*ProfileExecutionID.*?>(.*?)</\\S*ProfileExecutionID>.*";
                Matcher m = Pattern.compile(profileExecutionIDExtractionRegex,
                        Pattern.DOTALL & Pattern.MULTILINE)
                        .matcher(partOfTheArtefact);
                if (m.find()) {
                    // extract <cbc:ProfileExecutionID> value
                    final String theId = m.group(1);
                    return Arrays.stream(ProfileExecutionIDEnum.values())
                            .filter(id -> id.getValue().equals(theId))
                            .findAny().orElseThrow(() -> new IllegalStateException("Error... Imported XML Artefact "
                                    + "Profile Execution ID cannot be classified."));
                } else {
                    throw new IllegalStateException("Error... Matcher couldn't find Profile Execution ID value, by "
                            + "using regular expression.");
                }

            default:
                throw new IllegalStateException("Error... Imported XML Artefact EDM Version cannot be classified either as V1 nor as V2.");
        }
    }

    /**
     * Identify type of given ESPD artefact String representation (request or response)
     * <p>
     * Warning!!! Do not make this method public or package private.
     *
     * @param partOfTheArtefact A String representation of the ESPD XML artefact or part of it
     * @return The artefact type
     */
    private static ArtefactType findArtefactType(final String partOfTheArtefact) {
        String smallerPartOfTheArtefact = clearCRLF(partOfTheArtefact.substring(0, 99)); // 100 chars

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

        throw new IllegalStateException("Error... Imported XML Artefact Type cannot be classified either as ESPD Request nor as ESPD Response.");
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

        throw new IllegalStateException("Error... Imported XML Artefact Type cannot be classified either as ESPD Request nor as ESPD Response.");
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

        throw new IllegalStateException("Error... Imported XML Artefact Type cannot be classified either as ESPD Request nor as ESPD Response.");
    }

    /**
     * Identify QualificationApplicationTypeCode of given ESPD XML
     * artefact (regulated or self-contained).
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The QualificationApplicationTypeCode
     */
    public static QualificationApplicationTypeEnum findQualificationApplicationType(File xmlESPD) {

        try {
            return findQualificationApplicationType(new FileInputStream(xmlESPD));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        throw new IllegalStateException("Error... Imported XML Artefact Qualification Application Type cannot be classified either as Regulated nor as Self-Contained.");
    }

    /**
     * Identify QualificationApplicationTypeCode of given ESPD XML
     * artefact (regulated or self-contained).
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The QualificationApplicationTypeCode
     */
    public static QualificationApplicationTypeEnum findQualificationApplicationType(InputStream xmlESPD) {

        try {
            String partOfTheArtefact = getPartOfTheArtefact(xmlESPD, 4056);

            switch (findEDMVersion(partOfTheArtefact)) {

                case V1:
                    return QualificationApplicationTypeEnum.REGULATED;

                case V2:
                    String extractionRegex = ".*<\\S*QualificationApplicationTypeCode.*?>(.*?)</\\S*QualificationApplicationTypeCode>.*";
                    Matcher m = Pattern.compile(extractionRegex, Pattern.DOTALL & Pattern.MULTILINE)
                            .matcher(partOfTheArtefact);
                    if (m.find()) {
                        final String theType = m.group(1);
                        return Arrays.stream(QualificationApplicationTypeEnum.values())
                                .filter(type -> type.name().equals(theType))
                                .findAny().orElseThrow(() -> new IllegalStateException("Error... Imported XML Artefact Qualification Application Type cannot be classified either as Regulated nor as Self-Contained."));
                    } else {
                        throw new IOException("Error... Matcher couldn't find Qualification Application Type value by using regular expression.");
                    }

                default:
                    throw new IllegalStateException("Error... Imported XML Artefact EDM Version cannot be classified either as V1 nor as V2.");
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

        throw new IllegalStateException("Error... Imported XML Artefact Qualification Application Type cannot be classified either as Regulated nor as Self-Contained.");
    }

    public static DocumentDetails findDocumentDetails(InputStream xmlESPD) {
        EDMVersion version = findEDMVersion(xmlESPD);
        QualificationApplicationTypeEnum qualificationApplicationType = findQualificationApplicationType(xmlESPD);
        ArtefactType artefactType = findArtefactType(xmlESPD);
        return new DocumentDetails(version, artefactType, qualificationApplicationType);
    }

}
