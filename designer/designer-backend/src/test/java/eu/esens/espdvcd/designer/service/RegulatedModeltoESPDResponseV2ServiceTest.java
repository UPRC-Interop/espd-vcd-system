package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.RegulatedESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.List;

public class RegulatedModeltoESPDResponseV2ServiceTest {

    File ESPDV2JsonResponse;

    @Before
    public void setUp() throws Exception {
        URI ESPDResource = this.getClass().getClassLoader().getResource("evi.json").toURI();
        ESPDV2JsonResponse = new File(ESPDResource);
    }

    @Test
    public void testV2ResponseExport() throws Exception{
        ESPDResponse v2Response = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule()
                        .addDeserializer(Requirement.class, new RequirementDeserialiser()))
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .readValue(ESPDV2JsonResponse, RegulatedESPDResponse.class);

        v2Response.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);

//      forEach(e->{
//            if (e.getEvidenceURL() == null){
//                v2Response.getEvidenceList().remove(e);
//            }
//      });

        System.out.println(BuilderFactory.withEDMVersion2().getDocumentBuilderFor(v2Response).getAsString());
    }

    private void recurseRequirementsAndGenerateEvidences(List<RequirementGroup> requirementGroups, List<Evidence> evidences){
        for (RequirementGroup rg : requirementGroups){
            rg.getRequirements().forEach(r -> {
                if(r.getResponseDataType().equals(ResponseTypeEnum.EVIDENCE_IDENTIFIER)){
                    Evidence e = new Evidence();
                    e.setID(r.getID());
                    e.setEvidenceURL("");
                    evidences.add(e);
                }
            });
            recurseRequirementsAndGenerateEvidences(rg.getRequirementGroups(), evidences);
        }
    }
}