/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.RegulatedCriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CriteriaServiceTest {
    private final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new SimpleModule().setSerializerModifier(new BeanSerializerModifier() {
                @Override
                public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                    return beanProperties.stream().map(bpw -> new BeanPropertyWriter(bpw) {
                        @Override
                        public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
                            try {
                                super.serializeAsField(bean, gen, prov);
                            } catch (Exception e) {
                                Logger.getLogger(this.getClass().getName()).warning(String.format("Ignoring %s for field '%s' of %s instance", e.getClass().getName(), this.getName(), bean.getClass().getName()));
                            }
                        }
                    }).collect(Collectors.toList());
                }
            }))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new StdDateFormat())
            .writer().withDefaultPrettyPrinter();
    private CriteriaExtractor criteriaExtractorV1, criteriaExtractorV2;
    private CriteriaService predefinedCriteriaServiceV1, predefinedCriteriaServiceV2, selfContainedCriteriaService;

    @Before
    public void setUp() throws Exception {
        predefinedCriteriaServiceV1 = RegulatedCriteriaService.getV1Instance();
        predefinedCriteriaServiceV2 = RegulatedCriteriaService.getV2Instance();
        selfContainedCriteriaService = SelfContainedCriteriaService.getInstance();

        criteriaExtractorV1 = new RegulatedCriteriaExtractorBuilder(EDMVersion.V1).build();
        criteriaExtractorV2 = new RegulatedCriteriaExtractorBuilder(EDMVersion.V2).build();
    }

    @Ignore
    @Test
    public void testV1Criteria() throws RetrieverException {
        Assert.assertArrayEquals(predefinedCriteriaServiceV1.getCriteria().toArray(), criteriaExtractorV1.getFullList().toArray());
    }

    @Ignore
    @Test
    public void testV2Criteria() throws RetrieverException {
        Assert.assertArrayEquals(predefinedCriteriaServiceV2.getCriteria().toArray(), criteriaExtractorV2.getFullList().toArray());
    }

    @Test
    public void getUnselectedCriteriaV2() throws Exception {
        List<SelectableCriterion> theList = predefinedCriteriaServiceV2.getCriteria();
        theList.remove(30);
        List<SelectableCriterion> theListWithMissing30 = predefinedCriteriaServiceV2.getUnselectedCriteria(theList);
        Assert.assertFalse(theListWithMissing30.get(theListWithMissing30.size() - 1).isSelected());
//        System.out.println(theListWithMissing30.size());
    }

    @Test
    public void getUnselectedCriteriaV1() throws Exception {
        List<SelectableCriterion> theList = predefinedCriteriaServiceV1.getCriteria();
        theList.remove(30);
        List<SelectableCriterion> theListWithMissing30 = predefinedCriteriaServiceV1.getUnselectedCriteria(theList);
        Assert.assertFalse(theListWithMissing30.get(theListWithMissing30.size() - 1).isSelected());
//        System.out.println(theListWithMissing30.size());
    }

    @Test
    public void generateResponsesForSelfContained() throws Exception {
        List<SelectableCriterion> criteria = selfContainedCriteriaService.getCriteria();

        for (SelectableCriterion criterion : criteria) {
            requirementGroupResponseGenerator(criterion.getRequirementGroups());
        }

        System.out.println(WRITER.writeValueAsString(criteria));
    }

    private void requirementGroupResponseGenerator(List<RequirementGroup> requirementGroups) {
        for (RequirementGroup requirementGroup : requirementGroups) {
            for (Requirement requirement : requirementGroup.getRequirements()) {
                switch (requirement.getResponseDataType()) {
                    case INDICATOR:
                        requirement.setResponse(new IndicatorResponse());
                        break;
                    case DATE:
                        requirement.setResponse(new DateResponse());
                        break;
                    case DESCRIPTION:
                        requirement.setResponse(new DescriptionResponse());
                        break;
                    case EVIDENCE_URL:
                        requirement.setResponse(new EvidenceURLResponse());
                        break;
                    case QUANTITY:
                        requirement.setResponse(new QuantityResponse());
                        break;
                    case QUANTITY_YEAR:
                        requirement.setResponse(new QuantityYearResponse());
                        break;
                    case QUANTITY_INTEGER:
                        requirement.setResponse(new QuantityIntegerResponse());
                        break;
                    case AMOUNT:
                        requirement.setResponse(new AmountResponse());
                        break;
                    case CODE_COUNTRY:
                        requirement.setResponse(new CountryCodeResponse());
                        break;
                    case PERCENTAGE:
                        requirement.setResponse(new PercentageResponse());
                        break;
                    case PERIOD:
                        requirement.setResponse(new PeriodResponse());
                        break;
                    case CODE:
                        requirement.setResponse(new EvidenceURLCodeResponse());
                        break;
                    case EVIDENCE_IDENTIFIER:
                        requirement.setResponse(new EvidenceIdentifierResponse());
                        break;
                    case NONE:
                        break;
                    case IDENTIFIER:
                        requirement.setResponse(new IdentifierResponse());
                        break;
                    case URL:
                        requirement.setResponse(new URLResponse());
                        break;
                    case MAXIMUM_AMOUNT:
                        break;
                    case MINIMUM_AMOUNT:
                        break;
                    case MAXIMUM_VALUE_NUMERIC:
                        break;
                    case MINIMUM_VALUE_NUMERIC:
                        break;
                    case TRANSLATION_TYPE_CODE:
                        break;
                    case CERTIFICATION_LEVEL_DESCRIPTION:
                        break;
                    case COPY_QUALITY_TYPE_CODE:
                        break;
                    case TIME:
                        break;
                    case WEIGHT_INDICATOR:
                        requirement.setResponse(new WeightIndicatorResponse());
                        break;
                    case LOT_IDENTIFIER:
                        requirement.setResponse(new LotIdentifierResponse());
                        break;
                    case ECONOMIC_OPERATOR_IDENTIFIER:
                        requirement.setResponse(new EOIdentifierResponse());
                        break;
                }
            }
            requirementGroupResponseGenerator(requirementGroup.getRequirementGroups());
        }
    }
}