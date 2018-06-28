package eu.esens.espdvcd.designer.toop.iface.Util;

import com.helger.commons.ValueEnforcer;
import com.helger.httpclient.HttpClientFactory;
import com.helger.httpclient.HttpClientManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * This class can be used to send something from DC or DP to the
 * MessageProcessor.
 *
 * @author Philip Helger
 */
@Immutable
public final class HttpClientInvoker {
    private HttpClientInvoker () {
    }

    public static <T> void httpClientCall (@Nonnull final String sDestinationURL, @Nonnull final byte[] aDataToSend,
                                           @Nonnull final ResponseHandler<T> aResponseHandler,
                                           @Nonnull final Consumer<? super T> aResultHandler) throws IOException {
        ValueEnforcer.notEmpty (sDestinationURL, "DestinationURL");
        ValueEnforcer.notNull (aDataToSend, "DataToSend");
        ValueEnforcer.notNull (aResponseHandler, "ResponseHandler");
        ValueEnforcer.notNull (aResultHandler, "ResultHandler");

        try (final HttpClientManager aMgr = new HttpClientManager ()) {
            final HttpPost aPost = new HttpPost (sDestinationURL);
            aPost.setEntity (new ByteArrayEntity(aDataToSend));

            final T aResponse = aMgr.execute (aPost, aResponseHandler);
            aResultHandler.accept (aResponse);
        }
    }

    public static void httpClientCallNoResponse (@Nonnull final String sDestinationURL,
                                                 @Nonnull final byte[] aDataToSend) throws IOException {
        ValueEnforcer.notEmpty (sDestinationURL, "DestinationURL");
        ValueEnforcer.notNull (aDataToSend, "DataToSend");

        httpClientCall(sDestinationURL, aDataToSend, new ResponseHandler<Object>() {
            @Override
            public Object handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return null;
            }
        }, x -> {
            // do nothing
        });
    }
}
