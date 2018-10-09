<?xml version="1.0" encoding="UTF-8"?>
<!-- Uebergreifende Strukturen fuer die Transformationen (sowogl EU- als auch NAT-Verfahren -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:import href="chapter.xsl"/>
    <xsl:import href="label.xsl"/>
    <xsl:import href="url.xsl"/>
    <xsl:import href="date.xsl"/>
    <xsl:import href="mail.xsl"/>

    <!-- Währungsformatierungs-Variable -->
    <xsl:variable name="currencyPattern" select="'#.##0,00'"/>
    <!-- Das decimal-format european tauscht '.' und ',' als Tausendertrennzeichen
    Das decimal-format muss jetzt aber beim Formatieren immer explizit mit angegeben werden -->
    <xsl:decimal-format name="european" decimal-separator=',' grouping-separator='.'/>

    <!-- Diese beiden folgenden Templates entfernen die Zeilenumbrüche aus der XML -->
    <xsl:template match="*/text()[normalize-space()]">
        <xsl:value-of select="normalize-space()"/>
    </xsl:template>

    <xsl:template match="*/text()[not(normalize-space())]"/>

    <!-- Abschnitt -->
    <xsl:template name="section">
        <xsl:param name="title"/>
        <xsl:param name="content"/>
        <xsl:element name="div">
            <xsl:attribute name="class">section</xsl:attribute>
            <xsl:if test="$title">
                <xsl:element name="h3">
                    <xsl:value-of select="$title"/>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$content">
                <xsl:copy-of select="$content"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template name="sectionLabel">
        <xsl:param name="label"/>
        <xsl:param name="text"/>
        <xsl:element name="div">
            <span class="content-left">
                <xsl:copy-of select="$label"/>
            </span>
            <xsl:choose>
                <xsl:when test="string-length($text)> 56">
                    <span class="content-long">
                        <xsl:copy-of select="$text"/>
                    </span>
                </xsl:when>
                <xsl:otherwise>
                    <span class="content-right">
                        <xsl:copy-of select="$text"/>
                    </span>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:element>
    </xsl:template>

    <xsl:template name="leftPadding">
        <xsl:param name="content"/>
        <div class="left-padding">
            <xsl:copy-of select="$content"/>
        </div>
    </xsl:template>

</xsl:stylesheet>