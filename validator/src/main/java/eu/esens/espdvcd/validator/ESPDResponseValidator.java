package eu.esens.espdvcd.validator;


import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import eu.esens.espdvcd.schema.XSD;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

/**
 * Created by Ulf Lotzmann on 04/05/2016.
 */
public class ESPDResponseValidator extends ESPDRequestValidator {
    public ESPDResponseValidator(InputStream is) throws SAXException, JAXBException {
        super(is);
    }

    @Override
    protected String getXSDPath() {
        // FIXME: the path returned by XSD.ESPD_RESPONSE.xsdPath() is probably incorrect, hence returning static string
        //return XSD.ESPD_RESPONSE.xsdPath();
        return "/schema/maindoc/ESPDResponse-1.0.xsd";
    }

    @Override
    protected Class getJaxbClass() {
        return ESPDResponseType.class;
    }
}
