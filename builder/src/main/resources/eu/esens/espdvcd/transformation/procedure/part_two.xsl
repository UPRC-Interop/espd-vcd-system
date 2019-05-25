<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:cacV1="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:espd-cacV1="urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonAggregateComponents-1"
                xmlns:espd-cbc="urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonBasicComponents-1">

    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>
    <xsl:import href="../base/tender_criterion.xsl"/>

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
                        <xsl:apply-templates select="//*[local-name() = 'EconomicOperatorParty'] | //espd-cacV1:EconomicOperatorParty" />
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

        <xsl:for-each select="(//*[local-name() = 'EconomicOperatorParty']/*[local-name() = 'Party']/*[local-name() = 'PowerOfAttorney']) | (//espd-cacV1:EconomicOperatorParty/espd-cacV1:RepresentativeNaturalPerson)">

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
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'FirstName']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'FirstName']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.first.name'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'FamilyName']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'FamilyName']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.last.name'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'BirthDate']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'BirthDate']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.birth.date'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'BirthplaceName']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'BirthplaceName']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.birth.place'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'StreetName']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'StreetName']" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.street.and. num'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'PostalZone']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'PostalZone']" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.postcode'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'CityName']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'CityName']" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.city'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'Country']/*[local-name() = 'IdentificationCode']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'ResidenceAddress']/*[local-name() = 'Country']/*[local-name() = 'IdentificationCode']" />
                        <xsl:with-param name="propertyKey" select="'espd.part1.country'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'Contact']/*[local-name() = 'Telephone']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'Contact']/*[local-name() = 'Telephone']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.telephone'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <xsl:with-param name="node" select="*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'Contact']/*[local-name() = 'ElectronicMail']
                        | *[local-name() = 'PowerOfAttorney']/*[local-name() = 'AgentParty']/*[local-name() = 'Person']/*[local-name() = 'Contact']/*[local-name() = 'ElectronicMail']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.email'"/>
                    </xsl:call-template>

                    <xsl:call-template name="sectionLabelForValueOfNode">
                        <!-- <xsl:with-param name="node" select="*[local-name() = 'Description'] | //espd-cacV1:EconomicOperatorParty/espd-cacV1:RepresentativeNaturalPerson/espd-cbc:NaturalPersonRoleDescription" /> -->
                        <xsl:with-param name="node" select="*[local-name() = 'Description'] | *[local-name() = 'NaturalPersonRoleDescription']" />
                        <xsl:with-param name="propertyKey" select="'espd.part2.pos.act.in.capacity'"/>
                    </xsl:call-template>

                </xsl:with-param>
            </xsl:call-template>

        </xsl:for-each>

    </xsl:template>

    <xsl:template match="*[local-name() = 'EconomicOperatorParty']">

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PartyName']/*[local-name() = 'Name']" />
            <xsl:with-param name="propertyKey" select="'espd.part2.name'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PostalAddress']/*[local-name() = 'StreetName']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.street.and. num'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PostalAddress']/*[local-name() = 'PostalZone']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.postcode'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PostalAddress']/*[local-name() = 'CityName']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.city'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PostalAddress']/*[local-name() = 'Country']/*[local-name() = 'IdentificationCode']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.country'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node"
                            select="*[local-name() = 'Party']/*[local-name() = 'Contact']/*[local-name() = 'Name']"/>
            <xsl:with-param name="propertyKey" select="'espd.part2.contact.person'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForMailValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'Contact']/*[local-name() = 'ElectronicMail']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.email'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'Contact']/*[local-name() = 'Telephone']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.telephone'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'Contact']/*[local-name() = 'Telefax']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.fax'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'PartyIdentification']/*[local-name() = 'ID']" />
            <xsl:with-param name="propertyKey" select="'espd.part2.vat'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForUrlValueOfNode">
            <xsl:with-param name="node" select="*[local-name() = 'Party']/*[local-name() = 'WebsiteURI']" />
            <xsl:with-param name="propertyKey" select="'espd.part1.website'"/>
        </xsl:call-template>

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

        <xsl:if test="//*[local-name() = 'ProcurementProjectLot']/*[local-name() = 'ID'] and //*[local-name() = 'ESPDResponse']">
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

        <br/>

        <xsl:call-template name="tenderingCriterionSectionByCriterionTypeCode">
            <xsl:with-param name="criterionTypeCode" select="'CRITERION.OTHER.EO_DATA.LOTS_TENDERED'"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="smeIndicator">
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
    </xsl:template>


</xsl:stylesheet>