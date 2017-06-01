package eu.esens.espdvcd.designer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.LegislationType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;

public class ECertisForm extends VerticalLayout {

    private final Panel panel = new Panel();
    private final VerticalLayout panelContent = new VerticalLayout();
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label jurisdictionLevelCode = new Label();
    private final Label article = new Label();
    private final Label URI = new Label();
    private final Label legislationDescription = new Label();
    private final Criterion c;
    private final EODetails eoDetails;

    public ECertisForm(Criterion c, EODetails eo) {
        this.c = c;
        this.eoDetails = eo;
        
        setMargin(true);
        setStyleName("legislationReferenceForm-layout");
        setWidth("100%");
        panel.setWidth("100%");
        addComponent(panel);

        panel.setStyleName("legislationReferenceForm-panel");
        panel.setContent(panelContent);
        panel.setCaption("eCertis National Mapping");
        panel.setIcon(FontAwesome.CHEVRON_RIGHT);
        panelContent.setMargin(true);
        panelContent.setVisible(false);
        this.addLayoutClickListener(this::onPanelClick);      
      
    }
    private void fetchData() {
            
        ECertisCriteriaExtractor ce = new ECertisCriteriaExtractor();

          try {
            List<CriterionType> cList = ce.getNationalCriterionMapping(c.getID(), eoDetails.getPostalAddress().getCountryCode().toLowerCase());
            if (!cList.isEmpty()) {
                CriterionType ct = cList.get(0);

                JAXB.marshal(ct, System.out);
                title.setCaption("National Criterion Title");
                title.setValue(ct.getName().getValue());
                panelContent.addComponent(title);

                if (ct.getDescription() != null) {
                    description.setCaption("Description");
                    description.setValue(ct.getDescription().getValue());
                    panelContent.addComponent(description);
                }

                if (!ct.getLegislationReference().isEmpty()) {
 
                    LegislationType lt = ct.getLegislationReference().get(0);

                    jurisdictionLevelCode.setCaption("Jurisdiction Level");
                    jurisdictionLevelCode.setValue(lt.getJurisdictionLevelCode().getValue());
                    panelContent.addComponent(jurisdictionLevelCode);
                  
                    legislationDescription.setCaption("Legislation Description");
                    legislationDescription.setValue(lt.getDescription().getValue());
                    panelContent.addComponent(legislationDescription);
                    
                    article.setCaption("Article");
                    article.setValue(lt.getArticle().getValue());
                    panelContent.addComponent(article);
                    
                    URI.setCaption("Legislation Reference URI");
                    URI.setValue(lt.getURI().getValue());
                    panelContent.addComponent(URI);
                }

                //article.setValue(ct.getLegislationReference().get(0).getArticle().getValue());
                //panelContent.addComponent(article);
                //URI.setValue(ct.getLegislationReference().get(0).getURI().getValue());
                //panelContent.addComponent(URI);
            } else {
                title.setCaption("eCertis Data not available");
                title.setValue("No information could be retrieved for country "+eoDetails.getPostalAddress().getCountryCode());
                panelContent.addComponent(title);
            }

        } catch (RetrieverException ex) {
            Logger.getLogger(ECertisForm.class.getName()).log(Level.SEVERE, null, ex);
            title.setCaption("eCertis Data not available");
            title.setValue(ex.getMessage());
            panelContent.addComponent(title);


        }
    }
    
    private void onPanelClick(LayoutEvents.LayoutClickEvent event) {
        if (panelContent.isVisible()) {
            panelContent.setVisible(false);            
            panelContent.removeAllComponents();
        } else {
            fetchData();
            panelContent.setIcon(FontAwesome.CHEVRON_DOWN);
            panelContent.setVisible(true);
        }
    }
}
