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
package eu.esens.espdvcd.codelist.enums;

/**
 * The EOIndustryClassificationCodeEnum provides a compile time enumeration of the available
 * Economic Operator industry classification codes. In order to be classified as an SME an EO has
 * to belong to one of the following Industry classification codes: {@link EOIndustryClassificationCodeEnum#MICRO},
 *                                                                  {@link EOIndustryClassificationCodeEnum#SMALL},
 *                                                                  {@link EOIndustryClassificationCodeEnum#MEDIUM},
 *                                                                  {@link EOIndustryClassificationCodeEnum#SME}
 *
 * @version 2.0.1
 * @since 2.0.1
 */
public enum EOIndustryClassificationCodeEnum {

    MICRO, SMALL, MEDIUM, SME, LARGE

}
