package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;
import java.time.LocalDate;

public class ApplicablePeriodResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1338028276838763638L;

    private LocalDate startDate;
    private LocalDate endDate;

    public ApplicablePeriodResponse() {

    }

    public ApplicablePeriodResponse(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
