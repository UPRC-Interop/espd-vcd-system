package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.SchemaVersion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ModelBuilderV2 implements ModelBuilder {

    private EODetails eoDetails = null;
    private CADetails caDetails = null;
    private ServiceProviderDetails serviceProviderDetails = null;
    private CriteriaExtractor criteriaExtractor = null;
    private InputStream importStream = null;

    /* package private constructor. Create only through factory */
    ModelBuilderV2() {
    }

    public ModelBuilderV2 importFrom(InputStream is) {
        importStream = getBufferedInputStream(is);
        return this;
    }

    public ModelBuilderV2 withCADetailsFrom(InputStream is) throws BuilderException {
        // code goes here
        return this;
    }

    public ESPDRequest createRegulatedESPDRequest() throws BuilderException {
        ESPDRequest req;
        if (importStream != null) {
            req = createRegulatedESPDRequestFromXML(importStream);
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList(req.getFullCriterionList()));
                } catch (RetrieverException ex) {
                    Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            req = new RegulatedESPDRequest();
            if (criteriaExtractor != null) {
                try {
                    req.setCriterionList(criteriaExtractor.getFullList());
                } catch (RetrieverException ex) {
                    Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                req.setCriterionList(getEmptyCriteriaList());
            }
            req.setCADetails(createDefaultCADetails());
            req.setServiceProviderDetails(createDefaultServiceProviderDetails());
        }

        //Overriding the default/imported ca details
        if (caDetails != null) {
            req.setCADetails(caDetails);
        }

        // Overriding the default/imported service provider details
        if (serviceProviderDetails != null) {
            req.setServiceProviderDetails(serviceProviderDetails);
        }

        // Apply workaround
        // req.getFullCriterionList().forEach(this::applyCriteriaWorkaround);
        req.getFullCriterionList().forEach(sc -> applyCriteriaWorkaround(sc, SchemaVersion.V2));

        return req;
    }

    public ESPDRequest createSelfContainedESPDRequest() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createRegulatedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createSelfContainedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    /**
     * Parses the input stream and creates an ESPDRequest model instance.
     *
     * @param xmlESPD The input stream of the XML document to be parsed
     * @return a prefilled ESPDRequest based on the input data
     * @throws BuilderException when the parsing from XML to ESPDRequest Model
     *                          fails
     */
    private ESPDRequest createRegulatedESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {

        ESPDRequest req;

        try (InputStream bis = getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            // but first identify the artefact schema version
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
            StringBuilder partOfTheArtefact = new StringBuilder();
            int numberOfLines = 0;
            String inputLine;

            // Read stream
            reader.mark(717);
            while ((inputLine = reader.readLine()) != null) {
                    // && numberOfLines < 2) {
                partOfTheArtefact.append(inputLine);
                numberOfLines++;
                if (numberOfLines > 1) {
                    break;
                }
            }


            // --
            System.out.println("Print 1\n" + partOfTheArtefact.toString());
            System.out.println("# " + partOfTheArtefact.toString().length());
            // --

            // reset the stream
            reader.reset();

            // ---

            StringBuilder partOfTheArtefact2 = new StringBuilder();
            int numberOfLines2 = 0;
            String inputLine2;

            // Read stream again
            reader.mark(717);
            while ((inputLine2 = reader.readLine()) != null) {
                partOfTheArtefact2.append(inputLine2);
                numberOfLines2++;
                if (numberOfLines2 > 1) { // read only the first 2 lines of the artefact
                    break;
                }
            }

            System.out.println("Print 2\n" + partOfTheArtefact2.toString());
            System.out.println("# " + partOfTheArtefact2.toString().length());
            // --

            // reset the stream
            reader.reset();

            boolean isV1Artefact = Pattern.compile("ESPDRequest")
                    .matcher(partOfTheArtefact.toString()).find();

            if (isV1Artefact) { // v1 artefact found
                Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.INFO, "v1 artefact found...");
                ESPDRequestType reqType = readESPDRequestFromStream(bis);
                // Create the Model Object
                req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
            } else {
                // check if it is a v2 artefact
                boolean isV2Artefact = Pattern.compile("QualificationApplicationRequest")
                        .matcher(partOfTheArtefact.toString()).find();
                if (isV2Artefact) { // v2 artefact found
                    Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.INFO, "v2 artefact found...");
                    QualificationApplicationRequestType reqType = readQualificationApplicationRequestFromStream(bis);
                    // Create the Model Object
                    req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
                } else {
                    // nor v1 or v2 artefact found
                    throw new BuilderException("Error... Imported artefact cound not identified neither as v1 nor as v2.");
                }
            }

            return req;

        } catch (IOException | JAXBException ex) {
            Logger.getLogger(ModelBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }

    }

    private QualificationApplicationRequestType readQualificationApplicationRequestFromStream(InputStream is) throws JAXBException {

        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), QualificationApplicationRequestType.class).getValue();
    }

    private ESPDRequestType readESPDRequestFromStream(InputStream is) throws JAXBException {

        // Start with the convenience methods provided by JAXB. If there are
        // performance issues we will switch back to the JAXB API Usage
        return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDRequestType.class).getValue();
    }

}
