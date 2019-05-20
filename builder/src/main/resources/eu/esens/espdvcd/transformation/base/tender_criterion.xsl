<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:cbcV1="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:ccvV1="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1"
                xmlns:ccv-cbcV1="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1"
                xmlns:ubl-cacV1="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
                xmlns:espd-cacV1="urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonAggregateComponents-1"
                xmlns:ubl-cbcV1="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
                xmlns:ccv-cacV1="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1"
                xmlns:cev-cacV1="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1">

    <xsl:import href="../html/chapter.xsl"/>
    <xsl:import href="../html/structure.xsl"/>
    <xsl:import href="../html/date.xsl"/>
    <xsl:import href="../propertyreader/property_reader.xsl"/>

    <xsl:template name="searchForTenderingCriterionSectionByArticle">
        <xsl:param name="article"/>

        <xsl:variable name="articlesInCriterionEspdV1" select="//ccvV1:Criterion[contains(./ccvV1:LegislationReference/ccv-cbcV1:Article, $article) and not(./cbcV1:TypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER')]" />
        <xsl:variable name="articlesInCriterionEspdV2" select="//cac:TenderingCriterion[contains(./cac:Legislation/cbc:Article, $article) and not(./cbc:CriterionTypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER')]" />
        <xsl:for-each select="$articlesInCriterionEspdV1 | $articlesInCriterionEspdV2">
            <xsl:value-of select="current()"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="tenderingCriterionSection">
        <xsl:param name="article"/>

        <xsl:variable name="articlesInCriterionEspdV1" select="//ccvV1:Criterion[contains(./ccvV1:LegislationReference/ccv-cbcV1:Article, $article) and not(./cbcV1:TypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER')]" />
        <xsl:variable name="articlesInCriterionEspdV2" select="//cac:TenderingCriterion[contains(./cac:Legislation/cbc:Article, $article) and not(./cbc:CriterionTypeCode = 'CRITERION.EXCLUSION.NATIONAL.OTHER')]" />
        <xsl:for-each select="$articlesInCriterionEspdV1 | $articlesInCriterionEspdV2">
            <xsl:apply-templates select="current()"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="SearchForTenderingCriterionSectionByCriterionTypeCode">
        <xsl:param name="criterionTypeCode"/>

        <xsl:variable name="typeCodesEspdV2" select="//cac:TenderingCriterion[starts-with(./cbc:CriterionTypeCode, $criterionTypeCode)]" />
        <xsl:variable name="typeCodesEspdV1" select="//ccvV1:Criterion[starts-with(./cbcV1:TypeCode, $criterionTypeCode)]" />
        <xsl:for-each select="$typeCodesEspdV1 | $typeCodesEspdV2">
            <xsl:value-of select="current()"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="tenderingCriterionSectionByCriterionTypeCode">
        <xsl:param name="criterionTypeCode"/>

        <xsl:variable name="typeCodesEspdV2" select="//cac:TenderingCriterion[starts-with(./cbc:CriterionTypeCode, $criterionTypeCode)]" />
        <xsl:variable name="typeCodesEspdV1" select="//ccvV1:Criterion[starts-with(./cbcV1:TypeCode, $criterionTypeCode)]" />
        <xsl:for-each select="$typeCodesEspdV1 | $typeCodesEspdV2">
            <xsl:apply-templates select="current()"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="cac:TenderingCriterion | ccvV1:Criterion">
        <xsl:call-template name="label">
            <xsl:with-param name="label" >
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="*[local-name()='Name']"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="paragraph">
            <xsl:with-param name="value">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="*[local-name()='Description']"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>

        <xsl:apply-templates select="cac:TenderingCriterionPropertyGroup | ccvV1:RequirementGroup"/>

        <br />
    </xsl:template>

    <xsl:template match="ccvV1:RequirementGroup">
        <xsl:apply-templates select="ccvV1:Requirement" />
        <xsl:apply-templates select="ccvV1:RequirementGroup" />
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
            <xsl:when test="local-name($node/../../..) = 'SubsidiaryTenderingCriterionPropertyGroup' or local-name($node/../../../..) = 'RequirementGroup'">4</xsl:when>
            <xsl:when test="local-name($node/../..) = 'SubsidiaryTenderingCriterionPropertyGroup' or local-name($node/../../..) = 'RequirementGroup'">3</xsl:when>
            <xsl:when test="local-name($node/..) = 'SubsidiaryTenderingCriterionPropertyGroup' or local-name($node/../..) = 'RequirementGroup'">2</xsl:when>
            <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cac:TenderingCriterionProperty | ccvV1:Requirement">

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
                    <xsl:when test="cbc:ValueDataTypeCode = 'EVIDENCE_IDENTIFIER' or @responseDataType = 'EVIDENCE_URL'">
                        <xsl:call-template name="evidenceIdentifier"/>
                    </xsl:when>
                    <xsl:when test="cbc:ValueDataTypeCode = 'ECONOMIC_OPERATOR_IDENTIFIER'">
                        <xsl:call-template name="economicOperatorIdentifier"/>
                    </xsl:when>
                    <xsl:otherwise>

                        <!-- Description -->
                        <!--                        <xsl:choose>-->
                        <!--                            <xsl:when test="cbc:ValueDataTypeCode = 'PERIOD' or @responseDataType = 'PERIOD'">-->
                        <!--                                <xsl:call-template name="getPeriodDescription" />-->
                        <!--                            </xsl:when>-->
                        <!--                            <xsl:otherwise>-->
                        <!--                                <xsl:call-template name="getDescription" />-->
                        <!--                            </xsl:otherwise>-->
                        <!--                        </xsl:choose>-->

                        <xsl:choose>
                            <xsl:when test="cbc:ValueDataTypeCode = 'INDICATOR' or @responseDataType = 'INDICATOR'">
                                <xsl:call-template name="yesNoIndicator"/>
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'DESCRIPTION' or @responseDataType = 'DESCRIPTION'">
                                <xsl:call-template name="descriptionValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'AMOUNT' or @responseDataType = 'AMOUNT'">
                                <xsl:call-template name="amountValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'DATE' or @responseDataType = 'DATE'">
                                <xsl:call-template name="dateValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'CODE_COUNTRY' or @responseDataType = 'CODE_COUNTRY'">
                                <xsl:call-template name="countryValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'IDENTIFIER' or @responseDataType = 'IDENTIFIER'">
                                <xsl:call-template name="identifierValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'CODE' or @responseDataType = 'CODE'">
                                <xsl:call-template name="codeValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'URL' or @responseDataType = 'URL'">
                                <xsl:call-template name="urlValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'PERCENTAGE' or @responseDataType = 'PERCENTAGE'">
                                <xsl:call-template name="percentageValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'QUANTITY' or @responseDataType = 'QUANTITY'">
                                <xsl:call-template name="quantityIntegerValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'QUANTITY_INTEGER' or @responseDataType = 'QUANTITY_INTEGER'">
                                <xsl:call-template name="quantityIntegerValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'QUANTITY_YEAR' or @responseDataType = 'QUANTITY_YEAR'">
                                <xsl:call-template name="quantityIntegerValue" />
                            </xsl:when>
                            <xsl:when test="cbc:ValueDataTypeCode = 'PERIOD'">
                                <xsl:call-template name="periodValue" />
                            </xsl:when>
                            <xsl:when test="@responseDataType = 'PERIOD'">
                                <xsl:call-template name="periodValueEspdV1"/>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>


    </xsl:template>

    <!-- The texts are not the same like the translated properties, that's the reason why we must map them, so we can translate it. -->
    <xsl:template name="getDescription">
        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="*[local-name() = 'Description']"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="getPeriodDescription">
        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.start.date'"/>
                </xsl:call-template> -
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.end.date'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="getPeriodV1Description">
        <xsl:call-template name="label">
            <xsl:with-param name="label">
                <xsl:call-template name="getESPDProperty">
                    <xsl:with-param name="key" select="'espd.crit.length.period.exclusion'"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="yesNoIndicator">
        <xsl:variable name="tenderingCriterionResponse1"
                      select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
        <xsl:if test="$tenderingCriterionResponse1/cac:ResponseValue/cbc:ResponseIndicator | ./ccvV1:Response/ccv-cbcV1:Indicator or  not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:variable name="tenderingCriterionResponse"
                          select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
            <xsl:call-template name="getDescription"/>
            <xsl:call-template name="yesNo">
                <xsl:with-param name="value"
                                select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseIndicator | ./ccvV1:Response/ccv-cbcV1:Indicator"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="countryValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseCode | ./ccvV1:Response/ccv-cbcV1:Code"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="amountValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:if test="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseAmount | ./ccvV1:Response/ubl-cbcV1:Amount or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:call-template name="responseValue">
                <xsl:with-param name="responseValue" >
                    <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseAmount | ./ccvV1:Response/ubl-cbcV1:Amount"/>&#160;
                    <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseAmount/@currencyID | ./ccvV1:Response/ubl-cbcV1:Amount/@currencyID"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

    </xsl:template>

    <xsl:template name="dateValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:if test="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseDate | ./ccvV1:Response/ubl-cbcV1:Date  or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:call-template name="responseValueNode">
                <xsl:with-param name="responseValueNode">
                    <xsl:call-template name="formatDate">
                        <xsl:with-param name="isoDate" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseDate | ./ccvV1:Response/ubl-cbcV1:Date"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

    </xsl:template>

    <xsl:template name="descriptionValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:Description | ./ccvV1:Response/cbcV1:Description"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="identifierValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseID | ./ccvV1:Response/ubl-cbcV1:ID"/>
        </xsl:call-template>

    </xsl:template>
    <xsl:template name="codeValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseCode | ./ccvV1:Response/ccv-cbcV1:Code"/>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="urlValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseURI"/>
        </xsl:call-template>

    </xsl:template>
    <xsl:template name="percentageValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:if test="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseQuantity | ./ccvV1:Response/ubl-cbcV1:Percent  or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:call-template name="responseValueNode">
                <xsl:with-param name="responseValueNode">
                    <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseQuantity | ./ccvV1:Response/ubl-cbcV1:Percent"/> &#37;
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="periodValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:if test="$tenderingCriterionResponse/cac:ApplicablePeriod/cbc:StartDate and $tenderingCriterionResponse/cac:ApplicablePeriod/cbc:EndDate  or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:call-template name="getPeriodDescription"/>
            <xsl:variable name="value">
                <xsl:call-template name="formatDate">
                    <xsl:with-param name="isoDate" select="$tenderingCriterionResponse/cac:ApplicablePeriod/cbc:StartDate"/>
                </xsl:call-template> - <xsl:call-template name="formatDate">
                <xsl:with-param name="isoDate" select="$tenderingCriterionResponse/cac:ApplicablePeriod/cbc:EndDate" />
            </xsl:call-template>
            </xsl:variable>

            <xsl:call-template name="responseValueNode">
                <xsl:with-param name="responseValueNode" select="$value"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="quantityIntegerValue">
        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseQuantity | ./ccvV1:Response/ubl-cbcV1:Quantity"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="periodValueEspdV1">
        <xsl:if test="./ccv-cacV1:Response/ubl-cacV1:Period/ubl-cbcV1:Description">
            <xsl:call-template name="getPeriodV1Description"/>
            <xsl:call-template name="responseValueNode">
            <xsl:with-param name="responseValueNode" select="./ccv-cacV1:Response/ubl-cacV1:Period/ubl-cbcV1:Description"/>
        </xsl:call-template>
        </xsl:if>
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
                    <xsl:when test="$responseValue and not($responseValue = '')">
                        <xsl:call-template name="getDescription"/>
                        <xsl:copy-of select="$responseValue"/>
                    </xsl:when>
                    <xsl:when
                            test="not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
                        <xsl:call-template name="getDescription"/>
                        -
                    </xsl:when>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="evidenceIdentifier">

        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>
        <xsl:variable name="evidence" select="//cac:Evidence[./cbc:ID = $tenderingCriterionResponse/cac:EvidenceSupplied/cbc:ID] | ./ccv-cacV1:Response/cev-cacV1:Evidence"/>

        <xsl:if test="$evidence or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:if test="$tenderingCriterionResponse">
            <xsl:call-template name="label">
                <xsl:with-param name="label">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.crit.url'"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
            </xsl:if>
            <xsl:call-template name="responseValue">
                <xsl:with-param name="responseValue">
                    <xsl:value-of select="$evidence/cac:DocumentReference/cac:Attachment/cac:ExternalReference/cbc:URI | $evidence/cev-cacV1:EvidenceDocumentReference/ubl-cacV1:Attachment/ubl-cacV1:ExternalReference/ubl-cbcV1:URI"/>
                </xsl:with-param>
            </xsl:call-template>

            <xsl:if test="$tenderingCriterionResponse">
                <xsl:call-template name="label">
                    <xsl:with-param name="label">
                        <xsl:call-template name="getESPDProperty">
                            <xsl:with-param name="key" select="'espd.crit.code'"/>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="responseValue">
                    <xsl:with-param name="responseValue">
                        <xsl:value-of
                                select="$evidence/*[local-name() = 'Description'] | $evidence/../../*[local-name() = 'Description']"/>
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
                        <xsl:value-of
                                select="$evidence/cac:DocumentReference/cac:IssuerParty/cac:PartyName/cbc:Name | ../ccv-cacV1:Requirement[3]/ccv-cacV1:Response/ubl-cbcV1:Description"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>

        </xsl:if>
    </xsl:template>

    <xsl:template name="economicOperatorIdentifier">

        <xsl:variable name="tenderingCriterionResponse" select="//cac:TenderingCriterionResponse[./cbc:ValidatedCriterionPropertyID = current()/cbc:ID]"/>

        <xsl:if test="$tenderingCriterionResponse  or not(.//*[local-name() = 'EconomicOperatorParty'] | //cac:EconomicOperatorParty | //espd-cacV1:EconomicOperatorParty)">
            <xsl:call-template name="label">
                <xsl:with-param name="label">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.crit.entity.ID'"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="responseValue">
                <xsl:with-param name="responseValue">
                    <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseID"/>
                </xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="label">
                <xsl:with-param name="label">
                    <xsl:call-template name="getESPDProperty">
                        <xsl:with-param name="key" select="'espd.crit.type.id'"/>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="responseValue">
                <xsl:with-param name="responseValue">
                    <xsl:value-of select="$tenderingCriterionResponse/cac:ResponseValue/cbc:ResponseID/@schemeName"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>