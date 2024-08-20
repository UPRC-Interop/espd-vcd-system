<#import '../base/procedure.ftlh' as proc>
<#import '../base/selection.ftlh' as s>
<#import '../base/exclusion.ftlh' as e>
<#import '../base/finish.ftlh' as f>
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro header props fontAwesomeCssPath>
    <head>
        <#--        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">-->
        <link rel="stylesheet" href="${fontAwesomeCssPath}">
        <style media="screen, print" type="text/css">
            <#include '../css/espd-details-print.css'>
        </style>
        <title>${pr.getESPDProperty("espd.gui.app.title",props)}</title>
    </head>
</#macro>

<#macro page espdModel espdModelType enProps props version logo>
    <body>
        <#assign title = pr.getESPDProperty("espd.gui.app.title",props)>
        <div class="espd-details eu-form">
            <div class="procedure-header">
                <div class="footer-content">
                    ${title}
                </div>
                <div class="image-content">
                    <img src="data:image/png;base64,${logo}" alt="Logo" width="300px">
                </div>
            </div>
            <@proc.procedureSection espdModel espdModelType enProps props version/>
            <@e.exclusionSection espdModel espdModelType enProps props version/>
            <@s.selectionSection espdModel espdModelType enProps props version/>
            <@f.finishSection espdModel espdModelType enProps props version/>
        </div>
    </body>
</#macro>
