<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- URLs -->
    <xsl:template name="formatUrl">
        <xsl:param name="url"/>
        <xsl:choose>
            <xsl:when test="$url = 'http://dummy.evergabe-online.de'">&lt;URL wird vom System generiert&gt;
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="$url"/>
                    </xsl:attribute>
                    <xsl:attribute name="target">_blank</xsl:attribute>
                    <xsl:value-of select="$url"/>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
