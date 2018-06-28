package eu.esens.espdvcd.designer.toop.iface.schemaExtractor;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import eu.esens.espdvcd.designer.toop.iface.model.ToopRequest;
import eu.esens.espdvcd.designer.toop.iface.model.DataConsumer;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IndicatorType;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalTime;

public class ToopRequestSchemaExtractor {

    TDETOOPRequestType type;

    public TDETOOPRequestType extractRequest(ToopRequest request){
        IndicatorType copyIndicator = new IndicatorType();
        copyIndicator.setValue(false);
        type.setCopyIndicator(copyIndicator);

        IdentifierType documentTypeIdentifier = new IdentifierType();
        documentTypeIdentifier.setSchemeID("toop-doctypeid-qns");
        documentTypeIdentifier.setValue("urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10");
        type.setDocumentTypeIdentifier(documentTypeIdentifier);

        IdentifierType specificationIdentifier = new IdentifierType();
        specificationIdentifier.setSchemeID("toop-doctypeid-qns");
        specificationIdentifier.setValue("urn:eu:toop:ns:dataexchange-1p10::Request");
        type.setSpecificationIdentifier(specificationIdentifier);

        TDEDataConsumerType dataConsumer = new TDEDataConsumerType();
        IdentifierType dataConsumerElectronicAddressIdentifier = new IdentifierType();
        dataConsumerElectronicAddressIdentifier.setSchemeAgencyID("9999");
        dataConsumerElectronicAddressIdentifier.setSchemeID("iso6523-actorid-upis");
        dataConsumerElectronicAddressIdentifier.setValue("9999:gsccp-espd");
        type.setDataConsumer(dataConsumer);

        XMLGregorianCalendar today = new XMLGregorianCalendarImpl();
        today.setDay(LocalDate.now().getDayOfMonth());
        today.setMonth(LocalDate.now().getMonthValue());
        today.setYear(LocalDate.now().getYear());
        type.setDocumentIssueDate(today);

        XMLGregorianCalendar currentTime = new XMLGregorianCalendarImpl();
        currentTime.setHour(LocalTime.now().getHour());
        currentTime.setMinute(LocalTime.now().getMinute());
        currentTime.setSecond(LocalTime.now().getSecond());
        type.setDocumentIssueTime(currentTime);



        return type;
    }
}
