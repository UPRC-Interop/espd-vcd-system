/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javax.xml.bind.JAXBElement;
import org.oasis_open.docs.codelist.ns.genericode._1.CodeListDocument;
import org.oasis_open.docs.codelist.ns.genericode._1.Column;
import org.oasis_open.docs.codelist.ns.genericode._1.SimpleCodeList;

/**
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public abstract class GenericCode {

    protected static JAXBElement<CodeListDocument> GC;
    
    protected static final String getRowSimpleValueWithCode(String id, String IdValue, String dataId) {
        
          SimpleCodeList sgc = GC.getValue().getSimpleCodeList(); 
            return sgc.getRow().parallelStream()
                    .filter(r -> r.getValue().parallelStream() //Search in Rows
                            .filter(c -> ( c.getColumnRef() instanceof Column  // For Columns
                                          && ((Column) c.getColumnRef()).getId().equals(id)) // With Id "code"
                                          && c.getSimpleValue().getValue().equals(IdValue)) // And Short name GR
                            .findAny().isPresent()).findAny().orElseThrow(IllegalArgumentException::new)
                    .getValue().parallelStream()
                    .filter(c -> ( c.getColumnRef() instanceof Column  // For Columns
                                   && ((Column) c.getColumnRef()).getId().equals(dataId)))
                    .findAny().orElseThrow(IllegalArgumentException::new).getSimpleValue().getValue();
    }
    
    protected static final String getIdValueForData(String data) {
         return getRowSimpleValueWithCode("name", data, "code");
    }
    
    protected static final String getDataValueForId(String id) {
        return getRowSimpleValueWithCode("code", id, "name");
    }
    
    protected static final BiMap<String,String> getIdBiMap() {
        
        BiMap<String,String> idBiMap = HashBiMap.create();
        
        
        return idBiMap;
    }
}
