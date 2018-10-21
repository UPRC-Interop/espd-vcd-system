<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:qar="urn:X-test:UBL:Pre-award:QualificationApplicationRequest" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate">


    <xsl:import href="html/page.xsl"/>
    <xsl:import href="procedure.xsl"/>
    <xsl:import href="exclusion.xsl"/>
    <xsl:import href="selection.xsl"/>
    <xsl:import href="finish.xsl"/>
    <xsl:import href="propertyreader/property_reader.xsl"/>


    <xsl:variable name="language" select="'de'"/>

    <xsl:template match="/">
        <xsl:call-template name="page">
            <xsl:with-param name="type">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.app.title'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="procedureHeader">
                <h2>
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.gui.app.title'"/>
                    </xsl:call-template>
                </h2>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:call-template name="procedureSection"/>
                <xsl:call-template name="exclusionSection"/>
                <xsl:call-template name="selectionSection"/>
                <xsl:call-template name="finishSection"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>


</xsl:stylesheet>