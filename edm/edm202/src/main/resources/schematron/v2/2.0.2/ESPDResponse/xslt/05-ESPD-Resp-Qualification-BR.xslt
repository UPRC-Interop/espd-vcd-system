<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate" xmlns:cbc="urn:X-test:UBL:Pre-award:CommonBasic" xmlns:espd="urn:X-test:UBL:Pre-award:QualificationApplicationResponse" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
<xsl:key match="cbc:RoleCode" name="EOrole" use="." />

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
    <svrl:schematron-output schemaVersion="" title="ESPD Response pre-qualification system Business Rules">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:X-test:UBL:Pre-award:CommonAggregate" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:X-test:UBL:Pre-award:CommonBasic" />
      <svrl:ns-prefix-in-attribute-values prefix="ext" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="espd" uri="urn:X-test:UBL:Pre-award:QualificationApplicationResponse" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">BR-RESP-QUAL</xsl:attribute>
        <xsl:attribute name="name">BR-RESP-QUAL</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M6" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>ESPD Response pre-qualification system Business Rules</svrl:text>

<!--PATTERN BR-RESP-QUAL-->


	<!--RULE -->
<xsl:template match="espd:QualificationApplicationResponse" mode="M6" priority="1000">
    <svrl:fired-rule context="espd:QualificationApplicationResponse" />
    <xsl:variable name="isPQS" select="(cac:EconomicOperatorParty/cac:QualifyingParty/cac:Party/cac:PartyIdentification/cbc:ID)" />
    <xsl:variable name="isOENRON" select="count(key('EOrole', 'OENRON'))=1" />
    <xsl:variable name="allResponses" select="cac:TenderingCriterionResponse/cbc:ValidatedCriterionPropertyID" />
    <xsl:variable name="exclusionCriteria" select="cac:TenderingCriterion[starts-with(cbc:CriterionTypeCode, 'CRITERION.EXCLUSION.')]" />
    <xsl:variable name="exclusionResponses" select="$exclusionCriteria[cac:TenderingCriterionPropertyGroup/cac:TenderingCriterionProperty[cbc:ID = $allResponses]]/cbc:CriterionTypeCode" />
    <xsl:variable name="exclusionNotResponses" select="$exclusionCriteria[cac:TenderingCriterionPropertyGroup/cac:TenderingCriterionProperty[not(cbc:ID = $allResponses)]]/cbc:CriterionTypeCode/text()" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="($isPQS) or(not($isPQS) and (count($exclusionCriteria) = count($exclusionResponses)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="($isPQS) or(not($isPQS) and (count($exclusionCriteria) = count($exclusionResponses)) )">
          <xsl:attribute name="id">BR-RESP-30</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Information about compliance of exclusion grounds MUST be provided. The following exclusion criterion are not provided: <xsl:text />
            <xsl:value-of select="$exclusionNotResponses" />
            <xsl:text />
</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="selectionCriteria" select="cac:TenderingCriterion[starts-with(cbc:CriterionTypeCode, 'CRITERION.SELECTION.')]" />
    <xsl:variable name="selectionResponses" select="$selectionCriteria[cac:TenderingCriterionPropertyGroup/cac:TenderingCriterionProperty[cbc:ID = $allResponses]]/cbc:CriterionTypeCode" />
    <xsl:variable name="selectionNotResponses" select="$selectionCriteria[cac:TenderingCriterionPropertyGroup/cac:TenderingCriterionProperty[not(cbc:ID = $allResponses)]]/cbc:CriterionTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(($isPQS) and not($isOENRON)) or (not(($isPQS) and not($isOENRON)) and (count($selectionCriteria) = count($selectionResponses)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="(($isPQS) and not($isOENRON)) or (not(($isPQS) and not($isOENRON)) and (count($selectionCriteria) = count($selectionResponses)) )">
          <xsl:attribute name="id">BR-RESP-40</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Information about compliance of selection criteria MUST be provided. The following selection criterion are not provided: <xsl:text />
            <xsl:value-of select="$selectionNotResponses" />
            <xsl:text />
</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="hasServiceProvider" select="(cac:ContractingParty/cac:Party/cac:ServiceProviderParty)" />
    <xsl:variable name="testS10" select="$isPQS and not($isOENRON) and $hasServiceProvider" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="not($testS10) or ($testS10 and (count($selectionCriteria) = count($selectionResponses)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="not($testS10) or ($testS10 and (count($selectionCriteria) = count($selectionResponses)) )">
          <xsl:attribute name="id">BR-RESP-80-S10</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>When the pre-qualification system the EO is registered on does not cover all the selection criteria, information about compliance of selection criteria MUST be provided. The following selection criterion are not provided: <xsl:text />
            <xsl:value-of select="$selectionNotResponses" />
            <xsl:text />
</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="testS20" select="$isPQS and not($isOENRON) and not($hasServiceProvider)" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="not($testS20) or ($testS20 and (count($selectionResponses) = 0) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="not($testS20) or ($testS20 and (count($selectionResponses) = 0) )">
          <xsl:attribute name="id">BR-RESP-80-S20</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>When the pre-qualification system the EO is registered on covers all the selection criteria, information about compliance of selection criteria IS NOT required.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M6" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M6" priority="-1" />
  <xsl:template match="@*|node()" mode="M6" priority="-2">
    <xsl:apply-templates mode="M6" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
