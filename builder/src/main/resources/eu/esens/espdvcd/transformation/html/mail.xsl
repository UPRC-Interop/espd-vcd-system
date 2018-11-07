<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="formatMail">
        <xsl:param name="e-mail"/>
        <xsl:element name="a">
            <xsl:attribute name="href">mailto:<xsl:value-of select="$e-mail"/>
            </xsl:attribute>
            <xsl:value-of select="$e-mail"/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>