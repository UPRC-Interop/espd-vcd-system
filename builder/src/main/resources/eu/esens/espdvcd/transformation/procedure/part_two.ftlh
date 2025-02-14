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

<#import '../html/chapter.ftlh' as c>
<#import '../base/tender_criterion.ftlh' as tc>
<#import '../html/section.ftlh' as s />
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro partTwo model modelType enProps props version>
    <div class="section">
        <h3>${pr.getESPDProperty("espd.part2.header", props)}</h3>
        <@partTwoChapter model modelType enProps props version/>
    </div>
</#macro>

<#macro partTwoChapter model modelType enProps props version>
<#--  Α  -->
    <div class="chapter">
        <div class="chapter-headline">
            ${pr.getESPDProperty("espd.part2.info.eo", props)}
        </div>
        <div class="chapter-content">
            <@sectionForEO model modelType enProps props version/>
        </div>
    </div>
<#--  Β  -->
    <div class="chapter">
        <@powerOfAttorney model modelType props/>
    </div>
<#--  Γ  -->
    <div class="chapter">
        <div class="chapter-headline">
            ${pr.getESPDProperty("espd.part2.info.reliance", props)}
        </div>
        <div class="chapter-content">
            <@tenderingCriteriaByCriterionTypeCode model modelType 'CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES' enProps props version/>
        </div>
    </div>
<#--  Δ  -->
    <div class="chapter">
        <div class="chapter-headline">
            ${pr.getESPDProperty("espd.part2.information.subcontractors", props)}
        </div>
        <div class="chapter-content">
            <@tenderingCriteriaByCriterionTypeCode model modelType 'CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES' enProps props version/>
        </div>
    </div>
</#macro>

<#macro sectionForEO model modelType enProps props version>
    <#if modelType == "ESPD_RESPONSE">
        <#assign name = model.getEODetails().getName()!""/>
        <#assign address = model.getEODetails().getPostalAddress().getAddressLine1()!""/>
        <#assign postcode = model.getEODetails().getPostalAddress().getPostCode()!""/>
        <#assign city = model.getEODetails().getPostalAddress().getCity()!""/>
        <#assign countryCode = model.getEODetails().getPostalAddress().getCountryCode()!""/>
        <#assign contactPointName = model.getEODetails().getContactingDetails().getContactPointName()!""/>
        <#assign email = model.getEODetails().getContactingDetails().getEmailAddress()!""/>
        <#assign tel = model.getEODetails().getContactingDetails().getTelephoneNumber()!""/>
        <#assign fax = model.getEODetails().getContactingDetails().getFaxNumber()!""/>
        <#assign id = model.getEODetails().getID()!""/>
        <#assign websiteURI = model.getEODetails().getWebSiteURI()!""/>
        <#assign lots = model.getEODetails().getProcurementProjectLot()!""/>

        <@s.sectionLabelForValueOfNode name "espd.part2.name" props/>
        <@s.sectionLabelForValueOfNode address "espd.part1.street.and.num" props/>
        <@s.sectionLabelForValueOfNode postcode "espd.part1.postcode" props/>
        <@s.sectionLabelForValueOfNode city "espd.part1.city" props/>
        <@s.sectionLabelForValueOfNode countryCode "espd.part1.country" props/>
        <@s.sectionLabelForValueOfNode contactPointName "espd.part1.contact.person" props/>
        <@s.sectionLabelForMailValueOfNode email "espd.part1.email" props/>
        <@s.sectionLabelForValueOfNode tel "espd.part1.telephone" props/>
        <@s.sectionLabelForValueOfNode fax "espd.part1.fax" props/>
        <@s.sectionLabelForValueOfNode id "espd.part2.vat" props/>
        <@s.sectionLabelForUrlValueOfNode websiteURI "espd.part1.website" props/>
        <@s.sectionLabelForValueOfNode lots "espd.crit.lots.indication" props/>

        <div class="singleQuestion">
            <label id="eoSizedLabel">${pr.getESPDProperty("espd.part2.is.eo.sized",props)}<@smeIndicator model props/></label>
        </div>
    <#elseif modelType == "ESPD_REQUEST">
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.name", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.street.and.num", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.postcode", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.city", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.country", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.contact.person", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.email", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.telephone", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.fax", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.vat", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.website", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.crit.lots.indication", props)}</span>
        </div>
        <div class="singleQuestion">
            <#assign yes = pr.getESPDProperty("espd.gui.yes", props)/>
            <#assign no = pr.getESPDProperty("espd.gui.no", props)/>
            <label id="eoSizedLabel">${pr.getESPDProperty("espd.part2.is.eo.sized",props)}<span style="margin-left: 5px">${yes} / ${no}</span></label>
        </div>
    </#if>

    <@tenderingCriteriaByCriterionTypeCode model modelType "CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP" enProps props version/>
    <@tenderingCriteriaByCriterionTypeCode model modelType "CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST" enProps props version/>
    <@tenderingCriteriaByCriterionTypeCode model modelType "CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS" enProps props version/>
    <@tenderingCriteriaByCriterionTypeCode model modelType "CRITERION.OTHER.EO_DATA.LOTS_TENDERED" enProps props version/>
