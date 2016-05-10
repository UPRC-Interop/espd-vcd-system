package eu.esens.espdvcd.builder;

/**
 *The ESPDBuilderFactory is a Factory class that provides static methods in creating
 * ESPD and VCD Builder Instances. 
 * 
 * @since 1.0
 * 
 */
public class ESPDBuilderFactory {
   
    /**
     * Factory method that creates a simple ESPDBuilder that provides no validation checks. The criteria
     * contained can all be selected and deselected. 
     * 
     * @return a simple ESPDBuilder that provides no validation checks.
     */
    public static ESPDBuilder createESPDBuilder() {
        return new ESPDBuilder();
    }
    
    /**
     * Factory method that creates an ESPD Builder that follows the strict rules
     * of the ESPD Standard form. Specific exclusion criteria are compulsory and 
     * cannot be deselected.
     * 
     * @return a ESPBBuilder instance that contains validation checks for schema,
     * criteria element existence, required by the official ESPD Document.
     */
    public static ESPDBuilder createStrictECCompliantBuilder() {
        return new ESPDBuilder();
    }

}
