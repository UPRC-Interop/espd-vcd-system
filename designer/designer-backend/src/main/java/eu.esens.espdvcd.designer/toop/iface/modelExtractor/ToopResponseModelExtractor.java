package eu.esens.espdvcd.designer.toop.iface.modelExtractor;

import eu.esens.espdvcd.designer.toop.iface.model.ToopRequest;
import eu.esens.espdvcd.designer.toop.iface.model.ToopResponse;
import eu.toop.commons.dataexchange.TDETOOPResponseType;

public class ToopResponseModelExtractor {

    private ToopResponse response;

    public ToopResponse extractResponse(TDETOOPResponseType responseType){
        response = new ToopResponse();



        return response;
    }
}
