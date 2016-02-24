/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.codelist;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.oasis_open.docs.codelist.ns.genericode._1.CodeListDocument;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */

public class TendererRoleGC extends GenericCode {

    static {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CodeListDocument.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GC = (JAXBElement<CodeListDocument>) jaxbUnmarshaller.unmarshal(CodeListDocument.class.getResourceAsStream("/gc/TendererRole-CodeList.gc"));

        } catch (JAXBException ex) {
            Logger.getLogger(TendererRoleGC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getTendererRoleName(String code) {
            return getDataValueForId(code);
    }
    
    public static String getTendererRoleCode(String name) {
        return getIdValueForData(name);
           
    }
}
