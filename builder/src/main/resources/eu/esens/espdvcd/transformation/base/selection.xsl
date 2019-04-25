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

                        <!-- Chapter A : Suitability -->
                        <xsl:call-template name="chapterOne">
                            <xsl:with-param name="chapter-number"/>
                            <xsl:with-param name="chapter-headline">
                                <xsl:call-template name="getESPDProperty">
                                    <xsl:with-param name="key" select="'espd.part4.suitability'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                            <xsl:with-param name="chapter-content">
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.crit.selection.economic.financial.standing.article.tooltip'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.SUITABILITY.'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>

                        <!-- Chapter B : Economic and Financial Standing -->
                        <xsl:call-template name="chapterOne">
                            <xsl:with-param name="chapter-number"/>
                            <xsl:with-param name="chapter-headline">
                                <xsl:call-template name="getESPDProperty">
                                    <xsl:with-param name="key" select="'espd.part4.economic.and.financial.standing'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                            <xsl:with-param name="chapter-content">
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.part4.crit.selection.eo.economic.article'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>

                        <!-- Chapter C: Technical and Professional Ability -->
                        <xsl:call-template name="chapterOne">
                            <xsl:with-param name="chapter-number"/>
                            <xsl:with-param name="chapter-headline">
                                <xsl:call-template name="getESPDProperty">
                                    <xsl:with-param name="key" select="'espd.part4.technical.professional.ability'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                            <xsl:with-param name="chapter-content">
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.crit.selection.economic.financial.standing.article.tooltip'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.'"/>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.'"/>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>

                        <!-- Chapter D: Certificate -->
                        <xsl:call-template name="chapterOne">
                            <xsl:with-param name="chapter-number"/>
                            <xsl:with-param name="chapter-headline">
                                <xsl:call-template name="getESPDProperty">
                                    <xsl:with-param name="key" select="'espd.part4.quality.assurance'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                            <xsl:with-param name="chapter-content">
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.part4.crit.selection.quality.assurance.article'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                    <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.'"/>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>

                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>