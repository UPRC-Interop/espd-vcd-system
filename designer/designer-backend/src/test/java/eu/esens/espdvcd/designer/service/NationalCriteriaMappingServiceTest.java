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
            Assert.assertEquals("\"Der öffentliche Auftraggeber schlie�?t ein Unternehmen " +
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