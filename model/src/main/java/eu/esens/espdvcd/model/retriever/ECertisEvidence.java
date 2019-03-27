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
package eu.esens.espdvcd.model.retriever;

import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface ECertisEvidence {

    void setID(String ID);

    String getID();

    void setTypeCode(String typeCode);

    String getTypeCode();

    void setName(String name);
    
    String getName();

    void setDescription(String description);

    String getDescription();
   
    void setVersionID(String versionID);

    String getVersionID();

    void setFeeAmount(ECertisAmount feeAmount);
        
    ECertisAmount getFeeAmount();
    
    void setEvidenceIntendedUse(ECertisEvidenceIntendedUse evidenceIntendedUse);

    ECertisEvidenceIntendedUse getEvidenceIntendedUse();

    void setEvidenceIssuerParty(List<ECertisEvidenceIssuerParty> evidenceIssuerParty);

    List<ECertisEvidenceIssuerParty> getEvidenceIssuerParty();

    void setAddresseeDescription(String addresseeDescription);

    String getAddresseeDescription();

    void setJurisdictionLevelCode(List<String> jurisdictionLevelCode);

    List<String> getJurisdictionLevelCode();

    void setEvidenceDocumentReference(List<ECertisEvidenceDocumentReference> evidenceDocumentReference);

    List<ECertisEvidenceDocumentReference> getEvidenceDocumentReference();
    
}
