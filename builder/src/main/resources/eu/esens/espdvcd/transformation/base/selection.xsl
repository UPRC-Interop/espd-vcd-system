<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="selectionSection">


        <xsl:variable name="alphaCriterion" >
            <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.ALL'"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:choose>

            <xsl:when test="$alphaCriterion != ''">

                <!-- SECTION A START -->
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
                                <!-- alpha criterion -->
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.part4.crit.selection.ca.declares.that'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.part4.crit.selection.satisfies.all.criteria'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="getESPDProperty">
                                            <xsl:with-param name="key" select="'espd.crit.your.answer'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:call-template name="label">
                                    <xsl:with-param name="label">
                                        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                            <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.ALL'"/>
                                        </xsl:call-template>
                                    </xsl:with-param>
                                </xsl:call-template>
                            </xsl:with-param>

                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
                <!-- SECTION A END -->

            </xsl:when>
            <xsl:otherwise>

                <!-- SECTION B START -->
                <xsl:call-template name="section">
                    <xsl:with-param name="title">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part4.header'"/>
                        </xsl:call-template>
                    </xsl:with-param>

                    <xsl:with-param name="content">
                        <xsl:call-template name="chapterOne">
                            <xsl:with-param name="chapter-number"/>
                            <xsl:with-param name="chapter-headline"/>

                            <xsl:with-param name="chapter-content">

                                <!-- Chapter A : Suitability -->
                                <xsl:variable name="isChapterAExist">
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.SUITABILITY.'"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:if test="$isChapterAExist != ''">
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
                                </xsl:if>

                                <!-- Chapter B : Economic and Financial Standing -->
                                <xsl:variable name="isChapterBExist">
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.'"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:if test="$isChapterBExist != ''">
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
                                </xsl:if>

                                <!-- Chapter C: Technical and Professional Ability -->
                                <xsl:variable name="isChapterCExist">
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.'"/>
                                    </xsl:call-template>
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.'"/>
                                    </xsl:call-template>
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.'"/>
                                    </xsl:call-template>
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE'"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:if test="$isChapterCExist != ''">
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
                                            <!-- GES-111 -->
                                            <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                                <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE'"/>
                                            </xsl:call-template>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:if>

                                <!-- Chapter D: Certificate -->
                                <xsl:variable name="isChapterDExist">
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE'"/>
                                    </xsl:call-template>
                                    <xsl:call-template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
                                        <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE'"/>
                                    </xsl:call-template>
                                </xsl:variable>
                                <xsl:if test="$isChapterDExist != ''">
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
                                            <!-- GES-111 -->
                                            <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                                <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE'"/>
                                            </xsl:call-template>
                                            <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                                                <xsl:with-param name="criterionTypeCode"  select="'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE'"/>
                                            </xsl:call-template>
                                        </xsl:with-param>
                                    </xsl:call-template>
                                </xsl:if>

                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
                <!-- SECTION B END -->

            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
</xsl:stylesheet>