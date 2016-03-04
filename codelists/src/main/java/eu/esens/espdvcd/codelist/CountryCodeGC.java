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

public class CountryCodeGC extends GenericCode {

    static {
        init("/gc/CountryIdentificationCode-2.1.gc");
    }
    
    public static String getCountry(String twoLetter) {
            return getDataValueForId(twoLetter);
    }
    
    public static String getISOCode(String country) {
        return getIdValueForData(country);           
    }
    

}
