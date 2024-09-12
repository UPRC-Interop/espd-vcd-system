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

<#macro getTenderingCriterion criterion model modelType enProps props version>
    <#assign criterionID = criterion.getID()/>
    <#assign criterionName = criterion.getName()/>
    <#assign criterionDescription = criterion.getDescription()/>
    <#assign level = 0>
    <div class="criterion">
        <#if criterionID == "7d85e333-bbab-49c0-be8d-c36d71a72f5e">
            <#assign criterionName = pr.getESPDProperty("espd.crit.eu.title.payment.social.security", enProps)/>
        <#elseif criterionID == "696a75b2-6107-428f-8b74-82affb67e184">
            <#assign criterionName = pr.getESPDProperty("espd.crit.eu.title.guilty.misinterpretation", enProps)/>
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.eu.text.guilty.misinterpretation", enProps)/>
        <#elseif criterionID == "8b9700b7-b13c-41e6-a220-6bbf8d5fab31">
            <#assign criterionDescription = pr.getESPDProperty("espd.part2.lots.concerned", enProps)/>
        <#elseif criterionID == "a80ddb62-d25b-4e4e-ae22-3968460dc0a9">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.environmental", enProps)/>
        <#elseif criterionID == "a261a395-ed17-4939-9c75-b9ff1109ca6e">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.social", enProps)/>
        <#elseif criterionID == "a34b70d6-c43d-4726-9a88-8e2b438424bf">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.eu.text.breaching.obligations.labour", enProps)/>
        <#elseif criterionID == "6346959b-e097-4ea1-89cd-d1b4c131ea4d">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.selection.technical.number.managerial.staff.description", enProps)/>
        <#elseif criterionID == "1f49b3f0-d50f-43f6-8b30-4bafab108b9b">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.selection.technical.average.annual.manpower.description", enProps)/>
        <#elseif criterionID == "a7669d7d-9297-43e1-9d10-691a1660187c">
            <#assign criterionDescription = pr.getESPDProperty("espd.crit.selection.technical.supply.contracts.certificate.quality.description", enProps)/>
        <#elseif criterionID == "51c39ba9-0444-4967-afe9-36f753b30175">
            <#assign criterionDescription =  pr.getESPDProperty("espd.part5.title.eo.declares.that", enProps)/>
        <#elseif criterionID == "daffa2a9-9f8f-4568-8be8-7b8bf306d096">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.eu.text.national.bankruptcy", enProps)/>
        <#elseif criterionID == "8fda202a-0c37-41bb-9d7d-de3f49edbfcb">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.eu.text.liquidator", enProps)/>
        <#elseif criterionID == "166536e2-77f7-455c-b018-70582474e4f6">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.eu.text.suspended.business", enProps)/>
        <#elseif criterionID == "b1b5ac18-f393-4280-9659-1367943c1a2e">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.eu.text.conflict.interest", enProps)/>
        <#elseif criterionID == "499efc97-2ac1-4af2-9e84-323c2ca67747">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.economic.general.yearly.turnover.description", enProps)/>
        <#elseif criterionID == "d3dfb714-f558-4512-bbc5-e456fa2339de">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.economic.specific.average.turnover.description", enProps)/>
        <#elseif criterionID == "074f6031-55f9-4e99-b9a4-c4363e8bc315">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.economic.specific.yearly.turnover.description", enProps)/>
        <#elseif criterionID == "e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.economic.financial.ratio.description", enProps)/>
        <#elseif criterionID == "ab0e7f2e-6418-40e2-8870-6713123e41ad">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.economic.other.financial.requirements.description", enProps)/>
        <#elseif criterionID == "90a2e100-44cc-45d3-9970-69d6714f1596">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.technical.study.research.facilities.description", enProps)/>
        <#elseif criterionID == "07301031-2270-41af-8e7e-66fe0c777107">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.technical.educational.professional.qualifications.description", enProps)/>
        <#elseif criterionID == "c8809aa1-29b6-4f27-ae2f-27e612e394db">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.technical.allowance.of.checks.description", enProps)/>
        <#elseif criterionID == "612a1625-118d-4ea4-a6db-413184e7c0a8">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.technical.subcontracting.proportion.description", enProps)/>
        <#elseif criterionID == "5e506c16-26ab-4e32-bb78-b27f87dc0565">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.technical.service.contracts.performance.services.description", enProps)/>
        <#elseif criterionID == "9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4">
            <#assign criterionDescription =  pr.getESPDProperty("espd.crit.selection.suitability.service.contracts.auth.description", enProps)/>
        </#if>

        <#assign nameOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterionName,enProps), props)>
        <#assign descOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterionDescription,enProps), props)/>
        <label class="nameOfCriterionLabel">${nameOfCriterion}</label>
        <p class="descOfCriterionParagraph">${descOfCriterion}</p>
        <#if modelType == "ESPD_REQUEST">
            <@getTenderingCriterionGroupRequest criterion.getRequirementGroups() model modelType enProps props version/>
        <#elseif modelType == "ESPD_RESPONSE">
            <@getTenderingCriterionGroupResponse criterion.getRequirementGroups() model modelType enProps props version true/>
        </#if>
    </div>
