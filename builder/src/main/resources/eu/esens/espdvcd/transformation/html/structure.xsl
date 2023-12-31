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

    <xsl:template name="sectionLabelForNode">
        <xsl:param name="value" />
        <xsl:param name="propertyKey" />
        <xsl:variable name="propertyValue">
            <xsl:call-template name="getESPDProperty">
                <xsl:with-param name="key" select="$propertyKey"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:if test="$value">
            <xsl:call-template name="sectionLabel">
                <xsl:with-param name="label">
                    <!--asd: -->
                    <xsl:value-of select="$propertyValue"/><xsl:if test="not(':' =substring($propertyValue, string-length($propertyValue)))">:</xsl:if>
                </xsl:with-param>
                <xsl:with-param name="text">
                    <xsl:copy-of select="$value"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="sectionLabelForValueOfNode">
        <xsl:param name="node" />
        <xsl:param name="propertyKey" />
        <xsl:call-template name="sectionLabelForNode">
            <xsl:with-param name="propertyKey" select="$propertyKey"/>
            <xsl:with-param name="value">
                <xsl:value-of select="$node"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="sectionLabelForMailValueOfNode">
        <xsl:param name="node" />
        <xsl:param name="propertyKey" />
        <xsl:call-template name="sectionLabelForNode">
            <xsl:with-param name="propertyKey" select="$propertyKey"/>
            <xsl:with-param name="value">
                <xsl:call-template name="formatMail">
                    <xsl:with-param name="e-mail">
                        <xsl:value-of select="$node"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="sectionLabelForUrlValueOfNode">
        <xsl:param name="node" />
        <xsl:param name="propertyKey" />
        <xsl:call-template name="sectionLabelForNode">
            <xsl:with-param name="propertyKey" select="$propertyKey"/>
            <xsl:with-param name="value">
                <xsl:call-template name="formatUrl">
                    <xsl:with-param name="url">
                        <xsl:value-of select="$node"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="leftPadding">
        <xsl:param name="content"/>
        <div class="left-padding">
            <xsl:copy-of select="$content"/>
        </div>
    </xsl:template>

    <xsl:template name="multiLineText">
        <xsl:param name="pText"/>

        <xsl:choose>
            <xsl:when test="contains($pText, '&lt;br&gt;')">
                <xsl:call-template name="paragraph">
                    <xsl:with-param name="value">
                        <xsl:value-of select="substring-before($pText,'&lt;br&gt;')"/>
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="multiLineText">
                    <xsl:with-param name="pText"
                                    select="substring-after($pText,'&lt;br&gt;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="paragraph">
                    <xsl:with-param name="value">
                        <xsl:value-of select="$pText"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="yesNo">
        <xsl:param name="value"/>
        <xsl:choose>
            <xsl:when test="$value = 'true'">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.yes'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$value = 'false'">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.no'"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.yes'"/>
                </xsl:call-template>
                /
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.no'"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>