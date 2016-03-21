package eu.esens.espdvcd.model.requirement;

import java.util.List;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class PeriodDeclarationFulfillment {


    /**
     * Criterion fulfillment amount
     * <p>
     * Declared amount that fulfills this criterion.
     * <p>
     * Data type: Amount<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-162<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Amount<br>
     */
    private String amount;

    /**
     * Criterion fulfillment quantity
     * <p>
     * UOM should be stated by using recommendation 20 v10 Declared quantity that fulfills this criterion.
     * <p>
     * Data type: Quantity<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-163<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Quantity<br>
     */
    private String quantity;

    /**
     * Criterion fulfillment percenatge
     * <p>
     * Declared percentage that fulfills this criterion.
     * <p>
     * Data type: Percentage<br>
     * Cardinality: 0..1<br>
     * InfReqID:  tir92-164<br>
     * BusReqID: tbr92-018, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Percent<br>
     */
    private String percenatge;

    /**
     * Criterion fulfillment description
     * <p>
     * Textual description of the fulfilment of this criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-165<br>
     * BusReqID: tbr92-018, tbr92-005<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Description<br>
     */
    private String description;

    /**
     * Criterion fulfillment date
     * <p>
     * Declared date where  this criterion was fulfilled.
     * <p>
     * Data type: Date<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-166<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Date<br>
     */
    private GregorianCalendar date;

    /**
     * Criterion fulfillment event type code
     * <p>
     * Event declared to fulfil this criterion, expressed as a code.
     * <p>
     * Data type: Code<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-167<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.DescriptionCode<br>
     */
    private String eventTypeCode;


    /**
     * Criterion fulfillment event
     * <p>
     * Description of the event declared to fulfil this criterion.
     * <p>
     * Data type: <br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-168<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.Description<br>
     */
    private String event;

    /**
     * Criterion fulfillment time period
     * <p>
     *
     * <p>
     * Data type: CriterionFulfillmentTimePeriod<br>
     * Cardinality: 0..1<br>
     * InfReqID: <br>
     * BusReqID: <br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period<br>
     */
    private CriterionFulfillmentTimePeriod timePeriod;

    /**
     * Criterion fulfillment location
     * <p>
     * Textual description of the location where this criterion is fulfilled.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..n<br>
     * InfReqID: tir92-169<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Period.Description<br>
     */
    private List<String> locations;


    /**
     * Criterion fulfillment affected party
     * <p>
     * Textual description of the affected party by this criterion.
     * <p>
     * Data type: Text<br>
     * Cardinality: 0..n<br>
     * InfReqID: tir92-170<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.Description.RelatedParty.PartyName<br>
     */
    private List<String> affectedParties;


    public PeriodDeclarationFulfillment(String amount,
                                        String quantity,
                                        String percenatge,
                                        String description,
                                        GregorianCalendar date,
                                        String eventTypeCode,
                                        String event,
                                        CriterionFulfillmentTimePeriod timePeriod) {
        this.amount = amount;
        this.quantity = quantity;
        this.percenatge = percenatge;
        this.description = description;
        this.date = date;
        this.eventTypeCode = eventTypeCode;
        this.event = event;
        this.timePeriod = timePeriod;
    }

    public PeriodDeclarationFulfillment(String amount,
                                        String quantity,
                                        String percenatge,
                                        String description,
                                        GregorianCalendar date,
                                        String eventTypeCode,
                                        String event,
                                        CriterionFulfillmentTimePeriod timePeriod,
                                        List<String> locations,
                                        List<String> affectedParties) {
        this.amount = amount;
        this.quantity = quantity;
        this.percenatge = percenatge;
        this.description = description;
        this.date = date;
        this.eventTypeCode = eventTypeCode;
        this.event = event;
        this.timePeriod = timePeriod;
        this.locations = locations;
        this.affectedParties = affectedParties;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPercenatge() {
        return percenatge;
    }

    public void setPercenatge(String percenatge) {
        this.percenatge = percenatge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public CriterionFulfillmentTimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(CriterionFulfillmentTimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<String> getLocations() {
        if (locations == null) {
            locations = new ArrayList<>();
        }
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getAffectedParties() {
        if (affectedParties == null) {
            affectedParties = new ArrayList<>();
        }
        return affectedParties;
    }

    public void setAffectedParties(List<String> affectedParties) {
        this.affectedParties = affectedParties;
    }

}
