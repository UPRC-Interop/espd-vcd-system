package eu.esens.espdvcd.designer.toop.iface;

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
    public void createRequestAndSendToToopConnector() {

        ToopRequest.createRequestAndSendToToopConnector(new TDEDataRequestSubjectType(),
                new IdentifierType(), "",
                new EPredefinedDocumentTypeIdentifier(),
                new EPredefinedProcessIdentifier(),
                new List<? extends ConceptValue>);
    }
}