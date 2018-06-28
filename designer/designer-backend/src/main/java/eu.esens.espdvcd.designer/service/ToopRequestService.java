package eu.esens.espdvcd.designer.service;

import com.helger.asic.SignatureHelper;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.resourceprovider.DefaultResourceProvider;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import eu.esens.espdvcd.designer.Config;
import eu.esens.espdvcd.designer.model.ToopDataRequest;
import eu.esens.espdvcd.designer.toop.iface.Util.HttpClientInvoker;
import eu.esens.espdvcd.designer.toop.iface.model.ToopRequest;
import eu.esens.espdvcd.designer.toop.iface.schemaExtractor.ToopRequestSchemaExtractor;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class ToopRequestService implements ToopService{


    @Override
    public void onToopResponse(@Nonnull TDETOOPResponseType aResponse) throws IOException {

    }

    @Override
    public void onToopRequest(ToopDataRequest dataRequest) {
        //MAP datarequest from UI to a TOOP request
        //Call method below to send request to connector
    }

    private static void createRequestAndSendToToopConnector (ToopRequest request) throws IOException {

        final SignatureHelper aSH = new SignatureHelper (new DefaultResourceProvider().getInputStream (Config.getKeystorePath ()),
                Config.getKeystorePassword (),
                Config.getKeystoreKeyAlias (),
                Config.getKeystoreKeyPassword ());

        try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {

            TDETOOPRequestType aRequest = new ToopRequestSchemaExtractor().extractRequest(request);

            ToopMessageBuilder.createRequestMessage (aRequest, aBAOS, aSH);

            final String aFromDCUrl = Config.getToopConnectorDPUrl ();
            HttpClientInvoker.httpClientCallNoResponse (aFromDCUrl, aBAOS.toByteArray ());
        }
    }
}
