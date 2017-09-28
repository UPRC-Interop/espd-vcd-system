package eu.esens.espdvcd.codelist;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.LinkedHashMap;
import java.util.Map;
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

/**
 * GenericCode that is used to load simple Generic Code files and provide a generic API that maps the values used
 * as IDs, which are essentially the codelist values with actual values that are presented either on a form or a
 * document.
 *
 * It is used as an abstract superclass of the {@link eu.esens.espdvcd.codelist.CodeListsV1Impl} CodeListsV1Impl
 * and {@link eu.esens.espdvcd.codelist.CodeListsV2Impl} CodeListsV2Impl
 *
 * @version 1.0
 */
public class GenericCode {

    protected final JAXBElement<CodeListDocument> GC;
    protected BiMap<String, String> clBiMap;
    protected final BiMap<String, CodelistRow> clBiMapV2;
    
    protected GenericCode(String theCodelist) {

        XMLStreamReader xsr = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CodeListDocument.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory fac = XMLInputFactory.newFactory();
            xsr = fac.createXMLStreamReader(CodeListDocument.class.getResourceAsStream(theCodelist));

            GC = jaxbUnmarshaller.unmarshal(xsr, CodeListDocument.class);

            //create the BiMap that holds the default id <-> value mapping 
            // clBiMap = createBiMap();
            clBiMapV2 = createBiMapV2();

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

    protected final boolean containsId(String id) {
        return clBiMap.containsKey(id);
    }

    protected final boolean containsValue(String value) {
        return clBiMap.containsValue(value);
    }

    protected final BiMap<String, String> getBiMap() {
        return clBiMap;
    }

    private BiMap<String, String> createBiMap() {
        Map<String, String> sourceMap = new LinkedHashMap<>();

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
                    sourceMap.put(id, value);
                });
        BiMap<String, String> biMap = ImmutableBiMap.copyOf(sourceMap);
        return biMap;
    }

    private BiMap<String, CodelistRow> createBiMapV2() {

        Map<String, CodelistRow> sourceMap = new LinkedHashMap<>();
        SimpleCodeList sgc = GC.getValue().getSimpleCodeList();

        sgc.getRow().stream()
                .filter((Row r) -> r.getValue().stream() // Search in Rows
                .allMatch(c -> c.getColumnRef() instanceof Column)) // For Columns
                .forEach((Row r) -> {

                    // Extract Row data
                    String id = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("code"))
                            .findAny().get().getSimpleValue().getValue();

                    String name = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-en"))
                            .findAny().get().getSimpleValue().getValue();
                    
                    String descEn = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("description-en"))
                            .findAny().get().getSimpleValue().getValue();

                    String descEs = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("description-es"))
                            .findAny().get().getSimpleValue().getValue();

                    String descFr = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("description-fr"))
                            .findAny().get().getSimpleValue().getValue();

                    String descEl = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("description-el"))
                            .findAny().get().getSimpleValue().getValue();
                    
                    GenericCode.CodelistRow currentRow = new GenericCode.CodelistRow(id, name);
                    
                    currentRow.getDescriptionMap().put("en", descEn);
                    currentRow.getDescriptionMap().put("es", descEs);
                    currentRow.getDescriptionMap().put("fr", descFr);
                    currentRow.getDescriptionMap().put("el", descEl);

                    sourceMap.put(id, currentRow);
                });

        BiMap<String, CodelistRow> biMap = ImmutableBiMap.copyOf(sourceMap);
        return biMap;
    }
            
    protected final String getDescriptionForIdV2(String id, String lang) {
        return clBiMapV2.get(id).getDescriptionMap().get(lang);
    }
    
    protected final String getNameForIdV2(String id) {
        return clBiMapV2.get(id).getName();
    }
    
    protected final String getIdForDataV2(String data) {
        return clBiMapV2.values().stream()
                .filter(row -> row.getName().equals(data))
                .findAny().get().getId();
    }
    
    protected final boolean containsIdV2(String id) {
        return clBiMapV2.containsKey(id);
    }
    
    protected final boolean containsValueV2(String value) {
        return clBiMapV2.values().stream()
                .anyMatch(row -> row.getName().equals(value));
    }
    
    private static class CodelistRow {
        
        private String id;
        private String name;
        private Map<String, String> descriptionMap;

        public CodelistRow(String id) {
            this(id, "");
        }

        public CodelistRow(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getDescriptionMap() {
            if (descriptionMap == null) {
                descriptionMap = new LinkedHashMap<>();
            }
            return descriptionMap;
        }

        public void setDescriptionMap(Map<String, String> descriptionMap) {
            this.descriptionMap = descriptionMap;
        }
        
    }
    
}
