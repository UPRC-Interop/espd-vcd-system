<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:espd="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:udt="urn:oasis:names:specification:ubl:schema:xsd:UnqualifiedDataTypes-2" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--Implementers: please note that overriding process-prolog or process-root is 
    the preferred method for meta-stylesheets to use where possible. -->

<xsl:param name="archiveDirParameter" />
  <xsl:param name="archiveNameParameter" />
  <xsl:param name="fileNameParameter" />
  <xsl:param name="fileDirParameter" />
  <xsl:variable name="document-uri">
    <xsl:value-of select="document-uri(/)" />
  </xsl:variable>

<!--PHASES-->


<!--PROLOG-->
<xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" />

<!--XSD TYPES FOR XSLT2-->


<!--KEYS AND FUNCTIONS-->


<!--DEFAULT RULES-->


<!--MODE: SCHEMATRON-SELECT-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-select-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="." />
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-get-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="parent::*" />
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">
        <xsl:value-of select="name()" />
        <xsl:variable name="p_1" select="1+    count(preceding-sibling::*[name()=name(current())])" />
        <xsl:if test="$p_1>1 or following-sibling::*[name()=name(current())]">[<xsl:value-of select="$p_1" />]</xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>']</xsl:text>
        <xsl:variable name="p_2" select="1+   count(preceding-sibling::*[local-name()=local-name(current())])" />
        <xsl:if test="$p_2>1 or following-sibling::*[local-name()=local-name(current())]">[<xsl:value-of select="$p_2" />]</xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="@*" mode="schematron-get-full-path">
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">@<xsl:value-of select="name()" />
</xsl:when>
      <xsl:otherwise>
        <xsl:text>@*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>' and namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-2-->
<!--This mode can be used to generate prefixed XPath for humans-->
<xsl:template match="node() | @*" mode="schematron-get-full-path-2">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="preceding-sibling::*[name(.)=name(current())]">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>
<!--MODE: SCHEMATRON-FULL-PATH-3-->
<!--This mode can be used to generate prefixed XPath for humans 
	(Top-level element has index)-->

<xsl:template match="node() | @*" mode="schematron-get-full-path-3">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="parent::*">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>

<!--MODE: GENERATE-ID-FROM-PATH -->
<xsl:template match="/" mode="generate-id-from-path" />
  <xsl:template match="text()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.text-', 1+count(preceding-sibling::text()), '-')" />
  </xsl:template>
  <xsl:template match="comment()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.comment-', 1+count(preceding-sibling::comment()), '-')" />
  </xsl:template>
  <xsl:template match="processing-instruction()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.processing-instruction-', 1+count(preceding-sibling::processing-instruction()), '-')" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.@', name())" />
  </xsl:template>
  <xsl:template match="*" mode="generate-id-from-path" priority="-0.5">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:text>.</xsl:text>
    <xsl:value-of select="concat('.',name(),'-',1+count(preceding-sibling::*[name()=name(current())]),'-')" />
  </xsl:template>

<!--MODE: GENERATE-ID-2 -->
<xsl:template match="/" mode="generate-id-2">U</xsl:template>
  <xsl:template match="*" mode="generate-id-2" priority="2">
    <xsl:text>U</xsl:text>
    <xsl:number count="*" level="multiple" />
  </xsl:template>
  <xsl:template match="node()" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>n</xsl:text>
    <xsl:number count="node()" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="string-length(local-name(.))" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="translate(name(),':','.')" />
  </xsl:template>
<!--Strip characters-->  <xsl:template match="text()" priority="-1" />

<!--SCHEMA SETUP-->
<xsl:template match="/">
    <svrl:schematron-output schemaVersion="" title="Common Other Business Rules">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="ext" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="udt" uri="urn:oasis:names:specification:ubl:schema:xsd:UnqualifiedDataTypes-2" />
      <svrl:ns-prefix-in-attribute-values prefix="espd" uri="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2" />
      <svrl:ns-prefix-in-attribute-values prefix="fn" uri="http://www.w3.org/2005/xpath-functions" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">BR-COM-OTH</xsl:attribute>
        <xsl:attribute name="name">BR-COM-OTH</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M7" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>Common Other Business Rules</svrl:text>

<!--PATTERN BR-COM-OTH-->


	<!--RULE -->
