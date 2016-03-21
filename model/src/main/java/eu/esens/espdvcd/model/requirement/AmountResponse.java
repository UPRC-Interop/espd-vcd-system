package eu.esens.espdvcd.model.requirement;

import java.io.Serializable;

/**
 * Created by Ulf Lotzmann on 21/03/2016.
 */
public class AmountResponse extends Response implements Serializable {

    private static final long serialVersionUID = -1738098273888763636L;
    
    private String amount;

      public AmountResponse() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
