package eu.esens.espdvcd.model.retriever;

import eu.esens.espdvcd.model.Evidence;

/**
 *
 * @author Konstantinos Raptis
 */
public class ECertisEvidenceGroup {

    private String ID;
    private Evidence evidence;

    public ECertisEvidenceGroup() {
    }

    public ECertisEvidenceGroup(String ID, Evidence evidence) {
        this.ID = ID;
        this.evidence = evidence;
    }
        
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Evidence getEvidence() {
        return evidence;
    }

    public void setEvidence(Evidence evidence) {
        this.evidence = evidence;
    }
        
}
