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

<#import '../base/tender_criterion.ftlh' as tc>
<#import '../html/chapter.ftlh' as c>
<#import '../propertyreader/property_reader.ftlh' as pr>


<#macro exclusionSection model modelType enProps props version>
    <#assign criminalTopTitle = pr.getESPDProperty('espd.part3.crit.grounds.criminal.conv.top.title',props)>
    <#assign criminalMainTitle = pr.getESPDProperty('espd.part3.crit.eu.grounds.criminal.conv.main.title',props)>
    <#assign paymentTaxesTitle = pr.getESPDProperty('espd.part3.crit.top.grounds.payment.taxes.title',props)>
    <#assign taxesMainTitle = pr.getESPDProperty('espd.part3.crit.eu.payment.taxes.main.title',props)>
    <#assign conflictsTopTitle = pr.getESPDProperty('espd.part3.crit.insolvency.conflicts.top.title',props)>
    <#assign breachingObligations = pr.getESPDProperty('espd.part3.crit.eu.main.breaching.obligations',props)>
    <#assign nationalTopTile = pr.getESPDProperty('espd.part3.crit.purely.national.top.title',props)>

    <div class="section">
        <h3>${pr.getESPDProperty("espd.part3.header",props)}</h3>
        <@exclusionChapter 1 '' criminalTopTitle criminalMainTitle model modelType '57(1)' enProps props version/>
        <@exclusionChapter 1 '' paymentTaxesTitle taxesMainTitle model modelType '57(2)' enProps props version/>
        <@exclusionChapter 1 '' conflictsTopTitle breachingObligations model modelType '57(4)' enProps props version/>
        <@exclusionChapter 1 '' nationalTopTile '' model modelType '' enProps props version/>
    </div>
</#macro>

<#macro exclusionChapter level chapter_number chapter_headline chapter_content model modelType article enProps props version>
    <#assign levelString = c.getLevelString(level)>
    <#assign chapterString = c.getChapterString(chapter_number)>
    <div class="chapter ${levelString} ${chapterString}">
        <@c.chapterStructure chapter_number chapter_headline />
        <#if chapter_content??>
            <div class="chapter-content">
                <label>${chapter_content}</label>
                <#if article == ''>
                    <@purelyNationalExclusionGrounds model modelType enProps props version/>
                <#else>
                    <@tenderingCriteria model modelType article enProps props version/>
                </#if>
            </div>
        </#if>
    </div>
</#macro>

<#macro tenderingCriteria model modelType article enProps props version>
    <#assign exclusionCriteria = model.getExclusionCriteriaList()>
    <#list exclusionCriteria as criterion>
        <#if criterion.getLegislationReference()?? && criterion.getTypeCode()??>
            <#if criterion.getLegislationReference().getArticle()?contains(article?string) && criterion.getTypeCode() != "CRITERION.EXCLUSION.NATIONAL.OTHER">
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


<#macro purelyNationalExclusionGrounds model modelType enProps props version>
    <#assign exclusionCriteria = model.getExclusionCriteriaList()>
    <#list exclusionCriteria as criterion>
        <#if criterion.getLegislationReference()?? && criterion.getTypeCode()??>
            <#if criterion.getLegislationReference().getArticle() == "Article 57(3)" && criterion.getTypeCode() == "CRITERION.EXCLUSION.NATIONAL.OTHER">
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
