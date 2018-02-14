package eu.esens.espdvcd.builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;

/**
 *
 * @author Konstantinos Raptis
 */
public class DateTest {

    @Test
    public void testCreateECCompliantDate() {
        System.out.println("==== testCreateECCompliantDate ===");
        try {
            GregorianCalendar c = new GregorianCalendar();
            XMLGregorianCalendar xmlDate = createECCompliantDate(c);
            System.out.println(xmlDate);
            System.out.println("=== === Date");
            System.out.println(xmlDate.toGregorianCalendar().toZonedDateTime().toLocalDate().toString());
            System.out.println("=== === Time");
            System.out.println(xmlDate.toGregorianCalendar().toZonedDateTime().toLocalTime().toString());
        } catch (DatatypeConfigurationException e) {
            System.out.println("ERROR in DATES!");
        }
    }

    @Test
    public void testLocalDate() {
        System.out.println("==== testLocalDate ===");
        System.out.println(LocalDate.now());
    }
    
    @Test
    public void testLocalTime() {
        System.out.println("==== testLocalTime ===");
        System.out.println(LocalTime.now());
    }
    
    private XMLGregorianCalendar createECCompliantDate(GregorianCalendar c) throws DatatypeConfigurationException {
        // Creates the format according to the EC Application Requirement
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        c.get(Calendar.SECOND),
                        DatatypeConstants.FIELD_UNDEFINED,
                        DatatypeConstants.FIELD_UNDEFINED
                );
        return xmlDate;
    }

}
