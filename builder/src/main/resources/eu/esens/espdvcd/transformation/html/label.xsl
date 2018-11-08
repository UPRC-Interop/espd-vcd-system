<!-- Definiert die Labels bzw. Label und Text-Kombinationen -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="label">
        <xsl:param name="label"/>
        <xsl:call-template name="labelAndTextValue">
            <xsl:with-param name="label" select="$label"/>
            <xsl:with-param name="value"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="labelAndCopyOfValue">
        <xsl:param name="label"/>
        <xsl:param name="value"/>
        <label>
            <xsl:value-of select="$label"/>
        </label>
        <xsl:copy-of select="$value"/>
    </xsl:template>

    <xsl:template name="valueAndText">
        <xsl:param name="value"/>
        <xsl:param name="text"/>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value" select="$value"/>
        </xsl:call-template>
        <xsl:copy-of select="$text"/>
    </xsl:template>

    <xsl:template name="labelAndTextValue">
        <xsl:param name="label"/>
        <xsl:param name="value"/>
        <label>
            <xsl:value-of select="$label"/>
        </label>
        <p>
            <xsl:value-of select="$value"/>
        </p>
    </xsl:template>

    <xsl:template name="paragraph">
        <xsl:param name="value"/>
        <p>
            <xsl:copy-of select="$value"/>
        </p>
    </xsl:template>

</xsl:stylesheet>