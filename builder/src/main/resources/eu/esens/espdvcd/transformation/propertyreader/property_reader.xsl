<?xml version="1.0" encoding="UTF-8"?>
<!-- Kann die Properties lesen. -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:variable name="language"/>

    <xsl:template name="getESPDProperty">
        <xsl:param name="key"/>

        <xsl:call-template name="searchProperty">
            <xsl:with-param name="key" select="$key"/>
            <xsl:with-param name="prop" select="document(concat('../properties/ESPD_',$language,'.xml'))"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="searchProperty">
        <xsl:param name="key"/>
        <xsl:param name="prop"/>
        <xsl:value-of select="normalize-space($prop/*/entry[@key = normalize-space($key)]/text())"/>
    </xsl:template>

    <xsl:template name="getESPDPropertyTranslationKey">
        <xsl:param name="value"/>

        <xsl:call-template name="searchKey">
            <xsl:with-param name="value" select="$value"/>
            <xsl:with-param name="prop" select="document('../properties/ESPD_en.xml')"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="searchKey">
        <xsl:param name="value"/>
        <xsl:param name="prop"/>
        <xsl:value-of select="normalize-space($prop/*/entry[text() = normalize-space($value)]/@key)"/>
    </xsl:template>

</xsl:stylesheet>
