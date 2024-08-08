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
        <label>${nameOfCriterion}</label>
        <p>${descOfCriterion}</p>
        <@getTenderingCriterionGroup criterion.getRequirementGroups() model modelType enProps props level+1 version/>
    </div>
</#macro>

<#macro getTenderingCriterionGroup groupOfRequirements model modelType enProps props level version>
    <#if groupOfRequirements??>
        <#list groupOfRequirements as gr>
            <#if level = 1>
                <div class="questionGroup">
                    <@getRequirementGroupContent gr.getRequirements() model modelType enProps props "question" version/>
                    <#assign level += 1>
                    <@getTenderingCriterionGroup gr.getRequirementGroups() model modelType enProps props level+1 version/>
                </div>
            </#if>
            <#if level &gt; 1>
                <div class="questionSubgroup">
                    <@getRequirementGroupContent gr.getRequirements() model modelType enProps props "question" version/>
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
    <#if req.getResponseDataType() == "INDICATOR" || req.getResponseDataType() == "CODE_BOOLEAN" || req.getResponseDataType() == "WEIGHT_INDICATOR">
        <@yesNoIndicator req modelType enProps props/>
    <#elseif req.getResponseDataType() == "DATE">
        <@getDescription req enProps props/>
        <@getDateValue req modelType/>
    <#elseif req.getResponseDataType() == "DESCRIPTION">
        <@getDescription req enProps props/>
        <@getDescriptionValue req modelType/>
    <#elseif req.getResponseDataType() == "EVIDENCE_URL">
        <@getDescription req enProps props/>
        <@getEvidenceURLValue req model modelType props/>
    <#elseif req.getResponseDataType() == "QUANTITY">
        <@getDescription req enProps props/>
        <@getQuantityValue req modelType/>
    <#elseif req.getResponseDataType() == "QUANTITY_YEAR">
        <@getDescription req enProps props/>
        <@getQuantityYearValue req modelType/>
    <#elseif req.getResponseDataType() == "QUANTITY_INTEGER">
        <@getDescription req enProps props/>
        <@getQuantityIntegerValue req modelType/>
    <#elseif req.getResponseDataType() == "AMOUNT">
        <@getDescription req enProps props/>
        <@getAmountValue req modelType/>
    <#elseif req.getResponseDataType() == "CODE_COUNTRY">
        <@getDescription req enProps props/>
        <@getCountryValue req modelType/>
    <#elseif req.getResponseDataType() == "PERCENTAGE">
        <@getDescription req enProps props/>
        <@getPercentageValue req modelType/>
    <#elseif req.getResponseDataType() == "PERIOD">
        <@getDescription req enProps props/>
        <@getPeriodValue req modelType version/>
    <#elseif req.getResponseDataType() == "CODE">
        <@getDescription req enProps props/>
        <@getCodeValue req modelType/>
    <#elseif req.getResponseDataType() == "EVIDENCE_IDENTIFIER">
        <@getDescription req enProps props/>
        <@getEvidenceIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "IDENTIFIER">
        <@getDescription req enProps props/>
        <@getIdentifierValue req model modelType props/>
    <#elseif req.getResponseDataType() == "URL">
        <@getDescription req enProps props/>
        <@getUrlValue req modelType/>
<#--    <#elseif req.getResponseDataType() == "WEIGHT_INDICATOR">-->
<#--        <@getDescription req enProps props/>-->
<#--        <@yesNoIndicator req modelType props/>-->
    <#elseif req.getResponseDataType() == "LOT_IDENTIFIER">
        <@getDescription req enProps props/>
        <@getLotIdentifierValue req model modelType props/>
        <@getDescription req enProps props/>
    <#elseif req.getResponseDataType() == "ECONOMIC_OPERATOR_IDENTIFIER">
        <@getDescription req enProps props/>
        <@getEOIdentifierValue req model modelType props/>
    </#if>
</#macro>

<#-- Generic Descriptions -->
<#macro getDescription req enProps props>
    <#if req.getDescription()??>
        <#assign reqDesc = req.getDescription()/>
        <#assign foundKey = pr.getESPDPropertyTranslationKey(reqDesc, enProps)/>
        <#assign desc = pr.getESPDProperty(foundKey, props)/>
        <label>${desc}</label>
    </#if>
</#macro>

<#macro getPeriodDescription req props>
    <#assign desc = pr.getESPDProperty('espd.crit.start.date',props)+"-"+pr.getESPDProperty('espd.crit.end.date', props)>
    <label>${desc}</label>
</#macro>
<#---->

<#macro yesNoIndicator req modelType enProps props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getDescription()??>
            <#assign reqDesc = req.getDescription()/>
            <#assign foundKey = pr.getESPDPropertyTranslationKey(reqDesc, enProps)/>
            <#assign desc = pr.getESPDProperty(foundKey, props)/>
            <#if req.getResponse().isIndicator()??>
                <label>${desc}<span class="icon-label"><i class="fa fa-dot-circle-o" aria-hidden="true"></i>${yesNo(req.getResponse().isIndicator(),props)}</span></label>
            </#if>
        </#if>
<#--    <#else>-->
<#--        <#assign yes = pr.getESPDProperty('espd.gui.yes', props)>-->
<#--        <#assign no = pr.getESPDProperty('espd.gui.no', props)>-->
<#--        ${yes} / ${no}-->
    </#if>
