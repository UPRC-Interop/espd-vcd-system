package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class NationalCriteriaMappingServiceTest {

    @Test
    public void getNationalCriteria() throws RetrieverException {
        CriteriaDataRetriever retriever = new CriteriaDataRetrieverBuilder().build();
        List<SelectableCriterion> criteria = retriever.getNationalCriterionMapping("14df34e8-15a9-411c-8c05-8c051693e277", "DE");
        criteria.forEach(criterion -> {
            Assert.assertEquals("Criminal organisation", criterion.getName());
            Assert.assertEquals("\"(1) Public contracting authorities shall " +
                    "exclude an undertaking from participation at any point in the procurement " +
                    "procedure when they are aware that a person whose conduct is imputable to the " +
                    "undertaking in accordance with paragraph 3 has been convicted by final judgement " +
                    "or a final administrative fine has been issued against the undertaking under § " +
                    "30 of the German Administrative Offences Act [Gesetz über Ordnungswidrigkeiten] " +
                    "for a criminal offence under: 1. § 129 of the German Criminal Code [Strafgesetzbuch] " +
                    "(forming criminal organisations), § 129a of the German Criminal Code " +
                    "(forming terrorist organisations) or § 129b of the German Criminal Code " +
                    "(criminal and terrorist organisations abroad);\"", criterion.getDescription());
        });
    }

    @Test
    public void getTranslatedNationalCriteria() throws RetrieverException {
        NationalCriteriaMappingService service = NationalCriteriaMappingService.getInstance();
        List<SelectableCriterion> criteria = service.getTranslatedNationalCriteria(
                "14df34e8-15a9-411c-8c05-8c051693e277",
                "DE",
                "DE");
        criteria.forEach(criterion -> {
            Assert.assertEquals("Kriminelle Vereinigungen", criterion.getName());
            Assert.assertEquals("\"Der öffentliche Auftraggeber schließt ein Unternehmen " +
                    "zu jedem Zeitpunkt des Vergabeverfahrens von der Teilnahme aus, wenn er Kenntnis davon hat, " +
                    "dass eine Person, deren Verhalten nach Absatz 3 dem Unternehmen zuzurechnen " +
                    "ist, rechtskräftig verurteilt worden ist nach:\n" +
                    "\n" +
                    "1. § 129 des Strafgesetzbuchs (StGB) (Bildung krimineller Vereinigungen), " +
                    "§ 129a StGB (Bildung terroristischer Vereinigungen) oder " +
                    "§ 129b StGB (kriminelle und terroristische Vereinigungen im Ausland),\"\n", criterion.getDescription());
        });
    }
}