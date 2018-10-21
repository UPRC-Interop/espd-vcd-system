<?xml version="1.0" encoding="UTF-8"?>

<!-- Dieses Template definiert die Layouts fuer die Pages-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

    <xsl:output method="xml" version="1.0" doctype-public="-//W3C//DTD XHTML 1.1//EN"
                doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes" encoding="UTF-8"/>

    <xsl:variable name="espdDetailsCss" select="document('../css/espd-details.css')"/>
    <xsl:variable name="espdDetailsNoticeCss" select="document('../css/espd-details-notice.css')"/>
    <xsl:variable name="printCss" select="document('../css/espd-details-print.css')"/>

    <!-- Definition für normale HTML-Seite -->
    <xsl:template name="page">
        <xsl:param name="procedureHeader"/>
        <xsl:param name="content"/>
        <xsl:param name="type"/>
        <xsl:call-template name="htmlDefinition">
            <xsl:with-param name="type" select="$type"/>
            <xsl:with-param name="procedureHeader" select="$procedureHeader"/>
            <xsl:with-param name="content" select="$content"/>
        </xsl:call-template>
    </xsl:template>

    <!-- HTML Definitionen -->
    <xsl:template name="htmlDefinition">
        <xsl:param name="type"/>
        <xsl:param name="procedureHeader"/>
        <xsl:param name="content"/>
        <html lang="de"
        >
            <xsl:call-template name="head">
                <xsl:with-param name="type" select="$type"/>
            </xsl:call-template>

            <xsl:call-template name="body">
                <xsl:with-param name="procedureHeader" select="$procedureHeader"/>
                <xsl:with-param name="content" select="$content"/>
            </xsl:call-template>
        </html>
    </xsl:template>

    <!-- Die Body-Definition-->
    <xsl:template name="body">
        <xsl:param name="procedureHeader"/>
        <xsl:param name="content"/>

        <xsl:element name="body">

                    <xsl:element name="div">
                        <xsl:attribute name="class">espd-details eu-form</xsl:attribute>
                        <xsl:call-template name="procedureHeader">
                            <xsl:with-param name="content" select="$procedureHeader"/>
                        </xsl:call-template>
                        <xsl:copy-of select="$content"/>
                    </xsl:element>
        </xsl:element>

    </xsl:template>

    <xsl:template name="panelBody">
        <xsl:param name="procedureHeader"/>
        <xsl:param name="content"/>

        <xsl:element name="div">
            <xsl:attribute name="class">panel-body eu-form</xsl:attribute>
            <xsl:call-template name="procedureHeader">
                <xsl:with-param name="content" select="$procedureHeader"/>
            </xsl:call-template>

            <xsl:copy-of select="$content"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="head">
        <xsl:param name="type"/>
        <xsl:element name="head">
            <xsl:call-template name="metaDefinition"/>
            <xsl:call-template name="cssDefinition" />

            <xsl:element name="title">
                <xsl:value-of select="$type"/>
            </xsl:element>

        </xsl:element>
    </xsl:template>

    <!-- Import der CSS-Dateien -->
    <xsl:template name="cssDefinition">
        <xsl:element name="style">
            <xsl:attribute name="type">
                <xsl:text>text/css</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="media">
                <xsl:text>screen, print</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="$espdDetailsCss" disable-output-escaping="yes"/>
        </xsl:element>

        <xsl:element name="style">
            <xsl:attribute name="type">
                <xsl:text>text/css</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="media">
                <xsl:text>screen, print</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="$espdDetailsNoticeCss" disable-output-escaping="yes"/>
        </xsl:element>


        <xsl:element name="style">
            <xsl:attribute name="type">
                <xsl:text>text/css</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="media">
                <xsl:text>print</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="$printCss" disable-output-escaping="yes"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="metaDefinition">
        <xsl:element name="meta">
            <xsl:attribute name="http-equiv">
                <xsl:text>content-type</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="content">
                <xsl:text>text/html</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="charset">
                <xsl:text>UTF-8</xsl:text>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!-- Definiert den linksbündigen Footer-->
    <xsl:template name="footerContent">
        <xsl:element name="div">
            <xsl:attribute name="class">footer-content</xsl:attribute>
            <xsl:call-template name="getFooterContent"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getFooterContent">
        {{Hier kommt die Footerbeschriftung rein}}
    </xsl:template>

    <!-- Definiert den ProcedureHeader-->
    <xsl:template name="procedureHeader">
        <xsl:param name="content"/>
        <xsl:element name="div">
            <xsl:attribute name="class">procedure-header</xsl:attribute>
            <xsl:call-template name="footerContent"/>
            <xsl:copy-of select="$content"/>
        </xsl:element>
    </xsl:template>


</xsl:stylesheet>
