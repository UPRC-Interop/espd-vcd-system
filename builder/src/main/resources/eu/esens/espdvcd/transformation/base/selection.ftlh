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
    <div class="section">
        <h3>${pr.getESPDProperty("espd.part4.header", props)}</h3>

        <#assign CriteriaCounterA = 0/>
        <#assign CriteriaCounterB = 0/>
        <#assign CriteriaCounterC = 0/>
        <#assign CriteriaCounterD = 0/>
        <#assign emptyA = false/>
        <#assign emptyB = false/>
        <#assign emptyC = false/>
        <#assign emptyD = false/>

        <#assign selectionCriteria = model.getSelectionCriteriaList()>
        <#list selectionCriteria as criterion>
            <#assign criterionGroup = criterion.getCriterionGroup()/>
            <#assign criterionTypeCode = criterion.getTypeCode()/>
            <#if criterion.getID() == "f4dc58dd-af45-4602-a4c8-3dca30fac082" && criterion.isSelected()>
                <#assign isSelectionAllcriterion = true/>
                <#break>
            </#if>
            <#if criterionGroup == "CRITERION.SELECTION.SUITABILITY" && criterion.isSelected()>
                <#assign CriteriaCounterA += 1/>
            <#elseif criterionGroup == "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING" && criterion.isSelected()>
                <#assign CriteriaCounterB += 1/>
            <#elseif criterionGroup == "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY" && criterion.isSelected()>
                <#if criterionTypeCode == "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE" || criterionTypeCode == "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE">
                    <#assign CriteriaCounterD += 1/>
                <#else>
                    <#assign CriteriaCounterC += 1/>
                </#if>
            </#if>
        <#else>
            <#assign emptyA = true/>
            <#assign emptyB = true/>
            <#assign emptyC = true/>
            <#assign emptyD = true/>
        </#list>

        <#if CriteriaCounterA == 0>
            <#assign emptyA = true/>
        </#if>
        <#if CriteriaCounterB == 0>
            <#assign emptyB = true/>
        </#if>
        <#if CriteriaCounterC == 0>
            <#assign emptyC = true/>
        </#if>
        <#if CriteriaCounterD == 0>
            <#assign emptyD = true/>
        </#if>

        <#if isSelectionAllcriterion??>
            <@selectionChapter model modelType enProps props version "espd.part4.all.selection.switch"/>
        <#else>
            <#if (emptyA == false) && (emptyB == false) && (emptyC == false) && (emptyD == false)>
                <@selectionChapter model modelType enProps props version "espd.part4.suitability"/>
                <@selectionChapter model modelType enProps props version "espd.part4.economic.and.financial.standing"/>
                <@selectionChapter model modelType enProps props version "espd.part4.technical.professional.ability"/>
                <@selectionChapter model modelType enProps props version "espd.part4.quality.assurance"/>
            <#else>
                <#if emptyA == false>
                    <@selectionChapter model modelType enProps props version "espd.part4.suitability"/>
                </#if>
                <#if emptyB == false>
                    <@selectionChapter model modelType enProps props version "espd.part4.economic.and.financial.standing"/>
                </#if>
                <#if emptyC == false>
                    <@selectionChapter model modelType enProps props version "espd.part4.technical.professional.ability"/>
                </#if>
                <#if emptyD == false>
                    <@selectionChapter model modelType enProps props version "espd.part4.quality.assurance"/>
                </#if>
            </#if>
        </#if>
    </div>
</#macro>

<#macro selectionChapter model modelType enProps props version chapter>
    <div class="chapter">
        <#if chapter == "espd.part4.all.selection.switch">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.all.selection.switch", props)}</div>
            <div class="chapter-content">
                <#assign selectionCriteria = model.getSelectionCriteriaList()>
                <#list selectionCriteria as criterion>
                    <#if criterion.getID() == "f4dc58dd-af45-4602-a4c8-3dca30fac082" && criterion.isSelected()>
                        <label class="selectionChaptersInfo">${pr.getESPDProperty("espd.part4.crit.selection.ca.declares.that", props)}</label>
                        <@getSelectionAllCriterion criterion model modelType enProps props version/>
                        <#break>
                    </#if>
                </#list>
            </div>
        <#elseif chapter == "espd.part4.suitability">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.suitability", props)}</div>
            <div class="chapter-content">
                <label class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</label>
                <@selectionCriteriaContent model modelType "58(2)" enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.economic.and.financial.standing">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.economic.and.financial.standing", props)}</div>
            <div class="chapter-content">
                <label class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</label>
                <@selectionCriteriaContent model modelType "58(3)" enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.technical.professional.ability">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.technical.professional.ability", props)}</div>
            <div class="chapter-content">
                <label class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</label>
                <@selectionCriteriaContent model modelType "58(4)" enProps props version/>
            </div>
        <#elseif chapter == "espd.part4.quality.assurance">
            <div class="chapter-headline">${pr.getESPDProperty("espd.part4.quality.assurance", props)}</div>
            <div class="chapter-content">
                <label class="selectionChaptersInfo">${pr.getESPDProperty("espd.crit.selection.economic.financial.standing.article.tooltip", props)}</label>
                <@selectionCriteriaContent model modelType "62(2)" enProps props version/>
            </div>
        </#if>
    </div>
</#macro>

<#macro selectionCriteriaContent model modelType article enProps props version>
    <#assign selectionCriteria = model.getSelectionCriteriaList()>
    <#list selectionCriteria as criterion>
        <#if criterion.isSelected() == false>
            <#continue>
        </#if>
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

<#macro getSelectionAllCriterion criterion model modelType enProps props version>
    <div class="group">
        <div class="requirement">
            <#assign requirementGroups = criterion.getRequirementGroups()/>
            <#if requirementGroups??>
                <#list requirementGroups as requirementGroup>
                    <#assign descOfCriterion = pr.getESPDProperty("espd.part4.crit.selection.eo.satisfies.all.criteria", props)/>
                    <label class="descOfCriterionParagraph">${descOfCriterion}</label>
                    <div class="group">
                        <#list requirementGroup.getRequirements() as requirement>
                            <div class="requirement">
                                <#assign genericDescription = pr.getESPDProperty("espd.crit.your.answer", props)/>
                                <label class="descriptionLabel">${genericDescription}</label>
                                <label class="yesNoIndicatorLabel"><span class="icon-label"><i class="fa fa-dot-circle-o" aria-hidden="true"></i>${yesNo(criterion.isSelected(), props)}</span></label>
                            </div>
                        </#list>
                    </div>
                </#list>
            </#if>
        </div>
    </div>
</#macro>

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
