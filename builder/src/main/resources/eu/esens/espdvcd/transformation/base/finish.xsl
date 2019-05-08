<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
>
<xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../html/structure.xsl"/>
    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>

    <xsl:template name="finishSection">
        <xsl:call-template name="section">
            <xsl:with-param name="title">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.header'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:if test="//cac:TenderingCriterion[./cbc:CriterionTypeCode = 'CRITERION.OTHER.EO_DATA.REDUCTION_OF_CANDIDATES']">
                    <xsl:call-template name="chapterOne">
                        <xsl:with-param name="chapter-number"/>
                        <xsl:with-param name="chapter-headline">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part5.reduction'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                        <xsl:with-param name="chapter-content">
                            <xsl:call-template name="partFive"/>
                            <xsl:call-template name="label">
                                <xsl:with-param name="label">
                                    <xsl:call-template name="getESPDProperty">
                                        <xsl:with-param name="key" select="'espd.part5.reduction.question'"/>
                                    </xsl:call-template>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:apply-templates select="//cac:TenderingCriterion[./cbc:CriterionTypeCode = 'CRITERION.OTHER.EO_DATA.REDUCTION_OF_CANDIDATES']"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>

                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part5.concl.statements'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:call-template name="partSix"/>
                    </xsl:with-param>
                </xsl:call-template>

            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="partFive">
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.toptext'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="partSix">
        <xsl:call-template name="multiLineText">
            <xsl:with-param name="pText">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.text.part1'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="multiLineText">
            <xsl:with-param name="pText">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.text.part2'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="multiLineText">
            <xsl:with-param name="pText">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.text.part3'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="multiLineText">
            <xsl:with-param name="pText">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.text.part4'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="multiLineText">
            <xsl:with-param name="pText">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.text.part5'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part5.concl.statements.signature'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.date'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.place'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.signature'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>


    </xsl:template>

</xsl:stylesheet>