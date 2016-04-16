package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.oasis_open.docs.codelist.ns.genericode._1.CodeListDocument;
import org.oasis_open.docs.codelist.ns.genericode._1.Column;
import org.oasis_open.docs.codelist.ns.genericode._1.Row;
import org.oasis_open.docs.codelist.ns.genericode._1.SimpleCodeList;

public class GenericCode {

    protected final JAXBElement<CodeListDocument> GC;
    protected final BiMap<String, String> clBiMap;

    protected GenericCode(String theCodelist) {

        XMLStreamReader xsr = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CodeListDocument.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory fac = XMLInputFactory.newFactory();
            xsr = fac.createXMLStreamReader(CodeListDocument.class.getResourceAsStream(theCodelist));

            GC = jaxbUnmarshaller.unmarshal(xsr, CodeListDocument.class);

            //create the BiMap that holds the default id <-> value mapping 
            clBiMap = createBiMap();

        } catch (JAXBException | XMLStreamException ex) {
            Logger.getLogger(GenericCode.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error loading codelist: " + theCodelist);
        } finally {
            if (xsr != null) {
                try {
                    xsr.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(GenericCode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    protected final String getRowSimpleValueWithCode(String id, String IdValue, String dataId) {

        SimpleCodeList sgc = GC.getValue().getSimpleCodeList();
        return sgc.getRow().stream()
                .filter(r -> r.getValue().stream() //Search in Rows
                        .filter(c -> (c.getColumnRef() instanceof Column // For Columns
                                && ((Column) c.getColumnRef()).getId().equals(id)) // With Id "code"
                                && c.getSimpleValue().getValue().equals(IdValue)) // And Short name GR
                        .findAny().isPresent()).findAny().orElseThrow(IllegalArgumentException::new)
                .getValue().stream()
                .filter(c -> (c.getColumnRef() instanceof Column // For Columns
                        && ((Column) c.getColumnRef()).getId().equals(dataId)))
                .findAny().orElseThrow(IllegalArgumentException::new).getSimpleValue().getValue();
    }

    protected final String getIdForData(String data) {
        //return getRowSimpleValueWithCode("name", data, "code");
        return clBiMap.inverse().get(data);
    }

    protected final String getValueForId(String id) {
        //return getRowSimpleValueWithCode("code", id, "name");
        return clBiMap.get(id);
    }

    private BiMap<String, String> createBiMap() {
        BiMap<String, String> biMap = HashBiMap.create();

        SimpleCodeList sgc = GC.getValue().getSimpleCodeList();
        sgc.getRow().stream()
                .filter(r -> r.getValue().stream() //Search in Rows
                        .allMatch(c -> c.getColumnRef() instanceof Column)) // For Columns
                .forEach((Row r) -> {
                    String id = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("code"))
                            .findAny().get().getSimpleValue().getValue();

                    String value = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name"))
                            .findAny().get().getSimpleValue().getValue();
                    biMap.put(id, value);
                });
        return biMap;
    }

    public final BiMap<String, String> getBiMap() {
        return Maps.unmodifiableBiMap(clBiMap);
    }

}