</#macro>

<#macro getTenderingCriterionGroupRequest requirementGroups model modelType enProps props version>
    <#if requirementGroups??>
        <#list requirementGroups as requirementGroup>
            <div class="group">
                <#list requirementGroup.getRequirements() as requirement>
                    <div class="requirement">
                        <@getTenderCriterionContent requirement model modelType enProps props version/>
                    </div>
                </#list>
                <div class="requirementGroup">
                    <@getTenderingCriterionGroupRequest requirementGroup.getRequirementGroups() model modelType enProps props version/>
                </div>
            </div>
        </#list>
    </#if>
</#macro>

<#macro getTenderingCriterionGroupResponse requirementGroups model modelType enProps props version indicatorValue>
    <#if requirementGroups??>
        <#list requirementGroups as requirementGroup>
            <#assign condition = requirementGroup.getCondition()/>
            <#assign id = requirementGroup.getID()/>
            <#if id == "59e6f3ef-15cd-4e21-82ac-ea497ccd44e2">
                <#continue>
            <#elseif condition == "ONTRUE" && indicatorValue == false>
                <#continue>
            <#elseif condition == "ONFALSE" && indicatorValue == true>
                <#continue>
            </#if>
            <div class="group">
                <#list requirementGroup.getRequirements() as requirement>
                    <#if id == "73f0fe4c-4ed9-4343-8096-d898cf200146" && requirement.getResponseDataType() == "DESCRIPTION" && indicatorValue == false>
                        <#continue>
                    </#if>
                    <#if (indicatorValue && condition == "ONTRUE") || (indicatorValue && condition == "ON*") || (indicatorValue == false && condition == "ONFALSE") || (indicatorValue == false && condition == "ON*")>
                        <#assign showRequirementGroup = true/>
                    <#else>
                        <#assign showRequirementGroup = false/>
                    </#if>
                    <#if requirement.getResponse()??>
                        <#if requirement.getResponseDataType() == "INDICATOR">
                            <#assign indicator = requirement.getResponse().isIndicator()/>
                        </#if>
                    </#if>
                    <#if showRequirementGroup>
                        <div class="requirement">
                            <@getTenderCriterionContent requirement model modelType enProps props version/>
                        </div>
                    </#if>
                </#list>
                <#if showRequirementGroup>
                    <div class="requirementGroup">
                        <@getTenderingCriterionGroupResponse requirementGroup.getRequirementGroups() model modelType enProps props version indicator/>
                    </div>
                </#if>
            </div>
        </#list>
    </#if>
</#macro>

<#macro getTenderCriterionContent req model modelType enProps props version>
    <#if req.getResponseDataType() == "INDICATOR" || req.getResponseDataType() == "CODE_BOOLEAN">
        <@getDescription req modelType enProps props/>
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
        <@getDescription req modelType enProps props/>
        <@getQuantityYearValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "QUANTITY_INTEGER">
        <@getDescription req modelType enProps props/>
        <@getQuantityIntegerValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "AMOUNT">
        <@getDescription req modelType enProps props/>
        <@getAmountValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "CODE_COUNTRY">
        <@getCountryValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "PERCENTAGE">
        <@getDescription req modelType enProps props/>
        <@getPercentageValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "PERIOD">
        <@getPeriodDescription req modelType enProps props/>
        <@getPeriodValue req modelType version/>
    <#elseif req.getResponseDataType() == "CODE">
        <@getDescription req modelType enProps props/>
        <@getCodeValue req modelType enProps props/>
    <#elseif req.getResponseDataType() == "EVIDENCE_IDENTIFIER">
        <@getEvidenceIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "IDENTIFIER">
        <@getIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "URL">
        <@getDescription req modelType enProps props/>
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
        <#if req.getPropertyKeyOrNull()??>
            <#assign propertyKey = req.getPropertyKeyOrNull()/>
            <#assign genericDescription = pr.getESPDProperty(propertyKey, props)/>
            <label class="genericDescriptionLabel">${genericDescription}</label>
        </#if>
    </#if>
