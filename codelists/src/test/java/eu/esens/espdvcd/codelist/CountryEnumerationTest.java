/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.codelist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class CountryEnumerationTest {
    
    public CountryEnumerationTest() {
    }
    
    @Before
    public void setUp() {
    }
     
    @Test 
        public void testGC() {
       
            Assert.assertEquals("GREECE", CountryCodeGC.getCountry("GR"));
            Assert.assertEquals("GR", CountryCodeGC.getISOCode("GREECE"));
            
            Assert.assertEquals("MT", TendererRoleGC.getTendererRoleCode("Main tenderer"));
            Assert.assertEquals("Main tenderer", TendererRoleGC.getTendererRoleName("MT"));
            
        }
    
}
