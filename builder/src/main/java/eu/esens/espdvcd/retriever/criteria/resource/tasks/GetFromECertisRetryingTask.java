/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Konstantinos Raptis
 */
public class GetFromECertisRetryingTask implements Callable<String> {

    private GetFromECertisTask task;

    public GetFromECertisRetryingTask(GetFromECertisTask task) {
        this.task = task;
    }

    @Override
    public String call() throws ExecutionException, RetryException {

        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                .retryIfResult(Predicates.<String>isNull())
                .retryIfExceptionOfType(TimeoutException.class)
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
                .withStopStrategy(StopStrategies.stopAfterDelay(4, TimeUnit.SECONDS))
                .build();

        return retryer.call(task);
    }

}
