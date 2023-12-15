<#import '../base/procedure.ftlh' as proc>
<#import '../base/selection.ftlh' as s>
<#import '../base/exclusion.ftlh' as e>
<#import '../base/finish.ftlh' as f>
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro page espdModel espdModelType enProps props version>
    <body>
        <#assign title = pr.getESPDProperty('espd.gui.app.title',props)>
        <div class="espd-details eu-form">
            <div class="procedure-header">
                <div class="footer-content">
                    ${title}
                </div>
                <h2>${title}</h2>
            </div>
            <@proc.procedureSection espdModel espdModelType enProps props version/>
            <@e.exclusionSection espdModel espdModelType enProps props version/>
            <@s.selectionSection espdModel espdModelType enProps props version/>
            <@f.finishSection espdModel espdModelType enProps props version/>
        </div>
    </body>
</#macro>

<#macro header props>
    <head>
        <style media="screen, print" type="text/css">
            <#include '../css/espd-details.css'>
        </style>
        <style media="screen, print" type="text/css">
            <#include '../css/espd-details-notice.css'>
        </style>
        <style media="print" type="text/css">
            <#include '../css/espd-details-print.css'>
        </style>
        <#assign title = pr.getESPDProperty("espd.gui.app.title",props)/>
        <title>${title}</title>
    </head>
</#macro>