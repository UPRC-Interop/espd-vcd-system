package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.schema.SchemaVersion;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import eu.esens.espdvcd.schema.SchemaVersion;

public interface ModelBuilder {

    default EODetails createDefaultEODetails() {
        // Empty EODetails (with initialized lists)
        System.out.println("Creating default EO Details");
        EODetails eod = new EODetails();
        eod.setContactingDetails(new ContactingDetails());
        eod.setPostalAddress(new PostalAddress());
        eod.setNaturalPersons(new ArrayList<>());

        NaturalPerson np = new NaturalPerson();
        np.setPostalAddress(new PostalAddress());
        np.setContactDetails(new ContactingDetails());

        eod.getNaturalPersons().add(np);
        return eod;
    }

    default CADetails createDefaultCADetails() {
        // Default initialization of the ESPDRequest and ESPDResponse Models.
        // Empty CADetails
        System.out.println("Creating default CA Details");
        CADetails cad = new CADetails();
        cad.setContactingDetails(new ContactingDetails());
        cad.setPostalAddress(new PostalAddress());
        return cad;
    }

    default ServiceProviderDetails createDefaultServiceProviderDetails() {
        // Empty ServiceProviderDetails
        ServiceProviderDetails spd = new ServiceProviderDetails();
        // fill with default content
        spd.setName("e-SENS");
        spd.setEndpointID("N/A");
        spd.setID("N/A");
        spd.setWebsiteURI("N/A");
        return spd;
    }

    default List<SelectableCriterion> getEmptyCriteriaList() {
        // Empty Criteria List
        return new ArrayList<>();
    }

    default void applyCriteriaWorkaround(Criterion c) {

//        if (c.getTypeCode().equals("SELECTION.ECONOMIC_FINANCIAL_STANDING")
//                || c.getTypeCode().equals("DATA_ON_ECONOMIC_OPERATOR")) {
        if (c.getDescription().equals("")) {
            String oldName = c.getName();
            c.setDescription(oldName);
            // Since we have no name, we will add the Criteria type name as Criterion Name
            c.setName(CodelistsV1.CriteriaType.getValueForId(c.getTypeCode()) + " (No Name)");
//                System.out.println("Workaround for: "+c.getID() +" "+c.getDescription());
        }
//        }
    }

    default InputStream getBufferedInputStream(InputStream xmlESPD) {
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
    default SchemaVersion findSchemaVersion(InputStream xmlESPD) {
        InputStream bis = getBufferedInputStream(xmlESPD);
        SchemaVersion version = SchemaVersion.UNKNOWN;

        try {
            byte[] contents = new byte[1024];
            int bytesRead;
            StringBuilder partOfTheArtefact = new StringBuilder();
            bis.mark(1024);
            while ((bytesRead = bis.read(contents)) != -1) {
                partOfTheArtefact.append(new String(contents, 0, bytesRead));
                if (bytesRead >= 1024) {
                    break;
                }
            }
            bis.reset();

            boolean isV1Artefact = Pattern.compile("ESPDRequest|ESPDResponse")
                    .matcher(partOfTheArtefact.toString()).find();

            if (isV1Artefact) { // v1 artefact found
                version = SchemaVersion.V1;
            } else {
                // check if it is a v2 artefact
                boolean isV2Artefact = Pattern.compile("QualificationApplicationRequest|QualificationApplicationResponse")
                        .matcher(partOfTheArtefact.toString()).find();
                if (isV2Artefact) { // v2 artefact found
                    version = SchemaVersion.V2;
                } else {
                    // nor v1 or v2 artefact found
                    version = SchemaVersion.UNKNOWN;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }

    /**
     * Identify profile execution id of given ESPD XML artefact.
     *
     * @param xmlESPD The ESPD XML artefact
     * @return The profile execution id
     */
    default ProfileExecutionIDEnum findEDMArtefactVersion(InputStream xmlESPD) throws BuilderException {
        InputStream bis = getBufferedInputStream(xmlESPD);
        ProfileExecutionIDEnum profileExecutionIDEnum = ProfileExecutionIDEnum.UNKNOWN;

        try {
            int numberOfBytes = 2048;
            byte[] contents = new byte[numberOfBytes];
            int bytesRead;
            StringBuilder partOfTheArtefact = new StringBuilder();
            bis.mark(numberOfBytes);
            while ((bytesRead = bis.read(contents)) != -1) {
                partOfTheArtefact.append(new String(contents, 0, bytesRead));
                if (bytesRead >= numberOfBytes) {
                    break;
                }
            }
            bis.reset();

            String v1ArtefactRegex = "ESPDRequest|ESPDResponse";
            boolean isV1Artefact = Pattern.compile(v1ArtefactRegex)
                    .matcher(partOfTheArtefact.toString()).find();

            if (isV1Artefact) { // v1 artefact found
                profileExecutionIDEnum = ProfileExecutionIDEnum.ESPD_EDM_V1_0_2;
            } else {
                // check if it is a v2 artefact
                String v2ArtefactRegex = "QualificationApplicationRequest|QualificationApplicationResponse";
                boolean isV2Artefact = Pattern.compile(v2ArtefactRegex)
                        .matcher(partOfTheArtefact.toString()).find();
                if (isV2Artefact) { // v2 artefact found
                    /**
                     * in v2.0.x artefacts <cbc:ProfileExecutionID> is mandatory element
                     * and get values from: {@link eu.esens.espdvcd.codelist.CodelistsV2#ProfileExecutionID}
                     */
                    String profileExecutionIDExtractionRegex = ".*<cbc:ProfileExecutionID.*?>(.*?)</cbc:ProfileExecutionID>.*";
                    Matcher m = Pattern.compile(profileExecutionIDExtractionRegex,
                            Pattern.DOTALL & Pattern.MULTILINE)
                            .matcher(partOfTheArtefact.toString());
                    if (m.find())  {
                        // extract <cbc:ProfileExecutionID> value
                        final String theId = m.group(1);
                        profileExecutionIDEnum = Arrays.stream(ProfileExecutionIDEnum.values())
                                .filter(id -> id.getValue().equals(theId))
                                .findAny().orElseThrow(() -> new BuilderException("Error... ProfileExecutionID element value doesn't match with any ProfileExecutionID Codelist value."));
                    } else {
                        throw new BuilderException("Error... Matcher couldn't find profile execution id value, by using regular expression.");
                    }

                } else {
                    // nor v1 or v2 artefact found
                    throw new BuilderException("Error... Imported artefact could not be identified as either v1 or v2.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profileExecutionIDEnum;
    }


}
