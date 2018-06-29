package eu.esens.espdvcd.designer.toop.iface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.designer.model.ToopDataRequest;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TenderEnvelopeIDType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToopRequestTest {

    @Test
    public void createRequestAndSendToToopConnector() throws JsonProcessingException {
        ToopDataRequest request = new ToopDataRequest("sdfgsdfg", "SV");
        System.out.println(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new StdDateFormat())
                .writer().withDefaultPrettyPrinter().writeValueAsString(request));
    }
}