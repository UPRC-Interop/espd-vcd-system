<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Datum-->
    <xsl:template name="formatDate">
        <xsl:param name="isoDate"/>
        <xsl:variable name="year" select="substring($isoDate,1,4)"/>
        <xsl:variable name="month" select="substring($isoDate,6,2)"/>
        <xsl:variable name="day" select="substring($isoDate,9,2)"/>

        <xsl:value-of select="concat($day,'.',$month,'.',$year)"/>
    </xsl:template>

    <!-- Ortszeit -->
    <xsl:template name="formatTime">
        <xsl:param name="isoDate"/>
        <xsl:variable name="hour" select="substring($isoDate,12,2)"/>
        <xsl:variable name="minute" select="substring($isoDate,15,2)"/>
        <xsl:value-of select="concat($hour,':',$minute)"/> Uhr
    </xsl:template>

    <!-- Datum mit Ortszeit-->
    <xsl:template name="formatDateTime">
        <xsl:param name="isoDate"/>
        <xsl:call-template name="formatDate">
            <xsl:with-param name="isoDate" select="$isoDate"/>
        </xsl:call-template>
        -
        <xsl:call-template name="formatTime">
            <xsl:with-param name="isoDate" select="$isoDate"/>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>