<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate" xmlns:cbc="urn:X-test:UBL:Pre-award:CommonBasic">

    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>

    <xsl:template name="partOne">
        <xsl:call-template name="section">
            <xsl:with-param name="title">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.header'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part1.info.pub'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">
                        <xsl:apply-templates select="//cac:AdditionalDocumentReference" />
                    </xsl:with-param>
                </xsl:call-template>

                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part1.contact.details.ca'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">

                        <xsl:apply-templates select="//cac:ContractingParty"/>
                    </xsl:with-param>
                </xsl:call-template>

                <xsl:call-template name="chapterOne">
                    <xsl:with-param name="chapter-number"/>
                    <xsl:with-param name="chapter-headline">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.part1.info.procurement.proc'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                    <xsl:with-param name="chapter-content">

                        <xsl:call-template name="infoProcurementProc"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="infoProcurementProc">

        <xsl:call-template name="sectionLabel">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.procurer.name'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="//cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:FileName"/>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="labelAndTextValue">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.title.or.short.desc'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="value">
                <xsl:for-each select="//cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:Description">
                    <xsl:if test="position() = 1">
                        <xsl:value-of select="."/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="sectionLabel">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.file.ref.ca'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="//cbc:ContractFolderID"/>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template match="cac:ContractingParty">

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PartyName/cbc:Name" />
            <xsl:with-param name="propertyKey" select="'espd.part1.name'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PartyIdentification/cbc:ID" />
            <xsl:with-param name="propertyKey" select="'espd.part2.vat'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForUrlValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cbc:WebsiteURI" />
            <xsl:with-param name="propertyKey" select="'espd.part1.website'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cbc:CityName" />
            <xsl:with-param name="propertyKey" select="'espd.part1.city'"/>
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
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:Name" />
            <xsl:with-param name="propertyKey" select="'espd.part1.contact.person'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:Telephone" />
            <xsl:with-param name="propertyKey" select="'espd.part1.telephone'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:Telefax" />
            <xsl:with-param name="propertyKey" select="'espd.part1.fax'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForMailValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:Contact/cbc:ElectronicMail" />
            <xsl:with-param name="propertyKey" select="'espd.part1.email'"/>
        </xsl:call-template>

        <xsl:call-template name="sectionLabelForValueOfNode">
            <xsl:with-param name="node" select="cac:Party/cac:PostalAddress/cac:Country/cbc:IdentificationCode" />
            <xsl:with-param name="propertyKey" select="'espd.part1.country'"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template match="cac:AdditionalDocumentReference">
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.to.be.filled.alert'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="sectionLabel">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.ted.reception.id'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:for-each select="cac:Attachment/cac:ExternalReference/cbc:Description">
                    <xsl:if test="position() = 2">
                        <xsl:value-of select="."/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="sectionLabel">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.ojs.label'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="cbc:ID"/>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="sectionLabel">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.ojs.url'"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:call-template name="formatUrl">
                    <xsl:with-param name="url" select="cac:Attachment/cac:ExternalReference/cbc:URI" />
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <br />

        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.part1.official.journal.alert'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

</xsl:stylesheet>