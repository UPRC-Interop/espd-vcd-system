/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.esens.espdvcd.builder;


public class ESPDBuilderFactory {
    
    public static ESPDBuilder createESPDBuilder() {
        return new ESPDBuilder();
    }
    
    public static ESPDBuilder createStrictECCompliantBuilder() {
        return new ESPDBuilder();
    }

}
