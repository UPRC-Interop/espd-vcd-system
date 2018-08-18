package eu.esens.espdvcd.schema;

import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import eu.espd.schema.v1.commonaggregatecomponents_2.AddressType;
import eu.espd.schema.v1.commonaggregatecomponents_2.ContractingPartyType;
import eu.espd.schema.v1.commonaggregatecomponents_2.PartyType;
import eu.espd.schema.v1.commonbasiccomponents_2.BuildingNameType;
import eu.espd.schema.v1.commonbasiccomponents_2.IDType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdrequest_1.ObjectFactory;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ESPDCreationTests {

    public ESPDCreationTests() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void createESPDRequest() {

        ObjectFactory of = new ObjectFactory();
        ESPDRequestType req = new ESPDRequestType();

        //Add an ID
        req.setID(new IDType());
        req.getID().setSchemeID("Lala");
        req.getID().setValue("Value");

        req.setContractingParty(new ContractingPartyType());
        req.getContractingParty().setParty(new PartyType());
        req.getContractingParty().getParty().setPostalAddress(new AddressType());
        req.getContractingParty().getParty().getPostalAddress().setBuildingName(new BuildingNameType());
        req.getContractingParty().getParty().getPostalAddress().getBuildingName().setValue("Address Building 123");

        //This must be validated using code list for examle
        req.getContractingParty().getParty().getPostalAddress().getBuildingName().setLanguageID("EN");

        System.out.println(toXml(of.createESPDRequest(req), SchemaVersion.V1));

    }

    @Test
    public void createESPDResponse() {

        eu.espd.schema.v1.espdresponse_1.ObjectFactory of = new eu.espd.schema.v1.espdresponse_1.ObjectFactory();
        ESPDResponseType res = new ESPDResponseType();
        res.setID(new IDType());
        res.getID().setSchemeID("Lala");
        res.getID().setValue("Value");

        System.out.println(toXml(of.createESPDResponse(res), SchemaVersion.V1));

    }

    @Test
    public void createESPDRequestForV2() {

        eu.espd.schema.v2.pre_award.qualificationapplicationrequest.ObjectFactory of = new eu.espd.schema.v2.pre_award.qualificationapplicationrequest.ObjectFactory();
        QualificationApplicationRequestType req = new QualificationApplicationRequestType();

        //Add an ID
        req.setID(new eu.espd.schema.v2.pre_award.commonbasic.IDType());
        req.getID().setSchemeID("Lala");
        req.getID().setValue("Value");

        req.getContractingParty().add(new eu.espd.schema.v2.pre_award.commonaggregate.ContractingPartyType());
        req.getContractingParty().get(0).setParty(new eu.espd.schema.v2.pre_award.commonaggregate.PartyType());
        req.getContractingParty().get(0).getParty().setPostalAddress(new eu.espd.schema.v2.pre_award.commonaggregate.AddressType());
        req.getContractingParty().get(0).getParty().getPostalAddress().setBuildingName(new eu.espd.schema.v2.pre_award.commonbasic.BuildingNameType());
        req.getContractingParty().get(0).getParty().getPostalAddress().getBuildingName().setValue("Address Building 123");
        //This must be validated using code list for examle
        req.getContractingParty().get(0).getParty().getPostalAddress().getBuildingName().setLanguageID("EN");

        System.out.println(toXml(of.createQualificationApplicationRequest(req), SchemaVersion.V2));

    }

    @Test
    public void createESPDResponseForV2() {

        eu.espd.schema.v2.pre_award.qualificationapplicationresponse.ObjectFactory of = new eu.espd.schema.v2.pre_award.qualificationapplicationresponse.ObjectFactory();
        QualificationApplicationResponseType res = new QualificationApplicationResponseType();
        res.setID(new eu.espd.schema.v2.pre_award.commonbasic.IDType());
        res.getID().setSchemeID("Lala");
        res.getID().setValue("Value");

        System.out.println(toXml(of.createQualificationApplicationResponse(res), SchemaVersion.V2));

    }

    private String toXml(JAXBElement element, SchemaVersion version) {
        try {

            Marshaller marshaller = SchemaUtil.getMarshaller(version);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(element, baos);
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
