package eu.esens.espdvcd.codelist;

import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    protected final Map<String, Map<String, String>> langMap;

    protected GenericCode(String theCodelist) {

        XMLStreamReader xsr = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CodeListDocument.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory fac = XMLInputFactory.newFactory();
            xsr = fac.createXMLStreamReader(CodeListDocument.class.getResourceAsStream(theCodelist));

            GC = jaxbUnmarshaller.unmarshal(xsr, CodeListDocument.class);

            // create the lang Map, which holds the BiMap that holds the default id <-> value mapping 
            langMap = createLangMap();

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

    private Map<String, Map<String, String>> createLangMap() {

        final Map<String, Map<String, String>> tempLangMap = new LinkedHashMap<>();
        SimpleCodeList sgc = GC.getValue().getSimpleCodeList();
        ColumnSet cs = GC.getValue().getColumnSet();

        // Extract Ids and langs
        // Key = id,  value = lang
        final Map<String, String> idLangMap = new LinkedHashMap<>();
        cs.getColumnChoice().stream() // Search in ColumnSet
                .filter(o -> o instanceof Column) // For Columns
                .map(o -> (Column) o)
                // discard description & code
                /* @TODO This filtering may have to be removed in future versions,
                since we are not using the description column, we choose not to handle it*/
                .filter(c -> !c.getId().equals("code") && !c.getId().contains("description"))
                .forEach(c -> idLangMap.put(c.getId(), c.getData().getLang()));

        sgc.getRow().stream()
                .filter((Row r) -> r.getValue().stream() // Search in Rows
                .allMatch(c -> c.getColumnRef() instanceof Column)) // For Columns
                .forEach((Row r) -> {

                    // Extract Row data
                    // (1) extract code here
                    String code = r.getValue().stream()
                            .filter(c -> ((Column) c.getColumnRef()).getId().equals("code"))
                            .findAny().get().getSimpleValue().getValue();

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

                                // if language not exist, create new map for that particular language
                                if (!tempLangMap.containsKey(lang)) {
                                    // key = code, value =  data
                                    Map<String, String> sourceMap = new LinkedHashMap<>();
                                    sourceMap.put(code, data);
                                    // put map for that language to temp lang map
                                    tempLangMap.put(lang, sourceMap);
                                } // there is a map for that particular language
                                // get that map and put <code, data> there
                                else {
                                    tempLangMap.get(lang).put(code, data);
                                }

                            });

                });
        
//        tempLangMap.forEach((lang, biMap) -> {
//            System.out.println("language: " + lang);
//            biMap.forEach((code, data)-> System.out.println("Code: " + code + ", Data: " + data));
//            System.out.println();
//        });
        
        return tempLangMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> ImmutableMap.copyOf(e.getValue())));
    }

    protected final Map<String, String> getDataMap(String lang) {
        return langMap.get(lang);
    }

    protected final String getValueForId(String id, String lang) {
        String value = null;

        if (langMap.containsKey(lang)) {
            value = langMap.get(lang).get(id);
        }

        return value;
    }

    protected final boolean containsId(String id, String lang) {
        boolean result = false;

        if (langMap.containsKey(lang)) {
            // id = code of row in gc
            result = langMap.get(lang).containsKey(id);
        }

        return result;
    }

    protected final boolean containsValue(String value, String lang) {
        boolean result = false;

        if (langMap.containsKey(lang)) {
            langMap.get(lang).containsValue(value);
        }

        return result;
    }
    
    protected final Set<String> getAllLangs() {
        return langMap.keySet();
    }
    
}
