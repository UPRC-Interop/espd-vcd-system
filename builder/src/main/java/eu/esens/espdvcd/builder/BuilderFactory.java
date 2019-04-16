/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder;

/**
 * Factory class that can be used to create the available Builder
 * Factories for both exchange data model (EDM) versions.
 *
 * @since 2.0.2
 */
public class BuilderFactory {

    public static final BuilderFactoryV1 EDM_V1 = new BuilderFactoryV1();

    public static final BuilderFactoryV2 EDM_V2 = new BuilderFactoryV2();

}
