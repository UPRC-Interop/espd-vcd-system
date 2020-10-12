/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
