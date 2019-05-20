<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>
    <xsl:import href="../base/tender_criterion.xsl"/>

    <xsl:template name="partTwoForRequest">

        <xsl:call-template name="section">
            <xsl:with-param name="title">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part2.header'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part2.info.eo'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:call-template name="dummyEO"/>
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="dummyPowerOfAttorney"/>

                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part2.info.reliance'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                            <xsl:with-param name="criterionTypeCode" select="'CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>

                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part2.information.subcontractors'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
                            <xsl:with-param name="criterionTypeCode" select="'CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="dummyPowerOfAttorney">

            <xsl:call-template name="chapterOne">
                <xsl:with-param name="chapter-number"/>
                <xsl:with-param name="chapter-headline">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.part2.info.respresent'"/>
                    </xsl:call-template>
                    #<xsl:value-of select="position()"/>
                </xsl:with-param>
                <xsl:with-param name="chapter-content">

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.first.name'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.last.name'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.birth.date'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.birth.place'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part1.street.and. num'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part1.postcode'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part1.city'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part1.country'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.telephone'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.email'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                    <xsl:call-template name="label">
                        <xsl:with-param name="label">
                            <xsl:call-template name="getESPDProperty">
                                <xsl:with-param name="key" select="'espd.part2.pos.act.in.capacity'"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>

                </xsl:with-param>
            </xsl:call-template>
    </xsl:template>

    <xsl:template name="dummyEO">

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part2.name'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.street.and. num'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.postcode'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.city'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.country'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part2.contact.person'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.email'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.telephone'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.fax'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part2.vat'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.website'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:if test="//*[local-name() = 'ProcurementProjectLot']/*[local-name() = 'ID']">
            <xsl:call-template name="labelAndTextValue">
                <xsl:with-param name="label">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.part2.lots.concerned'"/>
                    </xsl:call-template>
                </xsl:with-param>
                <xsl:with-param name="value">
                    <xsl:value-of select="//*[local-name() = 'ProcurementProjectLot']/*[local-name() = 'ID']"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <br />

        <xsl:call-template name="labelAndCopyOfValue">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part2.is.eo.sized'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="value">
                <!-- <xsl:call-template name="smeIndicator"/> -->
                <xsl:call-template name="paragraph">
                    <xsl:with-param name="value">
                        <xsl:call-template name="yesNo">
                            <xsl:with-param name="value"/>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
            <xsl:with-param name="criterionTypeCode"  select="'CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP'"/>
        </xsl:call-template>

        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
            <xsl:with-param name="criterionTypeCode"  select="'CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST'"/>
        </xsl:call-template>

        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
            <xsl:with-param name="criterionTypeCode"  select="'CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS'"/>
        </xsl:call-template>

    </xsl:template>

    <!-- <xsl:template name="smeIndicator">
        <xsl:variable name="isSME">
            <xsl:choose>
                <xsl:when test="//*[local-name() = 'EconomicOperatorParty']/*[local-name() = 'Party']/*[local-name() = 'IndustryClassificationCode']/text() = 'SME'">
                    <xsl:value-of select="'true'"/>
                </xsl:when>
                <xsl:when test="//espd-cacV1:EconomicOperatorParty/espd-cbc:SMEIndicator/text() = 'true'">
                    <xsl:value-of select="'true'"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="'false'"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="yesNo">
                    <xsl:with-param name="value" select="$isSME"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template> -->


</xsl:stylesheet>