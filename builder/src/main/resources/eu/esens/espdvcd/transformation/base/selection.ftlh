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
<#import '../html/chapter.ftlh' as c>
<#import './tender_criterion.ftlh' as tc>

<#macro selectionSection model modelType enProps props version>
    <#assign selectionSwitch = pr.getESPDProperty("espd.part4.all.selection.switch", props)>
    <div class="section">
        <h3>${pr.getESPDProperty("espd.part4.header", props)}</h3>
        <@selectionChapter model modelType enProps props version "espd.part4.suitability"/>
        <@selectionChapter model modelType enProps props version "espd.part4.economic.and.financial.standing"/>
        <@selectionChapter model modelType enProps props version "espd.part4.technical.professional.ability"/>
        <@selectionChapter model modelType enProps props version "espd.part4.quality.assurance"/>
    </div>
</#macro>

<#macro selectionChapter model modelType enProps props version chapter>
    <div class="chapter">
        <#if chapter == "espd.part4.suitability">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.suitability", props)}</div>
            <div class="chapter-content">
                <div class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</div>
                <@selectionCriteriaContent model modelType '58(2)' enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.economic.and.financial.standing">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.economic.and.financial.standing", props)}</div>
            <div class="chapter-content">
                <div class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</div>
                <@selectionCriteriaContent model modelType '58(3)' enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.technical.professional.ability">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.technical.professional.ability", props)}</div>
            <div class="chapter-content">
                <div class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</div>
                <@selectionCriteriaContent model modelType '58(4)' enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.quality.assurance">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.quality.assurance", props)}</div>
            <div class="chapter-content">
                <div class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</div>
                <@selectionCriteriaContent model modelType '62(2)' enProps props version/>
            </div>
        </#if>
    </div>
</#macro>

<#macro selectionCriteriaContent model modelType article enProps props version>
    <#assign selectionCriteria = model.getSelectionCriteriaList()>
    <#list selectionCriteria as criterion>
        <#if criterion.getLegislationReference()??>
            <#if criterion.getLegislationReference().getArticle()?contains(article?string)>
                <@tc.getTenderingCriterion criterion model modelType enProps props version/>
                <#if criterion.getSubCriterionList()??>
                    <#assign subCriterionList = criterion.getSubCriterionList()/>
                    <#list subCriterionList as subCriterion>
                        <@tc.getTenderingCriterion subCriterion model modelType enProps props version/>
                    </#list>
                </#if>
            </#if>
        </#if>
    </#list>
</#macro>
