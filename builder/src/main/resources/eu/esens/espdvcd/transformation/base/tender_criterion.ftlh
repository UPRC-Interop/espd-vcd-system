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

<#assign levelOfRequirements = 1>

<#macro getTenderingCriterion criterion model modelType enProps props version>
    <#assign nameOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterion.getName(),enProps),props)>
    <#assign descOfCriterion = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(criterion.getDescription(),enProps),props)/>
    <#if nameOfCriterion == descOfCriterion>
        <p>pTest1${descOfCriterion}</p>
    <#else>
        <div class="questionGroup">
            <label class="criterionLabel">labelTest1${nameOfCriterion}</label>
            <p>pTest2${descOfCriterion}</p>
            <@getTenderingCriterionGroup criterion.getRequirementGroups() model modelType enProps props levelOfRequirements version/>
        </div>
    </#if>
</#macro>

<#macro getTenderingCriterionGroup groupOfRequirements model modelType enProps props level version>
    <#assign chapterString = c.getChapterString('')>
    <#if groupOfRequirements??>
        <#list groupOfRequirements as gr>
            <@getRequirementGroupContent gr.getRequirements() levelOfRequirements chapterString model modelType enProps props version/>
            <#assign levelOfRequirements += 1>
            <@getTenderingCriterionGroup gr.getRequirementGroups() model modelType enProps props level version/>
            <#assign levelOfRequirements = 1>
        </#list>
    </#if>
</#macro>

<#macro getRequirementGroupContent groupOfRequirements level chapterString model modelType enProps props version>
    <#list groupOfRequirements as req>
        <#assign levelString = c.getLevelString(level)>
        <div class="question ${levelString} ${chapterString}">
            <div class="question-content">
                <@getTenderCriterionContent req model modelType enProps props version/>
            </div>
        </div>
    </#list>
</#macro>

<#macro getTenderCriterionContent req model modelType enProps props version>
    <#if req.getResponseDataType() == "EVIDENCE_IDENTIFIER" || req.getResponseDataType() == "EVIDENCE_URL">
        <@getEvidenceIdentifier req model modelType props />
    <#else>
        <#if req.getResponseDataType() == "PERIOD">
            <@getPeriodDescription req props />
        <#else>
            <@getDescription req enProps props />
        </#if>

        <#if req.getResponseDataType() == "INDICATOR" || req.getResponseDataType() == "CODE_BOOLEAN">
            <@yesNoIndicator req modelType props />
        <#elseif req.getResponseDataType() == "DESCRIPTION">
            <@getDescriptionValue req modelType />
        <#elseif req.getResponseDataType() == "AMOUNT">
            <@getAmountValue req modelType />
        <#elseif req.getResponseDataType() == "DATE">
            <@getDateValue req modelType />
        <#elseif req.getResponseDataType() == "CODE_COUNTRY">
            <@getCountryValue req modelType />
        <#elseif req.getResponseDataType() == "IDENTIFIER">
            <@getIdentifierValue req modelType />
        <#elseif req.getResponseDataType() == "CODE">
            <@getCodeValue req modelType />
        <#elseif req.getResponseDataType() == "URL">
            <@getUrlValue req modelType />
        <#elseif req.getResponseDataType() == "PERCENTAGE">
            <@getPercentageValue req modelType />
        <#elseif req.getResponseDataType() == "QUANTITY_INTEGER">
            <@getQuantityIntegerValue req modelType />
        <#elseif req.getResponseDataType() == "QUANTITY_YEAR">
            <@getQuantityYearValue req modelType />
        <#elseif req.getResponseDataType() == "PERIOD">
            <@getPeriodValue req modelType version/>
        </#if>
    </#if>
</#macro>

<#macro getDescription req enProps props>
    <#if req.getDescription()??>
        <#assign reqDesc = req.getDescription()>
        <#assign desc = pr.getESPDProperty(pr.getESPDPropertyTranslationKey(reqDesc,enProps),props)>
        <label>labelTest2${desc}</label>
    </#if>
</#macro>

<#macro getPeriodDescription req props>
    <#assign desc = pr.getESPDProperty('espd.crit.start.date',props)+"-"+pr.getESPDProperty('espd.crit.end.date',props)>
    <label>labelTest3${desc}</label>
</#macro>

