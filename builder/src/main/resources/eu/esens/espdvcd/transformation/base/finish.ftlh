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
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro finishSection model modelType enProps props version>
    <#assign title = pr.getESPDProperty("espd.part5.header",props)>
    <#if modelType == "ESPD_RESPONSE">
        <div class="section">
            <h3>${title}</h3>
            <@partFive model modelType enProps props version/>
            <@partSix props />
        </div>
    <#else>
        <div class="section">
            <h3>${title}</h3>
            <@partSix props />
        </div>
    </#if>
</#macro>

<#macro reductionOfCandidatesPart model modelType enProps props version>
    <#if version == "V1">
        <#assign lstRc = model.getFullCriterionList()>
        <@reductionOfCandidatesContent lstRc model modelType enProps props version />
    <#elseif version == "V2">
        <#assign lstRc = model.getEORelatedCriteriaList()>
        <@reductionOfCandidatesContent lstRc model modelType enProps props version />
    </#if>
</#macro>

<#macro reductionOfCandidatesContent lstRc model modelType enProps props version>
    <#list lstRc as criterion>
        <#if criterion.getTypeCode()?string?contains('CRITERION.OTHER.EO_DATA.REDUCTION_OF_CANDIDATES') || criterion.getTypeCode()?string?contains('CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE')>
            <@tc.getTenderingCriterion criterion model modelType enProps props version/>
            <#if criterion.getSubCriterionList()??>
                <#assign subCriterionList = criterion.getSubCriterionList()/>
                <#list subCriterionList as subCriterion>
                    <@tc.getTenderingCriterion subCriterion model modelType enProps props version/>
                </#list>
            </#if>
        </#if>
    </#list>
</#macro>

<#macro partFive model modelType enProps props version>
    <#assign levelString = c.getLevelString(1)>
    <#assign chapterString = c.getChapterString('')>
    <div class="chapter ${levelString} ${chapterString}">
        <@c.chapterStructure '' pr.getESPDProperty('espd.part5.reduction',props)/>
         <div class="chapter-content">
            <p>${pr.getESPDProperty("espd.part5.toptext",props)}</p>
            <label>${pr.getESPDProperty('espd.part5.reduction.question',props)}</label>
            <@reductionOfCandidatesPart model modelType enProps props version/>
         </div>
    </div>
</#macro>

<#macro partSix props>
    <#assign levelString = c.getLevelString(1)>
    <#assign chapterString = c.getChapterString('')>
    <div class="chapter ${levelString} ${chapterString}">
        <@c.chapterStructure '' pr.getESPDProperty('espd.part5.concl.statements',props)/>
        <div class="chapter-content">
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text.part1",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text.part2",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text.part3",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text.part4",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.text.part5",props)}</p>
            <p>${pr.getESPDProperty("espd.part5.concl.statements.signature",props)}</p>
            <p>${pr.getESPDProperty("espd.crit.date",props)}</p>
            <p>${pr.getESPDProperty("espd.gui.place",props)}</p>
            <p>${pr.getESPDProperty("espd.gui.signature",props)}</p>
        </div>
    </div>
</#macro>