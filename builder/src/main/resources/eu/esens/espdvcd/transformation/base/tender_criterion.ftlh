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
<#import '../html/chapter.ftlh' as c >
<#import '../html/date.ftlh' as d >
<#import '../propertyreader/property_reader.ftlh' as pr >

<#--<span style="background-color: beige">HERE IT IS</span>-->
<#--<p style="background-color: beige">${reqDesc}</p>-->

<#macro getTenderingCriterion criterion model modelType enProps props version>
    <#assign nameOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterion.getName(),enProps),props)>
    <#assign descOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterion.getDescription(),enProps),props)/>
    <#assign level = 0>
    <div class="criterion">
        <#if criterion.getName() == "Payment of social security contributions">
            <#assign nameOfCriterion = pr.getESPDProperty("espd.crit.eu.title.payment.social.security",props)/>
        <#elseif criterion.getName() = "Guilty of misrepresentation, withheld information, unable to provide required documents and obtained confidential information of this procedure">
            <#assign nameOfCriterion = pr.getESPDProperty("espd.crit.eu.title.guilty.misinterpretation",props)/>
        </#if>

        <#if descOfCriterion == "Where applicable, indication of the lot(s) for which the economic operator wishes to tender">
            <#assign descOfCriterion = pr.getESPDProperty("espd.part2.lots.concerned",props)/>
        <#elseif criterion.getName() = "Guilty of misrepresentation, withheld information, unable to provide required documents and obtained confidential information of this procedure">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.eu.text.guilty.misinterpretation",props)/>
        <#elseif criterion.getName() = "Breaching of obligations in the field of environmental law">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.environmental",props)/>
        <#elseif criterion.getName() = "Breaching of obligations in the field of social law">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.social",props)/>
        <#elseif criterion.getName() = "Breaching of obligations in the field of labour law">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.labour",props)/>
        <#elseif criterion.getName() = "Number of managerial staff">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.selection.technical.number.managerial.staff.description",props)/>
        <#elseif criterion.getName() = "Average annual manpower">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.selection.technical.average.annual.manpower.description",props)/>
        <#elseif criterion.getName() = "For supply contracts: certificates by quality control institutes">
            <#assign descOfCriterion = pr.getESPDProperty("espd.crit.selection.technical.supply.contracts.certificate.quality.description",props)/>
        <#elseif criterion.getName() = "Reduction of the number of qualified candidates">
            <#assign descOfCriterion = pr.getESPDProperty("espd.part5.title.eo.declares.that",props)/>
        </#if>

        <label class="nameOfCriterionLabel">${nameOfCriterion}</label>
        <p class="descOfCriterionParagraph">${descOfCriterion}</p>

        <@getTenderingCriterionGroup criterion.getRequirementGroups() model modelType enProps props level+1 version/>
    </div>
</#macro>

<#macro getTenderingCriterionGroup groupOfRequirements model modelType enProps props level version>
    <#if groupOfRequirements??>
        <#list groupOfRequirements as gr>
            <#if level = 1>
                <div class="questionGroup">
                    <@getRequirementGroupContent gr.getRequirements() model modelType enProps props "question" version/>
                    <@getTenderingCriterionGroup gr.getRequirementGroups() model modelType enProps props level+1 version/>
                </div>
            <#elseif level &gt; 1>
                <div class="questionSubgroup">
                    <@getRequirementGroupContent gr.getRequirements() model modelType enProps props "question" version/>
                    <@getTenderingCriterionGroup gr.getRequirementGroups() model modelType enProps props level+1 version/>
                </div>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro getRequirementGroupContent groupOfRequirements model modelType enProps props className version>
    <#list groupOfRequirements as req>
        <div class="${className}">
            <@getTenderCriterionContent req model modelType enProps props version/>
        </div>
    </#list>
</#macro>