</#macro>

<#macro getDateValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDate()??>
            <#assign dateInput = req.getResponse().getDate()/>
            <#assign formattedDate = dateInput?date("yyyy-MM-dd")/>
            <p>${formattedDate?string("dd.MM.yyyy")}</p>
        </#if>
    <#else>
        <p>..</p>
    </#if>
</#macro>

<#macro getDescriptionValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDescription()??>
            <p>${req.getResponse().getDescription()}</p>
        </#if>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getEvidenceURLValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label>${pr.getESPDProperty('espd.crit.url',props)}</label>
        <p>&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#list model.getEvidenceList() as evidence>
            <#if req.getID() == evidence.getID()>
                <label>${pr.getESPDProperty('espd.crit.url',props)}</label>
                <p>${evidence.getEvidenceURL()}</p>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro getQuantityValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p>${req.getResponse().getQuantity()}</p>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getQuantityYearValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p>${req.getResponse().getYear()}</p>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getQuantityIntegerValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p>${req.getResponse().getQuantity()}</p>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getAmountValue req modelType>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getAmount()?? && req.getResponse().getCurrency()??>
            <#assign amountValue = req.getResponse().getAmount()+"\n"+req.getResponse().getCurrency() />
            <p>${amountValue}</p>
        </#if>
    </#if>
</#macro>

<#macro getCountryValue req modelType>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getCountryCode()??>
            <p> ${req.getResponse().getCountryCode()} </p>
        </#if>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getPercentageValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getPercentage()??>
            <p>${req.getResponse().getPercentage()?string("0.00")} &percnt;</p>
        </#if>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getPeriodValue req modelType version>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V1">
        <#if req.getResponse().getDescription()??>
            <p>${req.getResponse().getDescription()}</p>
        </#if>
    <#elseif modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V2">
        <#if req.getResponse().getStartDate()?? && req.getResponse().getEndDate()??>
            <p>${req.getResponse().getStartDate()} - ${req.getResponse().getEndDate()}</p>
        </#if>
    <#else>
        <p>..-..</p>
    </#if>
</#macro>

<#macro getCodeValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getEvidenceURLCode()??>
            <p>${req.getResponse().getEvidenceURLCode()}</p>
        </#if>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getEvidenceIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label>${pr.getESPDProperty('espd.crit.url',props)}</label>
        <p>&ndash;</p>
        <label>${pr.getESPDProperty('espd.crit.code',props)}</label>
        <p>&ndash;</p>
        <label>${pr.getESPDProperty('espd.crit.issuer',props)}</label>
        <p>&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#list model.getEvidenceList() as evidence>
            <#if req.getID() == evidence.getID()>
                <label>${pr.getESPDProperty('espd.crit.url',props)}</label>
                <p>${evidence.getEvidenceURL()}</p>
                <label>${pr.getESPDProperty('espd.crit.code',props)}</label>
                <p>${evidence.getDescription()}</p>
                <label>${pr.getESPDProperty('espd.crit.issuer',props)}</label>
                <p>${evidence.getEvidenceIssuer().getName()}</p>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro getUrlValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getUrl()??>
            <p> ${req.getResponse().getUrl()} </p>
        </#if>
    <#else>
        <p>&ndash;</p>
    </#if>
</#macro>

<#macro getLotIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label>${pr.getESPDProperty('espd.crit.lot.IDs',props)}</label>
        <p>&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#if req.getResponse()??>
            <#list model.getLotsList() as lot>
                <#if lot??>
                    <label>${pr.getESPDProperty('espd.crit.lot.IDs',props)}</label>
                    <p>${lot}</p>
                </#if>
            </#list>
        </#if>
    </#if>
</#macro>

<#macro getEOIdentifierValue req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label>${pr.getESPDProperty('espd.crit.entity.id',props)}</label>
        <p>&ndash;</p>
        <label>${pr.getESPDProperty('espd.crit.type.id',props)}</label>
        <p>&ndash;</p>
        <label>${pr.getESPDProperty('espd.crit.cpv.code',props)}</label>
        <p>&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#if req.getResponse()??>
            <label>${pr.getESPDProperty('espd.crit.entity.id',props)}</label>
            <p>${req.getResponse().getIdentifier()}</p>
            <label>${pr.getESPDProperty('espd.crit.type.id',props)}</label>
            <p>${req.getResponse().getEOIDType()}</p>
            <label>${pr.getESPDProperty('espd.crit.cpv.code',props)}</label>
        </#if>
    </#if>
</#macro>

<#macro getIdentifierValue req model modelType props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getIdentifier()??>
            <p>${req.getResponse().getIdentifier()}</p>
        </#if>
    <#else>
    <#--<label>${pr.getESPDProperty('espd.crit.qualification.number',props)}</label>-->
        <p>&ndash;</p>
    </#if>
</#macro>

<#function yesNo value props>
    <#switch value>
        <#case true>
            <#assign yes = pr.getESPDProperty('espd.gui.yes',props)>
            <#return '${yes}'>
        <#case false>
            <#assign no = pr.getESPDProperty('espd.gui.no',props)>
            <#return '${no}'>
    </#switch>
</#function>
