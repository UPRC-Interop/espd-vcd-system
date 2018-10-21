<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate" xmlns:cbc="urn:X-test:UBL:Pre-award:CommonBasic">

    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>
    <xsl:import href="../tender_criterion.xsl"/>

    <xsl:template name="partTwo">

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
                        <xsl:apply-templates select="//cac:EconomicOperatorParty" />

                    </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="powerOfAttorney"/>

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

    <xsl:template name="powerOfAttorney">

        <xsl:for-each select="//cac:EconomicOperatorParty/cac:Party/cac:PowerOfAttorney">

            <xsl:call-template name="chapterOne">
                <xsl:with-param name="chapter-number"/>
                <xsl:with-param name="chapter-headline">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.part2.info.respresent'"/>
                    </xsl:call-template>
                    #<xsl:value-of select="position()"/>
                </xsl:with-param>
                <xsl:with-param name="chapter-content">

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cbc:FirstName" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.first.name'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cbc:FamilyName" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.last.name'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cbc:BirthDate" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.birth.date'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cbc:BirthplaceName" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.birth.place'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:StreetName" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.street.and. num'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:PostalZone" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.postcode'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:CityName" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.city'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:ResidenceAddress/cac:Country/cbc:IdentificationCode" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.country'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:Contact/cbc:Telephone" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.telephone'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cac:AgentParty/cac:Person/cac:Contact/cbc:ElectronicMail" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.email'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="cbc:Description" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.pos.act.in.capacity'"/>
                    </xsl:call-template>

                </xsl:with-param>
            </xsl:call-template>

        </xsl:for-each>

    </xsl:template>

    <xsl:template match="cac:EconomicOperatorParty">

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PartyName/cbc:Name" />
            <xsl:with-param name="propertyKey" select="'espd.part2.name'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cbc:StreetName" />
            <xsl:with-param name="propertyKey" select="'espd.part1.street.and. num'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cbc:PostalZone" />
            <xsl:with-param name="propertyKey" select="'espd.part1.postcode'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cbc:CityName" />
            <xsl:with-param name="propertyKey" select="'espd.part1.city'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />
            <xsl:with-param name="propertyKey" select="'espd.part1.country'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForMailValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:ElectronicMail" />
            <xsl:with-param name="propertyKey" select="'espd.part1.email'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:Telephone" />
            <xsl:with-param name="propertyKey" select="'espd.part1.telephone'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:Telefax" />
            <xsl:with-param name="propertyKey" select="'espd.part1.fax'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PartyIdentification/cbc:ID" />
            <xsl:with-param name="propertyKey" select="'espd.part2.vat'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForUrlValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cbc:WebsiteURI" />
            <xsl:with-param name="propertyKey" select="'espd.part1.website'"/>
        </xsl:call-template>

        <br />

        <xsl:if test="//cac:ProcurementProjectLot/cbc:ID">
            <xsl:call-template name="labelAndTextValue">
                <xsl:with-param name="label">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.part2.lots.concerned'"/>
                    </xsl:call-template>
                </xsl:with-param>
                <xsl:with-param name="value">
                    <xsl:value-of select="//cac:ProcurementProjectLot/cbc:ID"/>
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
                <xsl:call-template name="smeIndicator"/>
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

    <xsl:template name="smeIndicator">
        <xsl:variable name="isSME" select="//cac:Party/cac:PartyIdentification/cbc:ID/text() = 'SME'"/>
        <form>
            <xsl:element name="input">
                <xsl:attribute name="name">yes_no</xsl:attribute>
                <xsl:attribute name="type">radio</xsl:attribute>
                <xsl:attribute name="disabled" />
                <xsl:if test="$isSME">
                    <xsl:attribute name="checked" />
                </xsl:if>
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.yes'"/>
                </xsl:call-template>
            </xsl:element>
            <xsl:element name="input">
                <xsl:attribute name="name">yes_no</xsl:attribute>
                <xsl:attribute name="type">radio</xsl:attribute>
                <xsl:attribute name="disabled" />
                <xsl:if test="not($isSME)">
                    <xsl:attribute name="checked" />
                </xsl:if>
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.no'"/>
                </xsl:call-template>
            </xsl:element>
        </form>
    </xsl:template>
    

</xsl:stylesheet>