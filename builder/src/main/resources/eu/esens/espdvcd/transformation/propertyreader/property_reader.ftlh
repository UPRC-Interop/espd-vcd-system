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

<#function getESPDProperty key props>
    <#assign espdProperty = searchProperty(key,props)>
    <#return espdProperty>
</#function>

<#function searchProperty key props>
    <#list props.entry as p>
        <#if p.@key == key>
            <#return p>
        </#if>
    </#list>
    <#return ''>
</#function>

<#function searchKey value props>
    <#list props.entry as p>
        <#assign value = value?string>
        <#if value?length &gt; 30>
            <#if p?string?contains(value?substring(0,30)) || p?string?contains(value?lower_case?substring(0,30))>
                <#return p.@key>
            </#if>
        <#else>
            <#if p?string?contains(value)>
                <#return p.@key>
            </#if>
        </#if>
    </#list>
    <#return ''>
</#function>

<#function getESPDPropertyTranslationKey value enProps>
    <#assign key = searchKey(value,enProps)>
    <#return key>
</#function>

