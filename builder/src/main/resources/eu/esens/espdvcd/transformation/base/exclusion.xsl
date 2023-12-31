<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:cbcV1="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:ccvV1="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1"
                xmlns:ccv-cbcV1="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1">

    <xsl:import href="tender_criterion.xsl"/>
    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>

    <xsl:template name="exclusionSection">
        <xsl:call-template name="section">
            <xsl:with-param name="title">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part3.header'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="content">

                <!-- Chapter A -->
                <xsl:variable name="isChapterAExist">
                    <xsl:call-template name="searchForTenderingCriterionSectionByArticle">
                        <xsl:with-param name="article"  select="'57(1)'"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:if test="$isChapterAExist != ''">
                    <xsl:call-template name="chapterOne">
                        <xsl:with-param name="chapter-number"/>
                        <xsl:with-param name="chapter-headline">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part3.crit.grounds.criminal.conv.top.title'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                        <xsl:with-param name="chapter-content">
                            <xsl:call-template name="label">
                                <xsl:with-param name="label">
                                    <xsl:call-template name="getESPDProperty">
                                        <xsl:with-param name="key" select="'espd.part3.crit.eu.grounds.criminal.conv.main.title'"/>
                                    </xsl:call-template>
                                </xsl:with-param>
                            </xsl:call-template>

                            <xsl:call-template name="tenderingCriterionSection">
                                <xsl:with-param name="article"  select="'57(1)'"/>
                            </xsl:call-template>

                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>

                <!-- Chapter B -->
                <xsl:variable name="isChapterBExist">
                    <xsl:call-template name="searchForTenderingCriterionSectionByArticle">
                        <xsl:with-param name="article"  select="'57(2)'"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:if test="$isChapterBExist != ''">
                    <xsl:call-template name="chapterOne">
                        <xsl:with-param name="chapter-number"/>
                        <xsl:with-param name="chapter-headline">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part3.crit.top.grounds.payment.taxes.title'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                        <xsl:with-param name="chapter-content">
                            <xsl:call-template name="label">
                                <xsl:with-param name="label">
                                    <xsl:call-template name="getESPDProperty">
                                        <xsl:with-param name="key" select="'espd.part3.crit.eu.payment.taxes.main.title'"/>
                                    </xsl:call-template>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:call-template name="tenderingCriterionSection">
                                <xsl:with-param name="article"  select="'57(2)'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>

                <!-- Chapter C -->
                <xsl:variable name="isChapterCExist">
                    <xsl:call-template name="searchForTenderingCriterionSectionByArticle">
                        <xsl:with-param name="article"  select="'57(4)'"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:if test="$isChapterCExist != ''">
                    <xsl:call-template name="chapterOne">
                        <xsl:with-param name="chapter-number"/>
                        <xsl:with-param name="chapter-headline">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part3.crit.insolvency.conflicts.top.title'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                        <xsl:with-param name="chapter-content">
                            <xsl:call-template name="label">
                                <xsl:with-param name="label">
                                    <xsl:call-template name="getESPDProperty">
                                        <xsl:with-param name="key" select="'espd.part3.crit.eu.main.breaching.obligations'"/>
                                    </xsl:call-template>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:call-template name="tenderingCriterionSection">
                                <xsl:with-param name="article"  select="'57(4)'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>

                <!-- Chapter D -->
                <xsl:variable name="isChapterDExist">
                    <xsl:call-template name="searchForPurelyNationalExclusionGrounds"/>
                </xsl:variable>
                <xsl:if test="$isChapterDExist != ''">
                    <xsl:call-template name="chapterOne">
                        <xsl:with-param name="chapter-number" />
                        <xsl:with-param name="chapter-headline">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part3.crit.purely.national.top.title'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                        <xsl:with-param name="chapter-content">
                            <xsl:call-template name="label">
                                <xsl:with-param name="label">
                                    <xsl:call-template name="getESPDProperty">
                                        <xsl:with-param name="key" select="'espd.part3.crit.eu.main.purely.national'"/>
                                    </xsl:call-template>
                                </xsl:with-param>
                            </xsl:call-template>
                            <xsl:call-template name="purelyNationalExclusionGrounds" />
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>

            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="searchForPurelyNationalExclusionGrounds">
        <xsl:for-each select="//cac:TenderingCriterion | //ccvV1:Criterion">
            <xsl:variable name="currentArticle">
                <xsl:value-of select="current()/cac:Legislation/cbc:Article | current()/ccvV1:LegislationReference/ccv-cbcV1:Article"/>
            </xsl:variable>
            <xsl:variable name="criterionTypeCode" >
                <xsl:value-of select="current()/cbc:CriterionTypeCode | current()/cbcV1:TypeCode"/>
            </xsl:variable>
            <xsl:if test="$criterionTypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER'">
                <xsl:value-of select="current()"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="purelyNationalExclusionGrounds">
        <xsl:for-each select="//cac:TenderingCriterion | //ccvV1:Criterion">
            <xsl:variable name="currentArticle">
                <xsl:value-of select="current()/cac:Legislation/cbc:Article | current()/ccvV1:LegislationReference/ccv-cbcV1:Article"/>
            </xsl:variable>
            <xsl:variable name="criterionTypeCode" >
                <xsl:value-of select="current()/cbc:CriterionTypeCode | current()/cbcV1:TypeCode"/>
            </xsl:variable>
            <xsl:if test="$criterionTypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER'">
                <xsl:apply-templates select="current()"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>