package eu.esens.espdvcd.retriever.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.EvidencesResource;
import eu.esens.espdvcd.retriever.criteria.resource.SelectableCriterionPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilderTest {


    private CriteriaDataRetriever r;

    @Before
    public void setUp() {
        r = new CriteriaDataRetrieverBuilder().build();
    }

    @Test
    public void testGetCriterion() throws Exception {

        SelectableCriterion sc = r.getCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");
        SelectableCriterionPrinter.print(sc);
    }

    @Test
    public void testGetECertisCriterion() throws Exception {

        ECertisResource r = new ECertisResource();
        List<EvidencesResource> rList = new ArrayList<>();
        rList.add(r);
        CriteriaDataRetrieverImpl rImpl = new CriteriaDataRetrieverImpl(rList);

        ECertisCriterion ec = rImpl.getECertisCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Print JSON String
        String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ec);
        System.out.println(prettyCt);
    }

    @Test
    public void testEuToNationalMapping() throws Exception {

        // String ID = "14df34e8-15a9-411c-8c05-8c051693e277";
        String ID = "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab";
        String code = EULanguageCodeEnum.EL.name();

        List<String> ncList = r.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID())
                .collect(Collectors.toList());

        System.out.printf("European Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
    }

    @Test
    public void testNationalToNationalMapping() throws Exception {

        String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";
        String code = EULanguageCodeEnum.HU.name();

        List<String> ncList = r.getNationalCriterionMapping(ID, code)
                .stream()
                .map(c -> c.getID())
                .collect(Collectors.toList());

        System.out.printf("National Criterion with ID %s mapped to National (%s) Criterion/s --> %s\n"
                , ID, CodelistsV2.LanguageCodeEU.getValueForId(code), ncList);
    }


    @Test
    public void testGetEvidenceGroup() throws Exception {

        final String ID = "fdab2c29-ab6d-4ce1-92c2-5663732dd022";

        List<Evidence> eList = r.getEvidences(ID);
        Assert.assertFalse(eList.isEmpty());

        int index = 1;

        for (Evidence e : r.getEvidences(ID)) {
            System.out.printf("#Evidence %-2d\nID: %s Description: %s Evidence Url: %s\n", index++, e.getID(), e.getDescription(), e.getEvidenceURL());
        }

    }

}
