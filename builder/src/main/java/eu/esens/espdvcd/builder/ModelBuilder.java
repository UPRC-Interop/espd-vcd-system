package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ModelBuilder {

    Logger logger = Logger.getLogger(ModelBuilder.class.getName());

    default EODetails createDefaultEODetails() {
        // Empty EODetails (with initialized lists)
        logger.log(Level.INFO, "Creating default Economic Operator Details");
        EODetails eod = new EODetails();
        eod.setContactingDetails(new ContactingDetails());
        eod.setPostalAddress(new PostalAddress());
        eod.setNaturalPersons(new ArrayList<>());

        NaturalPerson np = new NaturalPerson();
        np.setPostalAddress(new PostalAddress());
        np.setContactDetails(new ContactingDetails());

        eod.getNaturalPersons().add(np);
        return eod;
    }

    default CADetails createDefaultCADetails() {
        // Default initialization of the ESPDRequest and ESPDResponse Models.
        // Empty CADetails
        logger.log(Level.INFO, "Creating default Contracting Authority Details");
        CADetails cad = new CADetails();
        cad.setContactingDetails(new ContactingDetails());
        cad.setPostalAddress(new PostalAddress());
        return cad;
    }

    default ServiceProviderDetails createDefaultServiceProviderDetails() {
        // Empty ServiceProviderDetails
        logger.log(Level.INFO, "Creating default Service Provider Details");
        ServiceProviderDetails spd = new ServiceProviderDetails();
        // fill with default content
        spd.setWebsiteURI("N/A");
        spd.setEndpointID("N/A");
        spd.setID("N/A");
        spd.setName("e-SENS");
        spd.setPostalAddress(new PostalAddress());
        spd.getPostalAddress().setCountryCode("NO");
        return spd;
    }

    default List<SelectableCriterion> getEmptyCriteriaList() {
        // Empty Criteria List
        return new ArrayList<>();
    }

    default void applyCriteriaWorkaround(Criterion c) {

//        if (c.getTypeCode().equals("SELECTION.ECONOMIC_FINANCIAL_STANDING")
//                || c.getTypeCode().equals("DATA_ON_ECONOMIC_OPERATOR")) {
        if (c.getDescription().equals("")) {
            String oldName = c.getName();
            c.setDescription(oldName);
            // Since we have no name, we will add the Criteria type name as Criterion Name
            c.setName(CodelistsV1.CriteriaType.getValueForId(c.getTypeCode()) + " (No Name)");
//                System.out.println("Workaround for: "+c.getID() +" "+c.getDescription());
        }
//        }
    }

}
