package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.schema.SchemaVersion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    default void applyCriteriaWorkaround(Criterion c, SchemaVersion sv) {

//        if (c.getTypeCode().equals("SELECTION.ECONOMIC_FINANCIAL_STANDING")
//                || c.getTypeCode().equals("DATA_ON_ECONOMIC_OPERATOR")) {
        if (c.getDescription().equals("")) {
            String oldName = c.getName();
            c.setDescription(oldName);
            // Since we have no name, we will add the Criteria type name as Criterion Name
            switch (sv) {
                case V1:
                    c.setName(CodelistsV1.CriteriaType.getValueForId(c.getTypeCode()) + " (No Name)");
                    break;
                case V2:
                    c.setName(CodelistsV1.CriteriaType.getValueForId(c.getTypeCode()) + " (No Name)");
                    break;
                default:
                    Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, "Error... Unknown schema version");
            }
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

            boolean isV1Artefact = Pattern.compile("ESPDRequest")
                    .matcher(partOfTheArtefact.toString()).find();

            if (isV1Artefact) { // v1 artefact found
                version = SchemaVersion.V1;
            } else {
                // check if it is a v2 artefact
                boolean isV2Artefact = Pattern.compile("QualificationApplicationRequest")
                        .matcher(partOfTheArtefact.toString()).find();
                if (isV2Artefact) { // v2 artefact found
                    version = SchemaVersion.V2;
                } else {
                    // nor v1 or v2 artefact found
                    version = SchemaVersion.UNKNOWN;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return version;
    }

}
