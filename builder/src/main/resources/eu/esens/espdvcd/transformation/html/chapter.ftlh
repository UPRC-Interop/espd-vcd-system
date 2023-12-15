<#macro chapterStructure chapter_number chapter_headline>
    <#if chapter_number??>
        <div class="chapter-number">
            ${chapter_number}
        </div>
    </#if>
    <#if chapter_headline??>
        <div class="chapter-headline">
            ${chapter_headline}
        </div>
    </#if>
</#macro>

<#function getChapterString chapter_number>
    <#if !chapter_number??>
        <#return 'no-chapter-number'>
    <#else>
        <#return ''>
    </#if>
</#function>

<#function getLevelString level>
    <#switch level>
        <#case 2>
            <#return 'level-two'>
        <#case 3>
            <#return 'level-three'>
        <#case 4>
            <#return 'level-four'>
    </#switch>
    <#return ''>
</#function>