<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    <svrl:schematron-output schemaVersion="" title="Common Code lists Values Restrictions">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">BR-COM-CL-RESTR</xsl:attribute>
        <xsl:attribute name="name">BR-COM-CL-RESTR</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M3" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>Common Code lists Values Restrictions</svrl:text>

<!--PATTERN BR-COM-CL-RESTR-->
<xsl:variable name="selfcontained" select="if (/*[1]/cbc:QualificationApplicationTypeCode = 'SELFCONTAINED') then 'SELF-CONTAINED' else 'REGULATED'" />

	<!--RULE -->
<xsl:template match="cbc:ValueDataTypeCode" mode="M3" priority="1004">
    <svrl:fired-rule context="cbc:ValueDataTypeCode" />
    <xsl:variable name="gc" select="document('../../../../cl/gc/ResponseDataType-CodeList.gc')//SimpleCodeList" />
    <xsl:variable name="currentValue" select="." />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))">
          <xsl:attribute name="id">BR-COM-CL-RESTR-01.01</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The context of the value '<xsl:text />
            <xsl:value-of select="local-name()" />
            <xsl:text />=<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' does not match the type of 'cbc:QualificationApplicationTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:WeightingTypeCode" mode="M3" priority="1003">
    <svrl:fired-rule context="cbc:WeightingTypeCode" />
    <xsl:variable name="gc" select="document('../../../../cl/gc/WeightingType-CodeList.gc')//SimpleCodeList" />
    <xsl:variable name="currentValue" select="." />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))">
          <xsl:attribute name="id">BR-COM-CL-RESTR-01.02</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The context of the value '<xsl:text />
            <xsl:value-of select="local-name()" />
            <xsl:text />=<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' does not match the type of 'cbc:QualificationApplicationTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" mode="M3" priority="1002">
    <svrl:fired-rule context="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" />
    <xsl:variable name="gc" select="document('../../../../cl/gc/EvaluationMethodType-CodeList.gc')//SimpleCodeList" />
    <xsl:variable name="currentValue" select="." />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))">
          <xsl:attribute name="id">BR-COM-CL-RESTR-01.03</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The context of the value '<xsl:text />
            <xsl:value-of select="local-name()" />
            <xsl:text />=<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' does not match the type of 'cbc:QualificationApplicationTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:CriterionTypeCode" mode="M3" priority="1001">
    <svrl:fired-rule context="cbc:CriterionTypeCode" />
    <xsl:variable name="gc" select="document('../../../../cl/gc/ESPD-CriteriaTaxonomy_V2.1.0.gc')//SimpleCodeList" />
    <xsl:variable name="currentValue" select="." />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and contains($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'context']/SimpleValue, $selfcontained)))">
          <xsl:attribute name="id">BR-COM-CL-RESTR-01.04</xsl:attribute>
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The context of the value '<xsl:text />
            <xsl:value-of select="local-name()" />
            <xsl:text />=<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' does not match the type of 'cbc:QualificationApplicationTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" mode="M3" priority="1000">
    <svrl:fired-rule context="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" />
    <xsl:variable name="gc" select="document('../../../../cl/gc/EORoleType-CodeList.gc')//SimpleCodeList" />
    <xsl:variable name="currentValue" select="." />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and ($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'status']/SimpleValue = 'ACTIVE')))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(not(exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue])) or (exists($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]) and ($gc/Row[Value[@ColumnRef = 'code']/SimpleValue = $currentValue]/Value[@ColumnRef = 'status']/SimpleValue = 'ACTIVE')))">
          <xsl:attribute name="id">BR-COM-CL-RESTR-02.01</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The status of the value '<xsl:text />
            <xsl:value-of select="local-name()" />
            <xsl:text />=<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' is deprecated, instead, use a code in this same code list with the status='ACTIVE'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M3" priority="-1" />
  <xsl:template match="@*|node()" mode="M3" priority="-2">
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
