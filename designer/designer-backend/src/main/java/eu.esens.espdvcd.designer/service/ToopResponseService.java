package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.model.ToopDataRequest;
import eu.toop.commons.dataexchange.TDETOOPResponseType;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ToopResponseService implements ToopService{
    @Override
    public void onToopResponse(@Nonnull TDETOOPResponseType aResponse) throws IOException {

    }

    @Override
    public void onToopRequest(ToopDataRequest dataRequest) {

    }
}