</#macro>

<#macro getPeriodDescription  req modelType enProps props>
    <#assign desc = pr.getESPDProperty("espd.crit.start.date", props) + " - " + pr.getESPDProperty("espd.crit.end.date", props)>
    <label class="periodDescriptionLabel">${desc}</label>
</#macro>
<#---->

<#macro yesNoIndicator req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().isIndicator()??>
            <label class="yesNoIndicatorLabel"><span class="icon-label"><i class="fa fa-dot-circle-o" aria-hidden="true"></i>${yesNo(req.getResponse().isIndicator(), props)}</span></label>
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
            <#if req.getPropertyKeyOrNull()??>
                <#assign propertyKey = req.getPropertyKeyOrNull()/>
                <#assign dateInput = req.getResponse().getDate()/>
                <#assign formattedDate = dateInput?date("yyyy-MM-dd")/>
                <label class="dateLabel">${pr.getESPDProperty(propertyKey, props)}</label>
                <p class="dateParagraph">${formattedDate?string("dd.MM.yyyy")}</p>
            </#if>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <#if req.getPropertyKeyOrNull()??>
            <#assign propertyKey = req.getPropertyKeyOrNull()/>
            <label class="dateLabel">${pr.getESPDProperty(propertyKey, props)}</label>
        </#if>
        <p class="dateParagraph">..</p>
    </#if>
</#macro>

<#macro getDescriptionValue req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDescription()??>
            <#if req.getPropertyKeyOrNull()??>
                <#assign propertyKey = req.getPropertyKeyOrNull()/>
                <#assign description = pr.getESPDProperty(propertyKey, props)/>
                <label class="descriptionLabel">${description}</label>
                <p class="descriptionParagraph">${req.getResponse().getDescription()}</p>
            </#if>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <#if req.getPropertyKeyOrNull()??>
            <#assign propertyKey = req.getPropertyKeyOrNull()/>
            <label class="descriptionLabel">${pr.getESPDProperty(propertyKey, props)}</label>
        </#if>
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
        <p class="quantityYearParagraph">${req.getResponse().getYear()?c}</p>
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
            <#assign amountValue = req.getResponse().getAmount()?c + "\n" + req.getResponse().getCurrency()/>
            <p class="amountParagraph">${amountValue}</p>
        </#if>
    <#elseif modelType == "ESPD_REQUEST">
        <p class="amountParagraph">&ndash;</p>
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
        <p class="percentageParagraph">&percnt;</p>
    </#if>
</#macro>

<#macro getPeriodValue req modelType version>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V1">
        <#if req.getResponse().getDescription()??>
            <p class="periodParagraph">${req.getResponse().getDescription()}</p>
        </#if>
    <#elseif modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V2">
        <#if req.getResponse().getStartDate()?? && req.getResponse().getEndDate()??>
            <#assign startDateInput = req.getResponse().getStartDate()/>
            <#assign endDateInput = req.getResponse().getEndDate()/>
            <#assign formattedStartDate = startDateInput?date("yyyy-MM-dd")/>
            <#assign formattedEndDate = endDateInput?date("yyyy-MM-dd")/>
            <p class="periodParagraph">${formattedStartDate?string("dd.MM.yyyy")} - ${formattedEndDate?string("dd.MM.yyyy")}</p>
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
    <#elseif modelType == "ESPD_RESPONSE">
        <#if req.getResponse()??>
            <label class="eoIdentifierEntityLabel">${pr.getESPDProperty("espd.crit.entity.id", props)}</label>
            <p class="eoIdentifierEntityParagraph">${req.getResponse().getIdentifier()}</p>
            <label class="eoIdentifierTypeLabel">${pr.getESPDProperty("espd.crit.type.id", props)}</label>
            <p class="eoIdentifierTypeParagraph">${req.getResponse().getEOIDType()}</p>
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
