package eu.esens.espdvcd.builder;


public class ESPDBuilderFactory {
    
    public static ESPDBuilder createESPDBuilder() {
        return new ESPDBuilder();
    }
    
    public static ESPDBuilder createStrictECCompliantBuilder() {
        return new ESPDBuilder();
    }

}
