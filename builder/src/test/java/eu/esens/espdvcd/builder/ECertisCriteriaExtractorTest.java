package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author konstantinos
 */
public class ECertisCriteriaExtractorTest {

    private ECertisCriteriaExtractor extractor;

    @Before
    public void setUp() {
        extractor = new ECertisCriteriaExtractor();
    }

    // @Ignore
    @Test
    public void testGetAllEuropeanCriteriaID() throws RetrieverException {

        List<String> results = extractor.getAllEuropeanCriteriaID();

        assertThat(results, containsInAnyOrder(
                "d726bac9-e153-4e75-bfca-c5385587766d", "cc18c023-211d-484d-a32e-52f3f970285f",
                "3aaca389-4a7b-406b-a4b9-080845d127e7", "4bf996d9-439c-40c6-9ab9-980a48cb55a1",
                "612a1625-118d-4ea4-a6db-413184e7c0a8", "dc12a151-7fdf-4733-a8f0-30f667292e66",
                "90a2e100-44cc-45d3-9970-69d6714f1596", "6346959b-e097-4ea1-89cd-d1b4c131ea4d",
                "c599c130-b29f-461e-a187-4e16c7d40db7", "cdd3bb3e-34a5-43d5-b668-2aab86a73822",
                "bdf0601d-2480-4250-b870-658d0ee95be6", "7662b7a9-bcb8-4763-a0a7-7505d8e8470d",
                "3a18a175-1863-4b1d-baef-588ce61960ca", "a7669d7d-9297-43e1-9d10-691a1660187c",
                "5e506c16-26ab-4e32-bb78-b27f87dc0565", "9460457e-b43d-48a9-acd1-615de6ddd33e",
                "07301031-2270-41af-8e7e-66fe0c777107", "1f49b3f0-d50f-43f6-8b30-4bafab108b9b",
                "c8809aa1-29b6-4f27-ae2f-27e612e394db", "074f6031-55f9-4e99-b9a4-c4363e8bc315",
                "d3dfb714-f558-4512-bbc5-e456fa2339de", "77f481ce-ffb6-483f-8e2b-c78db5e68292",
                "7604bd40-4462-4086-8763-a50da51a869c", "ab0e7f2e-6418-40e2-8870-6713123e41ad",
                "499efc97-2ac1-4af2-9e84-323c2ca67747", "e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46",
                "b16cb9fc-6cb7-4585-9302-9533b415cf48", "73f10e36-ed7a-412e-995c-aa76463e3776",
                "9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4", "6ee55a59-6adb-4c3a-b89f-e62a7ad7be7f",
                "63adb07d-db1b-4ef0-a14e-a99785cf8cf6", "396f288a-e267-4c20-851a-ed4f7498f137",
                "696a75b2-6107-428f-8b74-82affb67e184", "514d3fde-1e3e-4dcd-b02a-9f984d5bbda3",
                "3293e92b-7f3e-42f1-bee6-a7641bb04251", "b1b5ac18-f393-4280-9659-1367943c1a2e",
                "166536e2-77f7-455c-b018-70582474e4f6", "a261a395-ed17-4939-9c75-b9ff1109ca6e",
                "a34b70d6-c43d-4726-9a88-8e2b438424bf", "a80ddb62-d25b-4e4e-ae22-3968460dc0a9",
                "d3732c09-7d62-4edc-a172-241da6636e7c", "8fda202a-0c37-41bb-9d7d-de3f49edbfcb",
                "68918c7a-f5bc-4a1a-a62f-ad8983600d48", "d486fb70-86b3-4e75-97f2-0d71b5697c7d",
                "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab", "297d2323-3ede-424e-94bc-a91561e6f320",
                "c27b7c4e-c837-4529-b867-ed55ce639db5", "8ed65e48-fd0d-444f-97bd-4f58da632999",
                "d789d01a-fe03-4ccd-9898-73f9cfa080d1", "daffa2a9-9f8f-4568-8be8-7b8bf306d096",
                "56d13e3d-76e8-4f23-8af6-13e60a2ee356", "47112079-6fec-47a3-988f-e561668c3aef",
                "b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4", "c6edf1cb-98a8-4f5e-b806-023c970e34b6",
                "87b3fa26-3549-4f92-b8e0-3fd8f04bf5c7", "7d85e333-bbab-49c0-be8d-c36d71a72f5e"));

    }

    // @Ignore
    @Test
    public void testGetFullList() throws RetrieverException {
        
        extractor.getFullList().forEach( (SelectableCriterion sc) -> {
            
            System.out.println(sc.getID() + " " + sc.getName() + " (" + sc.getTypeCode() + ")");
            traverseLegislationReference(sc.getLegislationReference());
            
        });
    }

    private void traverseLegislationReference(LegislationReference lr) {
        
        String tab = "\t";
       
        System.out.println("\n" + tab + "LegislationReference");
        System.out.println(tab + "====================");
        System.out.println(tab + "Title: " + lr.getTitle());
        System.out.println(tab + "Description: " + lr.getDescription());
        System.out.println(tab + "JurisdictionLevelCode: " + lr.getJurisdictionLevelCode());
        System.out.println(tab + "Article: " + lr.getArticle());
        System.out.println(tab + "URI: " + lr.getURI() + "\n");        
    }
    
}
