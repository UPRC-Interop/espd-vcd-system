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
    protected final BiMap<String, CodelistRow> clBiMap;
    
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

    private BiMap<String, CodelistRow> createBiMap() {

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

                    String nameENG = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-eng"))
                            .findAny().get().getSimpleValue().getValue();

                    String nameBUL = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-bul"))
                            .findAny().get().getSimpleValue().getValue();

                    String nameSPA = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-spa"))
                            .findAny().get().getSimpleValue().getValue();

                    String nameCES = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-ces"))
                            .findAny().get().getSimpleValue().getValue();
                    
                    String nameDAN = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-dan"))
                            .findAny().get().getSimpleValue().getValue();
                    
                    String nameDEU = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-deu"))
                            .findAny().get().getSimpleValue().getValue();    
                        
                    String nameEST = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-est"))
                            .findAny().get().getSimpleValue().getValue();    
                            
                    String nameELL = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-ell"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameFRA = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-fra"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameGLE = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-gle"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameHRV = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-hrv"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameITA = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-ita"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameLAV = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-lav"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameLIT = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-lit"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameHUN = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-hun"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameMLT = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-mlt"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameNLD = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-nld"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String namePOL = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-pol"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String namePOR = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-por"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameRON = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-ron"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameSLK = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-slk"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameSLV = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-slv"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameFIN = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-fin"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    String nameSWE = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("name-swe"))
                            .findAny().get().getSimpleValue().getValue();    
                    
                    GenericCode.CodelistRow currentRow = new GenericCode.CodelistRow(id);
                    
                    currentRow.getNameMap().put("eng", nameENG);
                    currentRow.getNameMap().put("bul", nameBUL);
                    currentRow.getNameMap().put("spa", nameSPA);
                    currentRow.getNameMap().put("ces", nameCES);
                    currentRow.getNameMap().put("dan", nameDAN);
                    currentRow.getNameMap().put("deu", nameDEU);
                    currentRow.getNameMap().put("est", nameEST);
                    currentRow.getNameMap().put("ell", nameELL);
                    currentRow.getNameMap().put("fra", nameFRA);
                    currentRow.getNameMap().put("gle", nameGLE);
                    currentRow.getNameMap().put("hrv", nameHRV);
                    currentRow.getNameMap().put("ita", nameITA);
                    currentRow.getNameMap().put("lav", nameLAV);
                    currentRow.getNameMap().put("lit", nameLIT);
                    currentRow.getNameMap().put("hun", nameHUN);
                    currentRow.getNameMap().put("mlt", nameMLT);
                    currentRow.getNameMap().put("nld", nameNLD);
                    currentRow.getNameMap().put("pol", namePOL);
                    currentRow.getNameMap().put("por", namePOR);
                    currentRow.getNameMap().put("ron", nameRON);
                    currentRow.getNameMap().put("slk", nameSLK);
                    currentRow.getNameMap().put("slv", nameSLV);
                    currentRow.getNameMap().put("fin", nameFIN);
                    currentRow.getNameMap().put("swe", nameSWE);
                    
                    sourceMap.put(id, currentRow);
                });

        BiMap<String, CodelistRow> biMap = ImmutableBiMap.copyOf(sourceMap);
        return biMap;
    }

    protected final BiMap<String, GenericCode.CodelistRow> getBiMap() {
        return clBiMap;
    }
        
    protected final String getDataForId(String id, String lang) {
        return clBiMap.get(id).getNameMap().get(lang.toLowerCase());
    }
    
    protected final String getIdForData(String data, String lang) {
        return clBiMap.values().stream()
                .filter(row -> row.getNameMap().get(lang).equals(data))
                .findAny().get().getId();
    }
    
    protected final boolean containsId(String id) {
        return clBiMap.containsKey(id);
    }
    
    protected final boolean containsValue(String value, String lang) {
        return clBiMap.values().stream()
                .anyMatch(row -> row.getNameMap().get(lang).equals(value));
    }
    
    public static class CodelistRow {
        
        private String id;
        private Map<String, String> nameMap;

        public CodelistRow(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }

        public Map<String, String> getNameMap() {
            if (nameMap == null) {
                nameMap = new LinkedHashMap<>();
            }
            return nameMap;
        }
        
    }
    
}
