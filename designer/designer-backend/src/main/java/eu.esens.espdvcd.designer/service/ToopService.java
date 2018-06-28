package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.designer.model.ToopDataRequest;
import eu.toop.commons.dataexchange.TDETOOPResponseType;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface ToopService {

    void onToopResponse (@Nonnull TDETOOPResponseType aResponse) throws IOException;
    void onToopRequest (ToopDataRequest dataRequest);
}