<xsl:template match="cac:AdditionalDocumentReference" mode="M7" priority="1006">
    <svrl:fired-rule context="cac:AdditionalDocumentReference" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(count(cbc:ID)=0) or not(cbc:DocumentTypeCode/text() = 'TED_CN') or ((count(cbc:ID))=1 and fn:matches(normalize-space(cbc:ID/text()), '^[0-9]{4}/S [0-9]{3}\-[0-9]{6}$'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(count(cbc:ID)=0) or not(cbc:DocumentTypeCode/text() = 'TED_CN') or ((count(cbc:ID))=1 and fn:matches(normalize-space(cbc:ID/text()), '^[0-9]{4}/S [0-9]{3}\-[0-9]{6}$'))">
          <xsl:attribute name="id">BR-COM-10-01</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>If the document referenced is a Contract Notice published on TED the ID must follow the scheme defined by the Publications Office: [][][][]/S [][][]-[][][][][][] (cbc:ID = '<xsl:text />
            <xsl:value-of select="cbc:ID" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(cbc:DocumentTypeCode/text() = 'TED_CN' and (count(cac:Attachment/cac:ExternalReference/cbc:Description) = 2)) or not(cbc:DocumentTypeCode/text() = 'TED_CN')" />
      <xsl:otherwise>
        <svrl:failed-assert test="(cbc:DocumentTypeCode/text() = 'TED_CN' and (count(cac:Attachment/cac:ExternalReference/cbc:Description) = 2)) or not(cbc:DocumentTypeCode/text() = 'TED_CN')">
          <xsl:attribute name="id">BR-COM-10-02</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>If the document being referenced is a Notice being published on TED, use two description lines ('/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:Description'). Use the second description line to place therein the temporary number received from TED (cbc:ID = '<xsl:text />
            <xsl:value-of select="cbc:ID" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(cbc:DocumentTypeCode)" />
      <xsl:otherwise>
        <svrl:failed-assert test="(cbc:DocumentTypeCode)">
          <xsl:attribute name="id">BR-COM-10-03</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The type of document being referenced, expressed as a code ('/cac:AdditionalDocumentReference/cbc:DocumentTypeCode') is mandatory (cbc:ID = '<xsl:text />
            <xsl:value-of select="cbc:ID" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:UBLVersionID" mode="M7" priority="1005">
    <svrl:fired-rule context="cbc:UBLVersionID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="text()='2.2'" />
      <xsl:otherwise>
        <svrl:failed-assert test="text()='2.2'">
          <xsl:attribute name="id">BR-OTH-05-01</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>'cbc:UBLVersionID' must use the value "2.2" (cbc:UBLVersionID = '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@schemeAgencyID = 'OASIS-UBL-TC'" />
      <xsl:otherwise>
        <svrl:failed-assert test="@schemeAgencyID = 'OASIS-UBL-TC'">
          <xsl:attribute name="id">BR-OTH-05-02</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>'cbc:UBLVersionID/@schemeAgencyID' must use the value "OASIS-UBL-TC" (cbc:UBLVersionID/@schemeAgencyID = '<xsl:text />
            <xsl:value-of select="@schemeAgencyID" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProfileID" mode="M7" priority="1004">
    <svrl:fired-rule context="cbc:ProfileID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="text()='4.1'" />
      <xsl:otherwise>
        <svrl:failed-assert test="text()='4.1'">
          <xsl:attribute name="id">BR-OTH-07-01</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>'cbc:ProfileID' must use the value "4.1" (cbc:ProfileID = '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@schemeAgencyID = 'CEN-BII'" />
      <xsl:otherwise>
        <svrl:failed-assert test="@schemeAgencyID = 'CEN-BII'">
          <xsl:attribute name="id">BR-OTH-07-02</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>'cbc:ProfileID/@schemeAgencyID' must use the value "CEN-BII" (cbc:ProfileID/@schemeAgencyID = '<xsl:text />
            <xsl:value-of select="@schemeAgencyID" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ExpectedCode | cbc:IdentificationCode | cbc:ValueCurrencyCode | cbc:ProcedureCode | cbc:ProcurementTypeCode | cbc:ProcurementSubTypeCode" mode="M7" priority="1003">
    <svrl:fired-rule context="cbc:ExpectedCode | cbc:IdentificationCode | cbc:ValueCurrencyCode | cbc:ProcedureCode | cbc:ProcurementTypeCode | cbc:ProcurementSubTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID">
          <xsl:attribute name="id">BR-OTH-01-10</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listAgencyID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listAgencyID">
          <xsl:attribute name="id">BR-OTH-01-20</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listAgencyID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listVersionID">
          <xsl:attribute name="id">BR-OTH-01-30</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listVersionID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ConfidentialityLevelCode | cbc:TypeCode | cbc:DocumentTypeCode | cbc:IndustryClassificationCode | cbc:RoleCode | cbc:EvaluationMethodTypeCode | cbc:PropertyGroupTypeCode |    cbc:QualificationApplicationTypeCode | cbc:ValueDataTypeCode | cbc:CriterionTypeCode | cbc:WeightingTypeCode" mode="M7" priority="1002">
    <svrl:fired-rule context="cbc:ConfidentialityLevelCode | cbc:TypeCode | cbc:DocumentTypeCode | cbc:IndustryClassificationCode | cbc:RoleCode | cbc:EvaluationMethodTypeCode | cbc:PropertyGroupTypeCode |    cbc:QualificationApplicationTypeCode | cbc:ValueDataTypeCode | cbc:CriterionTypeCode | cbc:WeightingTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID">
          <xsl:attribute name="id">BR-OTH-01-11</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listAgencyID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listAgencyID">
          <xsl:attribute name="id">BR-OTH-01-21</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listAgencyID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listVersionID">
          <xsl:attribute name="id">BR-OTH-01-31</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute listVersionID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="starts-with(@listVersionID, '2.') or starts-with(@listVersionID, '02.')" />
      <xsl:otherwise>
        <svrl:failed-assert test="starts-with(@listVersionID, '2.') or starts-with(@listVersionID, '02.')">
          <xsl:attribute name="id">BR-OTH-01-40</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>List version identifier '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />/listVersionID = <xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />' is not correct. ListVersionID should be '2.x.y' or '02.xx.yy'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="ext:ExtensionAgencyID | ext:ExtensionVersionID | cbc:AccountID| cbc:AdditionalAccountID|cbc:AgencyID| cbc:AircraftID| cbc:AttributeID| cbc:AwardID| cbc:AwardingCriterionID| cbc:BarcodeSymbologyID| cbc:BrokerAssignedID| cbc:BusinessClassificationEvidenceID|    cbc:BusinessIdentityEvidenceID| cbc:BuyerEventID| cbc:CV2ID| cbc:CarrierAssignedID| cbc:ChipApplicationID| cbc:CompanyID| cbc:ConsigneeAssignedID| cbc:ConsignorAssignedID| cbc:ConsumptionID| cbc:ConsumptionReportID| cbc:ContractFolderID| cbc:ContractedCarrierAssignedID|   cbc:CustomerAssignedAccountID| cbc:CustomizationID| cbc:DocumentID| cbc:ExchangeMarketID| cbc:ExpectedID| cbc:ExtendedID| cbc:FormatID| cbc:FreightForwarderAssignedID| cbc:HazardClassID| cbc:ID| cbc:IdentificationID| cbc:ImmobilizationCertificateID|   cbc:InstructionID| cbc:IssueNumberID| cbc:IssuerID| cbc:JourneyID| cbc:LanguageID| cbc:LicensePlateID| cbc:LineID| cbc:LoadingSequenceID| cbc:LocationID| cbc:LogoReferenceID| cbc:LotNumberID| cbc:LowerOrangeHazardPlacardID| cbc:MarkingID| cbc:MinimumImprovementBid|   cbc:NationalityID| cbc:NetworkID| cbc:OID| cbc:OpenTenderID| cbc:OriginalContractingSystemID| cbc:OriginalJobID| cbc:ParentDocumentID| cbc:ParentDocumentLineReferenceID| cbc:ParentDocumentVersionID| cbc:ParticipantID| cbc:PaymentID| cbc:PaymentMeansID|   cbc:PerformingCarrierAssignedID| cbc:PrepaidPaymentReferenceID| cbc:PreviousJobID| cbc:PreviousVersionID| cbc:PrimaryAccountNumberID| cbc:ProductTraceID| cbc:ProfileExecutionID| cbc:ProfileID| cbc:ProtocolID| cbc:RadioCallSignID| cbc:RailCarID| cbc:ReferenceID|    cbc:ReferencedConsignmentID| cbc:RegistrationID| cbc:RegistrationNationalityID| cbc:ReleaseID| cbc:RequestForQuotationLineID| cbc:RequiredCustomsID| cbc:ResponseID| cbc:RevisedForecastLineID| cbc:SalesOrderID| cbc:SalesOrderLineID| cbc:SecurityID| cbc:SellerEventID|   cbc:SequenceID| cbc:SequenceNumberID| cbc:SerialID| cbc:ShippingOrderID| cbc:SignatureID| cbc:SpecificationID| cbc:SubscriberID| cbc:SuccessiveSequenceID| cbc:SupplierAssignedAccountID| cbc:TenderEnvelopeID| cbc:TraceID| cbc:TrackingID| cbc:TrainID|    cbc:TransportExecutionPlanReferenceID| cbc:UBLVersionID| cbc:UUID| cbc:UpperOrangeHazardPlacardID| cbc:ValidatedCriterionPropertyID| cbc:ValidatorID| cbc:VariantID| cbc:VersionID| cbc:VesselID| cbc:WeighingDeviceID" mode="M7" priority="1001">
    <svrl:fired-rule context="ext:ExtensionAgencyID | ext:ExtensionVersionID | cbc:AccountID| cbc:AdditionalAccountID|cbc:AgencyID| cbc:AircraftID| cbc:AttributeID| cbc:AwardID| cbc:AwardingCriterionID| cbc:BarcodeSymbologyID| cbc:BrokerAssignedID| cbc:BusinessClassificationEvidenceID|    cbc:BusinessIdentityEvidenceID| cbc:BuyerEventID| cbc:CV2ID| cbc:CarrierAssignedID| cbc:ChipApplicationID| cbc:CompanyID| cbc:ConsigneeAssignedID| cbc:ConsignorAssignedID| cbc:ConsumptionID| cbc:ConsumptionReportID| cbc:ContractFolderID| cbc:ContractedCarrierAssignedID|   cbc:CustomerAssignedAccountID| cbc:CustomizationID| cbc:DocumentID| cbc:ExchangeMarketID| cbc:ExpectedID| cbc:ExtendedID| cbc:FormatID| cbc:FreightForwarderAssignedID| cbc:HazardClassID| cbc:ID| cbc:IdentificationID| cbc:ImmobilizationCertificateID|   cbc:InstructionID| cbc:IssueNumberID| cbc:IssuerID| cbc:JourneyID| cbc:LanguageID| cbc:LicensePlateID| cbc:LineID| cbc:LoadingSequenceID| cbc:LocationID| cbc:LogoReferenceID| cbc:LotNumberID| cbc:LowerOrangeHazardPlacardID| cbc:MarkingID| cbc:MinimumImprovementBid|   cbc:NationalityID| cbc:NetworkID| cbc:OID| cbc:OpenTenderID| cbc:OriginalContractingSystemID| cbc:OriginalJobID| cbc:ParentDocumentID| cbc:ParentDocumentLineReferenceID| cbc:ParentDocumentVersionID| cbc:ParticipantID| cbc:PaymentID| cbc:PaymentMeansID|   cbc:PerformingCarrierAssignedID| cbc:PrepaidPaymentReferenceID| cbc:PreviousJobID| cbc:PreviousVersionID| cbc:PrimaryAccountNumberID| cbc:ProductTraceID| cbc:ProfileExecutionID| cbc:ProfileID| cbc:ProtocolID| cbc:RadioCallSignID| cbc:RailCarID| cbc:ReferenceID|    cbc:ReferencedConsignmentID| cbc:RegistrationID| cbc:RegistrationNationalityID| cbc:ReleaseID| cbc:RequestForQuotationLineID| cbc:RequiredCustomsID| cbc:ResponseID| cbc:RevisedForecastLineID| cbc:SalesOrderID| cbc:SalesOrderLineID| cbc:SecurityID| cbc:SellerEventID|   cbc:SequenceID| cbc:SequenceNumberID| cbc:SerialID| cbc:ShippingOrderID| cbc:SignatureID| cbc:SpecificationID| cbc:SubscriberID| cbc:SuccessiveSequenceID| cbc:SupplierAssignedAccountID| cbc:TenderEnvelopeID| cbc:TraceID| cbc:TrackingID| cbc:TrainID|    cbc:TransportExecutionPlanReferenceID| cbc:UBLVersionID| cbc:UUID| cbc:UpperOrangeHazardPlacardID| cbc:ValidatedCriterionPropertyID| cbc:ValidatorID| cbc:VariantID| cbc:VersionID| cbc:VesselID| cbc:WeighingDeviceID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(@schemeAgencyID) and normalize-space(@schemeAgencyID) != ''" />
      <xsl:otherwise>
        <svrl:failed-assert test="(@schemeAgencyID) and normalize-space(@schemeAgencyID) != ''">
          <xsl:attribute name="id">BR-OTH-02</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute schemeAgencyID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />', and it should not be empty.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:EndpointID" mode="M7" priority="1000">
    <svrl:fired-rule context="cbc:EndpointID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(@schemeID)" />
      <xsl:otherwise>
        <svrl:failed-assert test="(@schemeID)">
          <xsl:attribute name="id">BR-OTH-08</xsl:attribute>
          <xsl:attribute name="flag">error</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>An end-point identifier MUST have a scheme identifier attribute ('/cac:ServiceProviderParty/cac:Party/cbc:EndpointID/@schemeID').</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(@schemeAgencyID) and normalize-space(@schemeAgencyID) != ''" />
      <xsl:otherwise>
        <svrl:failed-assert test="(@schemeAgencyID) and normalize-space(@schemeAgencyID) != ''">
          <xsl:attribute name="id">BR-OTH-02.</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute schemeAgencyID is mandatory for the element: '<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />', and it should not be empty.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M7" priority="-1" />
  <xsl:template match="@*|node()" mode="M7" priority="-2">
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>