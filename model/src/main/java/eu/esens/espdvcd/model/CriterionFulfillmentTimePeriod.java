package eu.esens.espdvcd.model;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class CriterionFulfillmentTimePeriod {


    /**
     * Period start date
     * <p>
     * The date when the period starts. The date is the first day of the period.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-171<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.StartDate<br>
     */
    private GregorianCalendar startDate;


    /**
     * Period start time
     * <p>
     * The start time of the period.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-172<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.StartTime<br>
     */
    private GregorianCalendar startTime;


    /**
     * Period end date
     * <p>
     * The date on which the period ends. The date is the last day of the period.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-173<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.EndDate<br>
     */
    private GregorianCalendar endDate;


    /**
     * Period end time
     * <p>
     * The end time of the period.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-174<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.EndTime<br>
     */
    private GregorianCalendar endTime;

    public CriterionFulfillmentTimePeriod(GregorianCalendar startDate, GregorianCalendar startTime, GregorianCalendar endDate, GregorianCalendar endTime) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar startTime) {
        this.startTime = startTime;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar endTime) {
        this.endTime = endTime;
    }
}