</#macro>

<#macro smeIndicator model props>
    <span class="icon-label"><i class="fa fa-dot-circle-o" aria-hidden="true"></i>${yesNo(isSME(model),props)}</span>
</#macro>

<#function isSME model>
    <#if model.getEODetails().isSmeIndicator()>
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>

<#function yesNo value props>
    <#switch value>
        <#case true>
            <#assign yes = pr.getESPDProperty("espd.gui.yes", props)/>
            <#return '${yes}'>
        <#case false>
            <#assign no = pr.getESPDProperty("espd.gui.no", props)/>
            <#return '${no}'>
    </#switch>
</#function>

<#macro powerOfAttorney model modelType props>
    <#if modelType == "ESPD_RESPONSE" && model.getEODetails().getNaturalPersons()??>
        <#assign naturalPersonsCounter = 0/>
        <#list model.getEODetails().getNaturalPersons() as person>
            <#assign naturalPersonsCounter = naturalPersonsCounter + 1/>
            <@personalInfoResponse person props naturalPersonsCounter/>
        </#list>
    <#elseif modelType == "ESPD_REQUEST">
        <@personalInfoRequest props 1/>
    </#if>
</#macro>

<#macro personalInfoResponse person props naturalPersonsCounter>
    <div class="chapter-headline">
        ${pr.getESPDProperty("espd.part2.info.respresent", props)} #${naturalPersonsCounter}
    </div>
    <div class="chapter-content">
        <@s.sectionLabelForValueOfNode person.getFirstName()!"" "espd.part2.first.name" props/>
        <@s.sectionLabelForValueOfNode person.getFamilyName()!"" "espd.part2.last.name" props/>
        <@s.sectionLabelForValueOfNode person.getBirthDate()!"" "espd.part2.birth.date" props/>
        <@s.sectionLabelForValueOfNode person.getBirthPlace()!"" "espd.part2.birth.place" props/>
        <@s.sectionLabelForValueOfNode person.getPostalAddress().getAddressLine1()!"" "espd.part1.street.and.num" props/>
        <@s.sectionLabelForValueOfNode person.getPostalAddress().getPostCode()!"" "espd.part1.postcode" props/>
        <@s.sectionLabelForValueOfNode person.getPostalAddress().getCity()!"" "espd.part1.city" props/>
        <@s.sectionLabelForValueOfNode person.getPostalAddress().getCountryCode() "espd.part1.country" props/>
        <@s.sectionLabelForValueOfNode person.getContactDetails().getTelephoneNumber()!"" "espd.part2.telephone" props/>
        <@s.sectionLabelForValueOfNode person.getContactDetails().getEmailAddress()!"" "espd.part2.email" props/>
        <@s.sectionLabelForValueOfNode person.getRole()!"" "espd.part2.pos.act.in.capacity" props/>
    </div>
</#macro>

<#macro personalInfoRequest props naturalPersonsCounter>
    <div class="chapter-headline">
        ${pr.getESPDProperty("espd.part2.info.respresent", props)} #${naturalPersonsCounter}
    </div>
    <div class="chapter-content">
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.first.name", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.last.name", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.birth.date", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.birth.place", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.street.and.num", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.postcode", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.city", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part1.country", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.telephone", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.email", props)}</span>
        </div>
        <div class="descInput">
            <span class="content-left">${pr.getESPDProperty("espd.part2.pos.act.in.capacity", props)}</span>
        </div>
    </div>
</#macro>

<#macro tenderingCriteriaByCriterionTypeCode model modelType criterionTypeCode enProps props version>
    <#assign criteriaList = model.getEORelatedCriteriaList()>
    <#list criteriaList as criterion>
        <#if criterion.getTypeCode()?string?contains(criterionTypeCode?string)>
            <@tc.getTenderingCriterion criterion model modelType enProps props version/>
        </#if>
    </#list>
</#macro>
