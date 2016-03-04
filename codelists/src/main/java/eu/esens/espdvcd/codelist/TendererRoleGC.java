/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.codelist;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */

public class TendererRoleGC extends GenericCode {

    static {
        init("/gc/TendererRole-CodeList.gc");
    }
    
    public static String getTendererRoleName(String code) {
            return getDataValueForId(code);
    }
    
    public static String getTendererRoleCode(String name) {
        return getIdValueForData(name);
           
    }
}
