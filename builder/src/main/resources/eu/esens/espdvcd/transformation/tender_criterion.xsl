<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate" xmlns:cbc="urn:X-test:UBL:Pre-award:CommonBasic">

    <xsl:import href="html/chapter.xsl"/>
    <xsl:import href="html/structure.xsl"/>
    <xsl:import href="html/date.xsl"/>
    <xsl:import href="propertyreader/property_reader.xsl"/>

    <xsl:template name="tenderingCriterionSection">
        <xsl:param name="article"/>

        <xsl:for-each select="//cac:TenderingCriterion[./cac:Legislation/cbc:Article = $article
            and not(./cbc:CriterionTypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER')]">
                <xsl:apply-templates select="current()"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="cac:TenderingCriterion">
        <xsl:call-template name="label">
            <xsl:with-param name="label" >
                <xsl:value-of select="cbc:Name"/>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:value-of select="cbc:Description"/>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="cac:TenderingCriterionPropertyGroup"/>

        <br />
    </xsl:template>

    <xsl:template match="cac:TenderingCriterionPropertyGroup">
        <xsl:apply-templates select="cac:TenderingCriterionProperty" />
        <xsl:apply-templates select="cac:SubsidiaryTenderingCriterionPropertyGroup" />
    </xsl:template>

    <xsl:template match="cac:SubsidiaryTenderingCriterionPropertyGroup">
        <xsl:apply-templates select="cac:TenderingCriterionProperty" />
        <xsl:apply-templates select="cac:SubsidiaryTenderingCriterionPropertyGroup" />
    </xsl:template>

    <xsl:template name="getLevelStringForSubChapter">
        <xsl:param name="node"/>
        <xsl:choose>
            <xsl:when test="local-name($node/../../..) = 'SubsidiaryTenderingCriterionPropertyGroup'">4</xsl:when>
            <xsl:when test="local-name($node/../..) = 'SubsidiaryTenderingCriterionPropertyGroup'">3</xsl:when>
            <xsl:when test="local-name($node/..) = 'SubsidiaryTenderingCriterionPropertyGroup'">2</xsl:when>
            <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cac:TenderingCriterionProperty">

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">
                <xsl:call-template name="getLevelStringForSubChapter">
                    <xsl:with-param name="node" select="current()"/>
                </xsl:call-template>
            </xsl:with-param>
            <xsl:with-param name="chapter-number"/>
            <xsl:with-param name="chapter-headline"/>
            <xsl:with-param name="chapter-content">
                <xsl:choose>
                    <xsl:when test="cbc:ValueDataTypeCode = 'EVIDENCE_IDENTIFIER'">
                        <xsl:call-template name="evidenceIdentifier"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="getDescription" />

                        <xsl:choose>
                            <xsl:when test="cbc:ValueDataTypeCode = 'INDICATOR'">
                                <xsl:call-template name="yesNoIndicator"/>
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'DESCRIPTION'">
                                <xsl:call-template name="descriptionValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'AMOUNT'">
                                <xsl:call-template name="amountValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'DATE'">
                                <xsl:call-template name="dateValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'CODE_COUNTRY'">
                                <!--ResponseCode-->
                                <xsl:call-template name="countryValue" />
                            </xsl:when>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>


    </xsl:template>

    <!-- Da die Texte in der XML nicht den übersetzten Properties entsprechen, müssen wir hier ein "Mapping" vornehmen, damit wir diese sauber übersetzen können-->
    <xsl:template name="getDescription">
        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:value-of select="cbc:Description"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="yesNoIndicator">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
        <form>
            <xsl:element name="input">
                <xsl:attribute name="name">yes_no</xsl:attribute>
                <xsl:attribute name="type">radio</xsl:attribute>
                <xsl:attribute name="disabled" />
                <xsl:if test="$tenderingCriterionResponse">
                    <xsl:if test="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseIndicator = 'true'">
                        <xsl:attribute name="checked" />
                    </xsl:if>
                </xsl:if>
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.yes'"/>
                </xsl:call-template>
            </xsl:element>
            <xsl:element name="input">
                <xsl:attribute name="name">yes_no</xsl:attribute>
                <xsl:attribute name="type">radio</xsl:attribute>
                <xsl:attribute name="disabled" />
                <xsl:if test="$tenderingCriterionResponse">
                    <xsl:if test="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseIndicator = 'false'">
                        <xsl:attribute name="checked" />
                    </xsl:if>
                </xsl:if>
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.gui.no'"/>
                </xsl:call-template>
            </xsl:element>
        </form>
    </xsl:template>

    <xsl:template name="countryValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseCode"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="amountValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValue">
            <xsl:with-param name="responseValue" >
                <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseAmount"/>&#160;
                <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseAmount/@currencyID"/>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="dateValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode">
                <xsl:call-template name="formatDate">
                    <xsl:with-param name="isoDate" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseDate"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="descriptionValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:Description"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="responseValueNode">
        <xsl:param name="responseValueNode"/>

        <xsl:call-template name="responseValue">
            <xsl:with-param name="responseValue">
                <xsl:value-of select="$responseValueNode"/>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="responseValue">
        <xsl:param name="responseValue"/>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:choose>
                    <xsl:when test="$responseValue">
                        <xsl:copy-of select="$responseValue"/>
                    </xsl:when>
                    <xsl:otherwise>
                        -
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="evidenceIdentifier">

        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
        <xsl:variable name="evidence" select="//cac:Evidence[./cbc:ID = $tenderingCriterionResponse/cac:EvidenceSupplied/cbc:ID]"/>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.url'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="responseValue">
            <xsl:with-param name="responseValue">
                <xsl:value-of select="$evidence/cac:DocumentReference/cac:Attachment/cac:ExternalReference/cbc:URI"/>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.code'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="responseValue">
            <xsl:with-param name="responseValue">
                <xsl:value-of select="$evidence/cbc:Description"/>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.issuer'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="responseValue">
            <xsl:with-param name="responseValue">
                <xsl:value-of select="$evidence/cac:DocumentReference/cac:IssuerParty/cac:PartyName/cbc:Name"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>