<#macro getTenderCriterionContent req model modelType enProps props version>
    <#if req.getResponseDataType() != "DESCRIPTION" && req.getResponseDataType() !="PERIOD">
        <@getDescription req modelType enProps props/>
    <#elseif req.getResponseDataType() != "DESCRIPTION" && req.getResponseDataType() ="PERIOD">
        <@getPeriodDescription req modelType enProps props/>
    </#if>

    <#if req.getResponseDataType() == "INDICATOR" || req.getResponseDataType() == "CODE_BOOLEAN">
        <@yesNoIndicator req modelType enProps props/>
    <#elseif req.getResponseDataType() == "DATE">
        <@getDateValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "DESCRIPTION">
        <@getDescriptionValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "EVIDENCE_URL">
        <@getEvidenceURLValue req model modelType props/>
    <#elseif req.getResponseDataType() == "QUANTITY">
        <@getQuantityValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "QUANTITY_YEAR">
        <@getQuantityYearValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "QUANTITY_INTEGER">
        <@getQuantityIntegerValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "AMOUNT">
        <@getAmountValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "CODE_COUNTRY">
        <@getCountryValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "PERCENTAGE">
        <@getPercentageValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "PERIOD">
        <@getPeriodValue req modelType version/>
    <#elseif req.getResponseDataType() == "CODE">
        <@getCodeValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "EVIDENCE_IDENTIFIER">
        <@getEvidenceIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "IDENTIFIER">
        <@getIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "URL">
        <@getUrlValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "WEIGHT_INDICATOR">
        <@yesNoIndicator req modelType enProps props/>
    <#elseif req.getResponseDataType() == "LOT_IDENTIFIER">
        <@getLotIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "ECONOMIC_OPERATOR_IDENTIFIER">
        <@getEOIdentifierValue req model modelType props/>
    </#if>
</#macro>

<#-- Generic Descriptions -->
<#macro getDescription req modelType enProps props>
    <#if req.getDescription()??>
        <#assign reqDesc = req.getDescription()/>
        <#assign hiddenElementClass = ""/>

        <#if reqDesc == "Is this information available electronically?">
            <#assign desc = pr.getESPDProperty("espd.crit.information.available.electronically", props)/>
        <#elseif reqDesc == pr.getESPDProperty("espd.crit.eo.has.cert.soc", enProps) || reqDesc == pr.getESPDProperty("espd.crit.decision.final.and.binding", enProps) || reqDesc == pr.getESPDProperty("espd.crit.please.describe.them", enProps) || reqDesc == pr.getESPDProperty("espd.part3.crit.eu.national.bankruptcy.title", enProps) || reqDesc == pr.getESPDProperty("espd.crit.eo.has.cert.soc", enProps)>
            <#assign desc = ""/>
            <#assign hiddenElementClass = "hiddenGenericDescriptionLabel"/>
        <#elseif reqDesc == "Have you taken measures to demonstrate your reliability" || reqDesc == "Have you taken measures to demonstrate your reliability (Self-Cleaning)?">
            <#assign desc = pr.getESPDProperty("espd.crit.taken.self.cleaning.measures", props)/>
        <#elseif reqDesc == "Your answer">
            <#assign desc = pr.getESPDProperty("espd.crit.your.answer", props)/>
        <#else>
            <#assign key = pr.getESPDPropertyTranslationKey(reqDesc, enProps)/>
            <#assign desc = pr.getESPDProperty(key, props)/>
        </#if>

        <#if hiddenElementClass = "">
            <label class="genericDescriptionLabel">${desc}</label>
        <#else>
            <label class="genericDescriptionLabel ${hiddenElementClass}">${desc}</label>
        </#if>
    </#if>
</#macro>

<#macro getPeriodDescription  req modelType enProps props>
    <#assign desc = pr.getESPDProperty("espd.crit.start.date", props) + "-" + pr.getESPDProperty("espd.crit.end.date", props)>
    <label class="periodDescriptionLabel">${desc}</label>
</#macro>
<#---->

<#macro yesNoIndicator req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().isIndicator()??>
            <label class="yesNoIndicatorLabel"><span class="icon-label"><i class="fa fa-dot-circle-o" aria-hidden="true"></i>${yesNo(req.getResponse().isIndicator(),props)}</span></label>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <#assign yes = pr.getESPDProperty("espd.gui.yes", props)>
        <#assign no = pr.getESPDProperty("espd.gui.no", props)>
        ${yes} / ${no}
    </#if>
