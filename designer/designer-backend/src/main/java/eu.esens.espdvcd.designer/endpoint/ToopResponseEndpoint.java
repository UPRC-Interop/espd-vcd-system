package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.PostalAddress;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import spark.Request;
import spark.Response;
import spark.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static eu.esens.espdvcd.designer.Server.TOOPResponseQueue;

public class ToopResponseEndpoint extends Endpoint {

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                return "You need to POST an ASIC container.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST an ASIC container.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request request, Response response) throws IOException {
        LOGGER.info("Got TOOP Response from Toop Connector!");
        TDETOOPResponseType TOOPResponse = ToopMessageBuilder.parseResponseMessage(new ByteArrayInputStream(request.bodyAsBytes()));
        LOGGER.info("Extracting response...");
        //DEBUG
//        Files.write(request.bodyAsBytes(), new File("response.asic"));

        TOOPResponseQueue.add(extractEODetails(TOOPResponse));
        return "";
    }

    private EODetails extractEODetails(TDETOOPResponseType TOOPResponse) throws JsonProcessingException {
        List<TDEDataElementResponseValueType> theList = new ArrayList<>();
        final EODetails eoDetails = new EODetails();

        TOOPResponse.getDataElementRequest().forEach(e -> {
            String providedDataName = e.getConceptRequest().getConceptRequestAtIndex(0).getConceptName().getValue();
            String providedDataValue = e.getConceptRequest().getConceptRequestAtIndex(0).getConceptRequestAtIndex(0).getDataElementResponseValueAtIndex(0).getResponseDescription().getValue();
            if (providedDataName != null && providedDataValue != null) {
                switch (providedDataName) {
                    case "CompanyCode":
                        eoDetails.setID(providedDataValue);
                        break;
                    case "companyName":
                        eoDetails.setName(providedDataValue);
                        break;
                    case "companyType":
                        eoDetails.setSmeIndicator(providedDataValue.equalsIgnoreCase("sme"));
                        break;
                    case "Address":
                        PostalAddress address = new PostalAddress();
                        String[] addressParts = providedDataValue.split(",[ ]*");
                        address.setAddressLine1(addressParts[0]);
                        address.setPostCode(addressParts[1]);
                        address.setCity(addressParts[2]);
                        if (addressParts[3].equalsIgnoreCase("Elonia"))
                            address.setCountryCode("SV");
                        else if (addressParts[3].equalsIgnoreCase("Sweden"))
                            address.setCountryCode("SW");
                        else
                            address.setCountryCode(addressParts[3]);
                        eoDetails.setPostalAddress(address);
                        break;
                }
            }
        });
        LOGGER.info(WRITER.writeValueAsString(eoDetails));
        return eoDetails;
    }
}
