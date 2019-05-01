<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:cacV1="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2">

    <xsl:import href="../procedure/part_one.xsl"/>
    <xsl:import href="../procedure/part_two.xsl"/>
    <xsl:import href="../procedure/part_two_for_request.xsl"/>
    <xsl:import href="../html/chapter.xsl"/>

    <xsl:template name="procedureSection">
        <xsl:call-template name="partOne"/>

        <xsl:if test=".//*[local-name() = 'EconomicOperatorParty']">
            <xsl:call-template name="partTwo"/>
        </xsl:if>

        <xsl:choose>
            <xsl:when test=".//*[local-name() = 'EconomicOperatorParty']">
                <xsl:call-template name="partTwo"/>
            </xsl:when>
            <xsl:otherwise>
                <!-- workaround in order to apply blank eo part II in espd request PDF -->
                <xsl:call-template name="partTwoForRequest"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>