</#macro>

<#macro getDateValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDate()??>
            <#assign dateInput = req.getResponse().getDate()/>
            <#assign formattedDate = dateInput?date("yyyy-MM-dd")/>
            <p class="dateParagraph">${formattedDate?string("dd.MM.yyyy")}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="dateParagraph">..</p>
    </#if>
</#macro>

<#macro getDescriptionValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDescription()??>
            <#assign resDesc = req.getResponse().getDescription()/>
            <#assign reqDesc = req.getDescription()/>

            <#if resDesc = "__Bankruptcy">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Insolvency">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Creditors">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__BankruptcyNationalLaw">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Liquidator">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Suspension">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Misconduct">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Competition">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__ConflictOfInterests">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__InvolvementIntoProcurementProcedure">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__Damages">
                <#assign key = "espd.crit.please.give.more.details"/>
            <#elseif resDesc = "__EnvManagement">
                <#assign key = "espd.part4.crit.explain.supply.contracts.environmental.law"/>
            <#elseif reqDesc == "Indicate reasons for being nevertheless to perform the contract">
                <#assign key = "espd.crit.reasons.nevertheless.contract"/>
            <#elseif reqDesc == "Lot Ids">
                <#assign key = "espd.crit.your.answer"/>
            <#else>
                <#assign key = pr.getESPDPropertyTranslationKey(reqDesc, enProps)/>
            </#if>

            <#assign desc = pr.getESPDProperty(key, props)/>

            <label class="descriptionLabel">${desc}</label>
            <p class="descriptionParagraph">${req.getResponse().getDescription()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="descriptionParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getEvidenceURLValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label class="evidenceUrlLabel">${pr.getESPDProperty("espd.crit.url",props)}</label>
        <p class="evidenceUrlParagraph">&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#list model.getEvidenceList() as evidence>
            <#if req.getID() == evidence.getID()>
                <label class="evidenceUrlLabel">${pr.getESPDProperty("espd.crit.url", props)}</label>
                <p class="evidenceUrlParagraph">${evidence.getEvidenceURL()}</p>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro getQuantityValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p class="quantityParagraph">${req.getResponse().getQuantity()}</p>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="quantityParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getQuantityYearValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p class="quantityYearParagraph">${req.getResponse().getYear()}</p>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="quantityYearParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getQuantityIntegerValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p class="quantityIntegerParagraph">${req.getResponse().getQuantity()}</p>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="quantityIntegerParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getAmountValue req modelType enProps props>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getAmount()?? && req.getResponse().getCurrency()??>
            <#assign amountValue = req.getResponse().getAmount()+"\n"+req.getResponse().getCurrency() />
            <p class="amountParagraph">${amountValue}</p>
        </#if>
    </#if>
</#macro>

<#macro getCountryValue req modelType enProps props>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getCountryCode()??>
            <label class="countryLabel">${pr.getESPDProperty("espd.crit.country.member.state", props)}</label>
            <p class="countryParagraph">${req.getResponse().getCountryCode()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <label class="countryLabel">${pr.getESPDProperty("espd.crit.country.member.state", props)}</label>
        <p class="countryParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getPercentageValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getPercentage()??>
            <p class="percentageParagraph">${req.getResponse().getPercentage()?string("0.00")} &percnt;</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="percentageParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getPeriodValue req modelType version>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V1">
        <#if req.getResponse().getDescription()??>
            <p class="periodParagraph">${req.getResponse().getDescription()}</p>
        </#if>
    <#elseif modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V2">
        <#if req.getResponse().getStartDate()?? && req.getResponse().getEndDate()??>
            <p class="periodParagraph">${req.getResponse().getStartDate()} - ${req.getResponse().getEndDate()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="periodParagraph">..-..</p>
    </#if>
</#macro>

<#macro getCodeValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getEvidenceURLCode()??>
            <p class="codeParagraph">${req.getResponse().getEvidenceURLCode()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="codeParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getEvidenceIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label class="evidenceIdentifierUrlLabel">${pr.getESPDProperty("espd.crit.url", props)}</label>
        <p class="evidenceIdentifierUrlParagraph">&ndash;</p>
        <label class="evidenceIdentifierCodeLabel">${pr.getESPDProperty("espd.crit.code", props)}</label>
        <p class="evidenceIdentifierCodeParagraph">&ndash;</p>
        <label class="evidenceIdentifierIssuerLabel">${pr.getESPDProperty("espd.crit.issuer", props)}</label>
        <p class="evidenceIdentifierIssuerParagraph">&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#list model.getEvidenceList() as evidence>
            <#if req.getID() == evidence.getID()>
                <label class="evidenceIdentifierUrlLabel">${pr.getESPDProperty("espd.crit.url", props)}</label>
                <p class="evidenceIdentifierUrlParagraph">${evidence.getEvidenceURL()}</p>
                <label class="evidenceIdentifierCodeLabel">${pr.getESPDProperty("espd.crit.code", props)}</label>
                <p class="evidenceIdentifierCodeParagraph">${evidence.getDescription()}</p>
                <label class="evidenceIdentifierIssuerLabel">${pr.getESPDProperty("espd.crit.issuer", props)}</label>
                <p class="evidenceIdentifierIssuerParagraph">${evidence.getEvidenceIssuer().getName()}</p>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro getUrlValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getUrl()??>
            <p class="urlParagraph">${req.getResponse().getUrl()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="urlParagraph">&ndash;</p>
    </#if>
</#macro>

<#macro getLotIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label class="lotIdentifierLabel">${pr.getESPDProperty("espd.crit.lot.IDs", props)}</label>
        <p class="lotIdentifierParagraph">&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#if req.getResponse()??>
            <#list model.getLotsList() as lot>
                <#if lot??>
                    <label class="lotIdentifierLabel">${pr.getESPDProperty("espd.crit.lot.IDs", props)}</label>
                    <p class="lotIdentifierParagraph">${lot}</p>
                </#if>
            </#list>
        </#if>
    </#if>
</#macro>

<#macro getEOIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label class="eoIdentifierEntityLabel">${pr.getESPDProperty("espd.crit.entity.id", props)}</label>
        <p class="eoIdentifierEntityParagraph">&ndash;</p>
        <label class="eoIdentifierTypeLabel">${pr.getESPDProperty("espd.crit.type.id", props)}</label>
        <p class="eoIdentifierTypeParagraph">&ndash;</p>
        <label class="eoIdentifierCpvLabel">${pr.getESPDProperty("espd.crit.cpv.code", props)}</label>
        <p class="eoIdentifierCpvParagraph">&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#if req.getResponse()??>
            <label class="eoIdentifierEntityLabel">${pr.getESPDProperty("espd.crit.entity.id", props)}</label>
            <p class="eoIdentifierEntityParagraph">${req.getResponse().getIdentifier()}</p>
            <label class="eoIdentifierTypeLabel">${pr.getESPDProperty("espd.crit.type.id", props)}</label>
            <p class="eoIdentifierTypeParagraph">${req.getResponse().getEOIDType()}</p>
            <label class="eoIdentifierCpvLabel">${pr.getESPDProperty("espd.crit.cpv.code", props)}</label>
        </#if>
    </#if>
</#macro>

<#macro getIdentifierValue req model modelType props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getIdentifier()??>
            <p class="identifierParagraph">${req.getResponse().getIdentifier()}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <label class="identifierLabel">${pr.getESPDProperty("espd.crit.qualification.number", props)}</label>
        <p class="identifierParagraph">&ndash;</p>
    </#if>
</#macro>

<#function yesNo value props>
    <#switch value>
        <#case true>
            <#assign yes = pr.getESPDProperty("espd.gui.yes", props)>
            <#return '${yes}'>
        <#case false>
            <#assign no = pr.getESPDProperty("espd.gui.no", props)>
            <#return '${no}'>
    </#switch>
</#function>
