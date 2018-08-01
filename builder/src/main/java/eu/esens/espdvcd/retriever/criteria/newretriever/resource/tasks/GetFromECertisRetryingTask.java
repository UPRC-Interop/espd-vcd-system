package eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GetFromECertisRetryingTask implements Callable<String> {

    private GetFromECertisTask task;

    public GetFromECertisRetryingTask(GetFromECertisTask task) {
        this.task = task;
    }

    @Override
    public String call() throws IOException, ExecutionException, RetryException {

        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                .retryIfResult(Predicates.<String>isNull())
                // .retryIfExceptionOfType(IOException.class)
                .retryIfException()
                // .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
                // .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                // .withStopStrategy(StopStrategies.neverStop())
                .withStopStrategy(StopStrategies.stopAfterDelay(4, TimeUnit.SECONDS))
                .build();

        return retryer.call(task);
    }

}
