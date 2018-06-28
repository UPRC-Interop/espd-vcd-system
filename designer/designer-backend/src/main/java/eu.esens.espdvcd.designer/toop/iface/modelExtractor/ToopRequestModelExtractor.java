package eu.esens.espdvcd.designer.toop.iface.modelExtractor;

import eu.esens.espdvcd.designer.toop.iface.model.ToopRequest;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

public class ToopRequestModelExtractor {

    private ToopRequest request;

    public ToopRequestModelExtractor(){
        request = new ToopRequest();
    }

    private void extractRequest(TDETOOPRequestType requestType){

    }

    private void extractDataConsumer(){

    }
}
