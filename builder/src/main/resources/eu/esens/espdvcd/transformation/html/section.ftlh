<#import '../html/url.ftlh' as u>
<#import '../html/mail.ftlh' as m>
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro sectionLabelForMailValueOfNode text value props>
    <#assign propertyValue = pr.getESPDProperty(value,props)>
    <#if value??>
        <#if !propertyValue?contains(":")>
            <#assign propertyValue += ":">
            <@sectionLabelForMail propertyValue text />
        <#else>
            <@sectionLabelForMail propertyValue text />
        </#if>
    </#if>
</#macro>

<#macro sectionLabelForUrlValueOfNode text value props>
    <#assign propertyValue = pr.getESPDProperty(value,props)>
    <#if value??>
        <#if !propertyValue?contains(":")>
            <#assign propertyValue += ":">
            <@sectionLabelForUrl propertyValue text />
        <#else>
            <@sectionLabelForUrl propertyValue text />
        </#if>
    </#if>
</#macro>

<#macro sectionLabelForValueOfNode text value props>
    <#assign propertyValue = pr.getESPDProperty(value,props)>
    <#if value??>
        <#if !propertyValue?contains(":")>
            <#assign propertyValue += ":">
            <@sectionLabel propertyValue text />
        <#else>
            <@sectionLabel propertyValue text />
        </#if>
    </#if>
</#macro>

<#macro sectionLabel label text="">
    <#if text?length gt 56>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-long">${text}</span>
        </div>
    <#else>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-right">${text}</span>
        </div>
    </#if>
</#macro>

<#macro sectionLabelForUrl label text="">
    <#if text?length gt 56>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-long"><@u.formatUrl text/></span>
        </div>
    <#else>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-right"><@u.formatUrl text/></span>
        </div>
    </#if>
</#macro>

<#macro sectionLabelForMail label text="">
    <#if text?length gt 56>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-long"><@m.formatMail text/></span>
        </div>
    <#else>
        <div>
            <span class="content-left">${label}</span>
            <span class="content-right"><@m.formatMail text/></span>
        </div>
    </#if>
</#macro>
