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
import org.oasis_open.docs.codelist.ns.genericode._1.ColumnSet;

/**
 * GenericCode that is used to load simple Generic Code files and provide a generic API that maps the values used
 * as IDs, which are essentially the codelist values with actual values that are presented either on a form or a
 * document.
 *
 * It is used as an abstract superclass of the {@link eu.esens.espdvcd.codelist.MultilingualCodeList}
 * MultilingualCodeList
 *
 * @version 1.0
 */
public class GenericCode {

    protected final JAXBElement<CodeListDocument> GC;
    protected final BiMap<String, CodeListRow> clBiMap;

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

    private BiMap<String, CodeListRow> createBiMap() {

        Map<String, CodeListRow> sourceMap = new LinkedHashMap<>();
        SimpleCodeList sgc = GC.getValue().getSimpleCodeList();
        ColumnSet cs = GC.getValue().getColumnSet();

        // Extract Ids and langs
        // Key = id,  value = lang
        final Map<String, String> idLangMap = new LinkedHashMap<>();
        cs.getColumnChoice().stream()
                .filter(o -> o instanceof Column)
                .map(o -> (Column) o)
                // discard description & code
                /* @TODO This filtering may have to been removed in future versions */
                .filter(c -> !c.getId().equals("code") && !c.getId().contains("description"))
                .forEach(c -> idLangMap.put(c.getId(), c.getData().getLang()));

        sgc.getRow().stream()
                .filter((Row r) -> r.getValue().stream() // Search in Rows
                .allMatch(c -> c.getColumnRef() instanceof Column)) // For Columns
                .forEach((Row r) -> {

                    // Extract Row data
                    // (1) extract code here
                    String id = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("code"))
                            .findAny().get().getSimpleValue().getValue();

                    final GenericCode.CodeListRow currentRow = new GenericCode.CodeListRow(id);
                    // (2) extract simple values here
                    r.getValue().stream()
                            // v stands for value here
                            .filter(v -> !((Column) v.getColumnRef()).getId().equals("code") && !((Column) v.getColumnRef()).getId().contains("description"))
                            /* @TODO have to find a way to avoid duplicate values here */
                            // nevertheless duplicates will be removed (because of Map)
                            .forEach(v -> {
                                // get column reference and use it in order to get the current value lang
                                String cRef = ((Column) v.getColumnRef()).getId();
                                String lang = idLangMap.get(cRef);
                                String data = v.getSimpleValue().getValue();
                                currentRow.getDataMap().put(lang, data);
                            });

                    sourceMap.put(id, currentRow);
                });

        BiMap<String, GenericCode.CodeListRow> biMap = ImmutableBiMap.copyOf(sourceMap);
        return biMap;
    }

    protected final BiMap<String, GenericCode.CodeListRow> getBiMap() {
        return clBiMap;
    }

    protected final String getValueForId(String id, String lang) {
        return lang != null ? (containsId(id) ? clBiMap.get(id).getDataMap().get(lang.toLowerCase()) : null)
                // en here is used because of v1 codelists
                : (containsId(id) ? clBiMap.get(id).getDataMap().get("en") : null);
    }
    
    protected final String getIdForData(String data, String lang) {
        return lang != null ? clBiMap.values().stream()
                .filter(row -> row.getDataMap().get(lang).equals(data))
                .findAny().get().getId()
                : clBiMap.values().stream()
                        // en here is used because of v1 codelists
                        .filter(row -> row.getDataMap().get("en").equals(data))
                        .findAny().get().getId();
    }

    protected final boolean containsId(String id) {
        return clBiMap.containsKey(id);
    }

    protected final boolean containsValue(String value, String lang) {
        return lang != null ? clBiMap.values().stream()
                .anyMatch(row -> row.getDataMap().get(lang).equals(value))
                : clBiMap.values().stream()
                        // en here is used because of v1 codelists
                        .anyMatch(row -> row.getDataMap().get("en").equals(value));
    }

    public static class CodeListRow {

        private final String id;
        // key = lang, value = name in that lang
        private final Map<String, String> dataMap;

        public CodeListRow(String id) {
            this.id = id;
            dataMap = new LinkedHashMap<>();
        }

        public String getId() {
            return id;
        }

        public Map<String, String> getDataMap() {
            return dataMap;
        }

    }

}