<#macro yesNoIndicator req modelType props>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().isIndicator()??>
            <p>pTest3${yesNo(req.getResponse().isIndicator(),props)}</p>
        </#if>
    <#else>
        <#assign yes = pr.getESPDProperty('espd.gui.yes',props)>
        <#assign no = pr.getESPDProperty('espd.gui.no',props)>
        ${yes} / ${no}
    </#if>
</#macro>

<#macro getCountryValue req modelType>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getCountryCode()??>
            <p>pTest4${req.getResponse().getCountryCode()} </p>
        </#if>
    <#else>
        <p>pTest5&ndash;</p>
    </#if>
</#macro>

<#macro getAmountValue req modelType>
    <#if modelType=="ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getAmount()?? && req.getResponse().getCurrency()??>
            <#assign amountValue = req.getResponse().getAmount()+"\n"+req.getResponse().getCurrency() />
            <p>pTest6${amountValue}</p>
        </#if>
    </#if>
</#macro>

<#macro getDateValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDate()??>
            <p>pTest7${req.getResponse().getDate()} </p>
        </#if>
    <#else>
        <p>pTest8..</p>
    </#if>
</#macro>

<#macro getDescriptionValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getDescription()??>
          <p>pTest9${req.getResponse().getDescription()}</p>
        </#if>
    <#else>
        <p>pTest10&ndash;</p>
    </#if>
</#macro>

<#macro getIdentifierValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getIdentifier()??>
            <p>pTest11${req.getResponse().getIdentifier()}</p>
        </#if>
    <#else>
        <p>pTest12&ndash;</p>
    </#if>
</#macro>

<#macro getCodeValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getEvidenceURLCode()??>
            <p>pTest13${req.getResponse().getEvidenceURLCode()}</p>
        </#if>
    <#else>
        <p>pTest14&ndash;</p>
    </#if>
</#macro>

<#macro getUrlValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getUrl()??>
            <p>pTest15${req.getResponse().getUrl()} </p>
        </#if>
    <#else>
        <p>pTest16&ndash;</p>
    </#if>
</#macro>

<#macro getPercentageValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <#if req.getResponse().getPercentage()??>
            <p>pTest17${req.getResponse().getPercentage()} &percnt;</p>
        </#if>
    <#else>
        <p>pTest18&ndash;</p>
    </#if>
</#macro>

<#macro getPeriodValue req modelType version>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V1">
        <#if req.getResponse().getDescription()??>
            <p>pTest19${req.getResponse().getDescription()}</p>
        </#if>
    <#elseif modelType == "ESPD_RESPONSE" && req.getResponse()?? && version == "V2">
        <#if req.getResponse().getStartDate()?? && req.getResponse().getEndDate()??>
            <p>pTest20${req.getResponse().getStartDate()} - ${req.getResponse().getEndDate()}</p>
        </#if>
    <#else>
        <p>pTest21..-..</p>
    </#if>
</#macro>

<#macro getQuantityIntegerValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p>pTest22${req.getResponse().getQuantity()}</p>
    <#else>
        <p>pTest23&ndash;</p>
    </#if>
</#macro>

<#macro getQuantityYearValue req modelType>
    <#if modelType == "ESPD_RESPONSE" && req.getResponse()??>
        <p>pTest24${req.getResponse().getYear()}</p>
    <#else>
        <p>pTest25&ndash;</p>
    </#if>
</#macro>

<#macro getEvidenceIdentifier req model modelType props>
    <#if modelType == "ESPD_REQUEST">
        <label>labelTest4${pr.getESPDProperty('espd.crit.url',props)}</label>
        <p>pTest26&ndash;</p>
        <label>labelTest5${pr.getESPDProperty('espd.crit.code',props)}</label>
        <p>pTest27&ndash;</p>
        <label>labelTest6${pr.getESPDProperty('espd.crit.issuer',props)}</label>
        <p>pTest28&ndash;</p>
    <#elseif modelType == "ESPD_RESPONSE">
        <#list model.getEvidenceList() as evidence>
            <#if req.getID() == evidence.getID()>
                <label>labelTest7${pr.getESPDProperty('espd.crit.url',props)}</label>
                <p>pTest29${evidence.getEvidenceURL()}</p>
                <label>labelTest8${pr.getESPDProperty('espd.crit.code',props)}</label>
                <p>pTest30${evidence.getDescription()}</p>
                <label>labelTest9${pr.getESPDProperty('espd.crit.issuer',props)}</label>
                <p>pTest31${evidence.getEvidenceIssuer().getName()}</p>
            </#if>
        </#list>
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
