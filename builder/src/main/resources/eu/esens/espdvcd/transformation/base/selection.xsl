<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="selectionSection">
        <xsl:call-template name="section">
            <xsl:with-param name="title">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part4.header'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part4.all.selection.switch'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:call-template name="label">
                            <xsl:with-param name="label">
                                <xsl:call-template name="getESPDProperty">
                                    <xsl:with-param name="key" select="'espd.part4.crit.selection.ca.declares.that'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>

                        <xsl:call-template name="tenderingCriterionSection">
                            <xsl:with-param name="article"  select="'58(2)'"/>
                        </xsl:call-template>

                        <xsl:call-template name="tenderingCriterionSection">
                            <xsl:with-param name="article"  select="'58(3)'"/>
                        </xsl:call-template>

                        <xsl:call-template name="tenderingCriterionSection">
                            <xsl:with-param name="article"  select="'58(4)'"/>
                        </xsl:call-template>

                        <xsl:call-template name="tenderingCriterionSection">
                            <xsl:with-param name="article"  select="'62(2)'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>

            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

</xsl:stylesheet>