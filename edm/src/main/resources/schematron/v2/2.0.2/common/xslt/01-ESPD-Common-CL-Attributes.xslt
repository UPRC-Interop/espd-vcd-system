<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:X-test:UBL:Pre-award:CommonAggregate" xmlns:cbc="urn:X-test:UBL:Pre-award:CommonBasic" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    <svrl:schematron-output schemaVersion="" title="Common Criterion Business Rules">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:X-test:UBL:Pre-award:CommonAggregate" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:X-test:UBL:Pre-award:CommonBasic" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">BR-COM-CL-ATT</xsl:attribute>
        <xsl:attribute name="name">BR-COM-CL-ATT</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M3" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>Common Criterion Business Rules</svrl:text>

<!--PATTERN BR-COM-CL-ATT-->


	<!--RULE -->
<xsl:template match="cbc:ConfidentialityLevelCode" mode="M3" priority="1015">
    <svrl:fired-rule context="cbc:ConfidentialityLevelCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'ConfidentialityLevel'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'ConfidentialityLevel'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = ConfidentialityLevel'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'ConfidentialityLevel'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'ConfidentialityLevel'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = ConfidentialityLevel'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ConfidentialityLevel.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ConfidentialityLevel.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ConfidentialityLevel.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:Country/cbc:IdentificationCode" mode="M3" priority="1014">
    <svrl:fired-rule context="cac:Country/cbc:IdentificationCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'CountryCodeIdentifier'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'CountryCodeIdentifier'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = CountryCodeIdentifier'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'CountryCodeIdentifier'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'CountryCodeIdentifier'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = CountryCodeIdentifier'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listVersionID != '1.0'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listVersionID != '1.0'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listVersionID = 1.0'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'Placeholder'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'Placeholder'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = Placeholder'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'Placeholder'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'Placeholder'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = Placeholder'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'ISO'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'ISO'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = ISO'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'ISO'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'ISO'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = ISO'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:TypeCode" mode="M3" priority="1013">
    <svrl:fired-rule context="cbc:TypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'CriterionElementType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'CriterionElementType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = CriterionElementType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'CriterionElementType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'CriterionElementType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = CriterionElementType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CriterionElementType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CriterionElementType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CriterionElementType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ValueCurrencyCode" mode="M3" priority="1012">
    <svrl:fired-rule context="cbc:ValueCurrencyCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'CurrencyCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'CurrencyCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = CurrencyCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'CurrencyCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'CurrencyCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = CurrencyCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listVersionID != '1.0'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listVersionID != '1.0'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listVersionID = 1.0'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CurrencyCode.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CurrencyCode.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/CurrencyCode.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = http://publications.europa.eu/mdr/authority/index.html'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'Publications Office of the EU'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'Publications Office of the EU'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = Publications Office of the EU'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-OP'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-OP'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-OP'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:AdditionalReferenceDocument/cbc:DocumentTypeCode" mode="M3" priority="1011">
    <svrl:fired-rule context="cac:AdditionalReferenceDocument/cbc:DocumentTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'DocRefContentType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'DocRefContentType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = DocRefContentType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'DocRefContentType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'DocRefContentType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = DocRefContentType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/DocRefContentType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/DocRefContentType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/DocRefContentType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:EconomicOperatorParty/cac:Party/cbc:IndustryClassificationCode" mode="M3" priority="1010">
    <svrl:fired-rule context="cac:EconomicOperatorParty/cac:Party/cbc:IndustryClassificationCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'EOIndustryClassificationCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'EOIndustryClassificationCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = EOIndustryClassificationCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'EOIndustryClassificationCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'EOIndustryClassificationCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = EOIndustryClassificationCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EOIndustryClassificationCode.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EOIndustryClassificationCode.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EOIndustryClassificationCode.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" mode="M3" priority="1009">
    <svrl:fired-rule context="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'EORoleType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'EORoleType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = EORoleType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'EORoleType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'EORoleType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = EORoleType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EORoleType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EORoleType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EORoleType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" mode="M3" priority="1008">
    <svrl:fired-rule context="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'EvaluationMethodType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'EvaluationMethodType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = EvaluationMethodType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'EvaluationMethodType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'EvaluationMethodType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = EvaluationMethodType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EvaluationMethodType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EvaluationMethodType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/EvaluationMethodType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProcedureCode" mode="M3" priority="1007">
    <svrl:fired-rule context="cbc:ProcedureCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'ProcedureType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'ProcedureType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = ProcedureType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'ProcedureType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'ProcedureType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = ProcedureType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listVersionID != '1.0'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listVersionID != '1.0'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listVersionID = 1.0'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProcedyreType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProcedyreType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProcedyreType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = http://publications.europa.eu/mdr/authority/index.html'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'Publications Office of the EU'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'Publications Office of the EU'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = Publications Office of the EU'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-OP'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-OP'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-OP'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProcurementTypeCode" mode="M3" priority="1006">
    <svrl:fired-rule context="cbc:ProcurementTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'ProjectType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'ProjectType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = ProjectType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'ProjectType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'ProjectType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = ProjectType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listVersionID != '1.0'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listVersionID != '1.0'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listVersionID = 1.0'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProjectType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProjectType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ProjectType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = http://publications.europa.eu/mdr/authority/index.html'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'Publications Office of the EU'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'Publications Office of the EU'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = Publications Office of the EU'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-OP'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-OP'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-OP'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterionPropertyGroup/cbc:PropertyGroupTypeCode" mode="M3" priority="1005">
    <svrl:fired-rule context="cac:TenderingCriterionPropertyGroup/cbc:PropertyGroupTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'PropertyGroupType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'PropertyGroupType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = PropertyGroupType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'PropertyGroupType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'PropertyGroupType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = PropertyGroupType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/PropertyGroupType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/PropertyGroupType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/PropertyGroupType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:QualificationApplicationTypeCode" mode="M3" priority="1004">
    <svrl:fired-rule context="cbc:QualificationApplicationTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'QualificationApplicationType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'QualificationApplicationType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = QualificationApplicationType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'QualificationApplicationType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'QualificationApplicationType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = QualificationApplicationType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/QualificationApplicationType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/QualificationApplicationType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/QualificationApplicationType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterionProperty/cbc:ValueDataTypeCode" mode="M3" priority="1003">
    <svrl:fired-rule context="cac:TenderingCriterionProperty/cbc:ValueDataTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'ResponseDataType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'ResponseDataType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = ResponseDataType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'ResponseDataType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'ResponseDataType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = ResponseDataType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ResponseDataType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ResponseDataType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ResponseDataType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:ProcurementProject/cbc:ProcurementSubTypeCode" mode="M3" priority="1002">
    <svrl:fired-rule context="cac:ProcurementProject/cbc:ProcurementSubTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'ServicesProjectSubType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'ServicesProjectSubType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = ServicesProjectSubType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'ServicesProjectSubType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'ServicesProjectSubType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = ServicesProjectSubType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listVersionID != '1.0'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listVersionID != '1.0'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listVersionID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listVersionID = 1.0'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ServicesProjectSubType.gc'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listSchemeURI != 'https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ServicesProjectSubType.gc'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listSchemeURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listSchemeURI = https://github.com/ESPD/ESPD-EDM/tree/ESPD-EDM-V2.0.2/docs/src/main/asciidoc/dist/cl/gc/ServicesProjectSubType.gc'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'http://publications.europa.eu/mdr/authority/index.html'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = http://publications.europa.eu/mdr/authority/index.html'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'Publications Office of the EU'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'Publications Office of the EU'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = Publications Office of the EU'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-OP'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-OP'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-OP'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:CriterionTypeCode" mode="M3" priority="1001">
    <svrl:fired-rule context="cbc:CriterionTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'CriteriaTypeCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'CriteriaTypeCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = CriteriaTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'CriteriaTypeCode'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'CriteriaTypeCode'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = CriteriaTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:WeightingTypeCode" mode="M3" priority="1000">
    <svrl:fired-rule context="cbc:WeightingTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listName != 'WeightingType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listName != 'WeightingType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listName = WeightingType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listID != 'WeightingType'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listID != 'WeightingType'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listID = WeightingType'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listURI != 'https://github.com/ESPD/ESPD-EDM'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listURI" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listURI = https://github.com/ESPD/ESPD-EDM'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyName != 'DG GROW (European Commission)'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyName" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyName = DG GROW (European Commission)'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(false() or not(@listAgencyID != 'EU-COM-GROW'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(false() or not(@listAgencyID != 'EU-COM-GROW'))">
          <xsl:attribute name="flag">fatal</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid codelist attribute value '<xsl:text />
            <xsl:value-of select="@listAgencyID" />
            <xsl:text />'. The element '<xsl:text />
            <xsl:value-of select="name(ancestor::*[1])" />
            <xsl:text />/<xsl:text />
            <xsl:value-of select="name()" />
            <xsl:text />' must have the following attribute and value: 'listAgencyID = EU-COM-GROW'.</svrl:text>
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
