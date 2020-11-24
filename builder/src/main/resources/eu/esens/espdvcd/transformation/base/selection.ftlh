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
    <#assign title = pr.getESPDProperty('espd.part4.header',props) >
    <#assign selectionSwitch = pr.getESPDProperty('espd.part4.all.selection.switch',props)>
    <div class="section">
        <h3>${title}</h3>
        <@selectionChapter 1 '' selectionSwitch model modelType enProps props version/>
    </div>
</#macro>

<#macro selectionChapter level chapter_number chapter_headline model modelType enProps props version>
     <#assign levelString = c.getLevelString(level)>
    <#assign chapterString = c.getChapterString(chapter_number)>
    <div class="chapter ${levelString} ${chapterString}">
        <@c.chapterStructure chapter_number chapter_headline />
        <div class="chapter-content">
            <label>${pr.getESPDProperty('espd.part4.crit.selection.ca.declares.that',props)}</label>
            <@selectionCriteriaContent model modelType '58(2)' enProps props version/>
            <@selectionCriteriaContent model modelType '58(3)' enProps props version/>
            <@selectionCriteriaContent model modelType '58(4)' enProps props version/>
            <@selectionCriteriaContent model modelType '62(2)' enProps props version/>
         </div>
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