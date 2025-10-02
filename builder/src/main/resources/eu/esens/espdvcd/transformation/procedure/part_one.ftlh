<#--
 Copyright 2016-2020 University of Piraeus Research Center

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<#import '../propertyreader/property_reader.ftlh' as pr>
<#import '../html/section.ftlh' as s>
<#import '../html/chapter.ftlh' as c>

<#macro partOne model props>
    <div class="section">
        <h3>${pr.getESPDProperty("espd.part1.header", props)}</h3>
        <@partOneChapter model props "espd.part1.info.pub"/>
        <@partOneChapter model props "espd.part1.contact.details.ca"/>
        <@partOneChapter model props "espd.part1.info.procurement.proc"/>
    </div>
</#macro>

<#macro partOneChapter model props chapter>
    <div class="chapter">
        <#if chapter == "espd.part1.info.pub">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part1.info.pub", props)}</div>
            <div class="chapter-content">
                <@additionalDocumentReference model props/>
            </div>
        <#elseif chapter == "espd.part1.contact.details.ca">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part1.contact.details.ca", props)}</div>
            <div class="chapter-content">
                <@contractingParty model props/>
            </div>
        <#elseif chapter == "espd.part1.info.procurement.proc">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part1.info.procurement.proc", props)}</div>
            <div class="chapter-content">
                <@infoProcurementProc model props/>
            </div>
        </#if>
    </div>
</#macro>

<#macro additionalDocumentReference model props>
    <#assign filledAlert = pr.getESPDProperty("espd.part1.to.be.filled.alert", props)/>
    <#assign tedRecId = pr.getESPDProperty("espd.part1.ted.reception.id", props)/>
    <#assign ojsLabel = pr.getESPDProperty("espd.part1.ojs.label", props)/>
    <#assign ojsUrl = pr.getESPDProperty("espd.part1.ojs.url", props)/>
    <#assign officialJournalAlert = pr.getESPDProperty("espd.part1.official.journal.alert", props)/>
    <#assign officialJournalAlert2 = pr.getESPDProperty("espd.part1.official.journal.alert2", props)/>

    <p>${filledAlert}</p>
    <br>
    <@s.sectionLabel tedRecId model.getCADetails().getReceivedNoticeNumber()/>
    <@s.sectionLabel ojsLabel model.getCADetails().getProcurementPublicationNumber()/>
    <br>
    <p>${officialJournalAlert}</p>
    <br>
    <@s.sectionLabelForUrl ojsUrl model.getCADetails().getProcurementPublicationURI()/>
    <br>
    <p>${officialJournalAlert2}</p>
    <br>
</#macro>

<#macro contractingParty model props >
    <#assign officialName = model.getCADetails().getCAOfficialName()!""/>
    <#assign vatId = model.getCADetails().getID()!""/>
    <#assign websiteURI = model.getCADetails().getWebSiteURI()!""/>
    <#assign city = model.getCADetails().getPostalAddress().getCity()!""/>
    <#assign address = model.getCADetails().getPostalAddress().getAddressLine1()!""/>
    <#assign postcode = model.getCADetails().getPostalAddress().getPostCode()!""/>
    <#if (model.getCADetails().getContactingDetails())??>
        <#assign contactPointName = model.getCADetails().getContactingDetails().getContactPointName()!""/>
        <#assign tel = model.getCADetails().getContactingDetails().getTelephoneNumber()!""/>
        <#assign fax = model.getCADetails().getContactingDetails().getFaxNumber()!""/>
        <#assign email = model.getCADetails().getContactingDetails().getEmailAddress()!""/>
    <#else>
        <#assign contactPointName = ""/>
        <#assign tel = ""/>
        <#assign fax = ""/>
        <#assign email = ""/>
    </#if>
    <#assign countryCode = model.getCADetails().getPostalAddress().getCountryCode()!""/>

    <@s.sectionLabelForValueOfNode officialName "espd.part1.name" props/>
    <@s.sectionLabelForValueOfNode vatId "espd.part2.vat" props/>
    <@s.sectionLabelForUrlValueOfNode websiteURI "espd.part1.website" props/>
    <@s.sectionLabelForValueOfNode city "espd.part1.city" props/>
    <@s.sectionLabelForValueOfNode address "espd.part1.street.and.num" props/>
    <@s.sectionLabelForValueOfNode postcode "espd.part1.postcode" props/>
    <@s.sectionLabelForValueOfNode contactPointName "espd.part1.contact.person" props/>
    <@s.sectionLabelForValueOfNode tel "espd.part1.telephone" props/>
    <@s.sectionLabelForValueOfNode fax "espd.part1.fax" props/>
    <@s.sectionLabelForMailValueOfNode email "espd.part1.email" props/>
    <@s.sectionLabelForValueOfNode countryCode "espd.part1.country" props/>
</#macro>

<#macro infoProcurementProc model props>
    <#assign procurerName = pr.getESPDProperty("espd.part1.procurer.name", props)/>
    <#assign shortDesc = pr.getESPDProperty("espd.part1.title.or.short.desc", props)/>
    <#assign fileRefCa = pr.getESPDProperty("espd.part1.file.ref.ca", props)/>

    <@s.sectionLabel procurerName model.getCADetails().getProcurementProcedureTitle()/>
    <@s.sectionLabel shortDesc model.getCADetails().getProcurementProcedureDesc()/>
    <@s.sectionLabel fileRefCa model.getCADetails().getProcurementProcedureFileReferenceNo()/>
</#macro>
