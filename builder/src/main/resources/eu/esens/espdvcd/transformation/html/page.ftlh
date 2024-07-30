<#import '../base/procedure.ftlh' as proc>
<#import '../base/selection.ftlh' as s>
<#import '../base/exclusion.ftlh' as e>
<#import '../base/finish.ftlh' as f>
<#import '../propertyreader/property_reader.ftlh' as pr>

<#macro page espdModel espdModelType enProps props version logo>
    <body>
        <#assign title = pr.getESPDProperty('espd.gui.app.title',props)>
        <div class="espd-details eu-form">
            <div class="procedure-header">
                <div class="footer-content">
                    ${title}
                </div>
                <div class="image-content">
                    <img src="data:image/png;base64,${logo}" alt="Logo" width="300px">
                </div>
            </div>
            <div style="background-color: crimson"><i class="fa fa-dot-circle-o" aria-hidden="true"></i></div>
            <@proc.procedureSection espdModel espdModelType enProps props version/>
            <@e.exclusionSection espdModel espdModelType enProps props version/>
            <@s.selectionSection espdModel espdModelType enProps props version/>
            <@f.finishSection espdModel espdModelType enProps props version/>
        </div>
    </body>
</#macro>

<#macro header props fontAwesomeCssPath>
    <head>
        <link rel="stylesheet" href="${fontAwesomeCssPath}">
<#--        <style>-->
<#--            @font-face {-->
<#--                font-family: 'FontAwesome';-->
<#--                src: url('${faCssFont}?v=4.7.0');-->
<#--                font-weight: normal;-->
<#--                font-style: normal;-->
<#--            }-->
<#--        </style>-->
        <style media="screen, print" type="text/css">
            <#include '../css/espd-details-print.css'>
        </style>
        <#assign title = pr.getESPDProperty("espd.gui.app.title",props)/>
        <title>${title}</title>
    </head>
</#macro>
