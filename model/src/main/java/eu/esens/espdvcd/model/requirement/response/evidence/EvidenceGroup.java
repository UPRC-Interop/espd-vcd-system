package eu.esens.espdvcd.model.requirement.response.evidence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Criterion evidence group
 *
 */
public class EvidenceGroup implements Serializable {

    private static final long serialVersionUID = -2279415611032396712L;

    private String ID;

    private List<Evidence> evidences;

    public EvidenceGroup(String ID) {
        this.ID = ID;
    }

    public EvidenceGroup(String ID, List<Evidence> evidences) {
        this.ID = ID;
        this.evidences = evidences;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public List<Evidence> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }
}
