<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:ccv="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1" xmlns:ccv-cbc="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1" xmlns:cev="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1" xmlns:cev-cbc="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1" xmlns:espd-req="urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:gc="http://docs.oasis-open.org/codelist/ns/genericode/1.0/" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    <svrl:schematron-output schemaVersion="" title="Common Business Rules assertions">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="ccv-cbc" uri="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1" />
      <svrl:ns-prefix-in-attribute-values prefix="cev-cbc" uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1" />
      <svrl:ns-prefix-in-attribute-values prefix="cev" uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1" />
      <svrl:ns-prefix-in-attribute-values prefix="ext" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="ccv" uri="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1" />
      <svrl:ns-prefix-in-attribute-values prefix="espd-req" uri="urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1" />
      <svrl:ns-prefix-in-attribute-values prefix="gc" uri="http://docs.oasis-open.org/codelist/ns/genericode/1.0/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">common-br-rules</xsl:attribute>
        <xsl:attribute name="name">common-br-rules</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M10" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>Common Business Rules assertions</svrl:text>

<!--PATTERN common-br-rules-->


	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/cac:ProcurementProjectLot" mode="M10" priority="1009">
    <svrl:fired-rule context="/espd-req:ESPDRequest/cac:ProcurementProjectLot" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(cbc:ID!='0') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(cbc:ID!='0') )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The current version of the ESPD Service does not allow for the specification of multiple lots. To ensure conformace with the current version of the ESPD Service the ID sub-element inside the element ProcurementProjectLot MUST be 0 (zero).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:variable name="TypeCodeExclusion" select="/espd-req:ESPDRequest/ccv:Criterion/cbc:TypeCode[starts-with(., 'CRITERION.EXCLUSION.')]" />

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest" mode="M10" priority="1007">
    <svrl:fired-rule context="/espd-req:ESPDRequest" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:ID))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:ID))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'espd-req:ESPDRequest / cbc:ID' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:IssueDate))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:IssueDate))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'espd-req:ESPDRequest / cbc:IssueDate' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="count( cac:AdditionalDocumentReference ) > 0" />
      <xsl:otherwise>
        <svrl:failed-assert test="count( cac:AdditionalDocumentReference ) > 0">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The ESPDRequest MUST always contain one 'AdditionalDocumentReference' element referring to the Contract Notice (CN) published in TeD (Publications Office). </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/ccv:Criterion" mode="M10" priority="1006">
    <svrl:fired-rule context="/espd-req:ESPDRequest/ccv:Criterion" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:ID))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:ID))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:ID' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:Name))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:Name))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:Name' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:Description))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:Description))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:Description' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:TypeCode))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:TypeCode))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:TypeCode' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="not( (count(ccv:LegislationReference) = 0) and (contains(cbc:TypeCode, 'CRITERION.EXCLUSION.'))  )" />
      <xsl:otherwise>
        <svrl:failed-assert test="not( (count(ccv:LegislationReference) = 0) and (contains(cbc:TypeCode, 'CRITERION.EXCLUSION.')) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>All Exclusion Criteria MUST provide a value for the element LegislationReference </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/ccv:Criterion/ccv:RequirementGroup" mode="M10" priority="1005">
    <svrl:fired-rule context="/espd-req:ESPDRequest/ccv:Criterion/ccv:RequirementGroup" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:ID))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:ID))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:ID' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/ccv:Criterion/ccv:RequirementGroup/ccv:Requirement" mode="M10" priority="1004">
    <svrl:fired-rule context="/espd-req:ESPDRequest/ccv:Criterion/ccv:RequirementGroup/ccv:Requirement" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:Description))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:Description))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:Description' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:ID))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:ID))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:ID' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(@responseDataType))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(@responseDataType))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The attribute 'responseDataType' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/ccv:Criterion/ccv:LegislationReference" mode="M10" priority="1003">
    <svrl:fired-rule context="/espd-req:ESPDRequest/ccv:Criterion/ccv:LegislationReference" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:URI))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:URI))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'ccv-cbc:URI' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(ccv-cbc:Article))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(ccv-cbc:Article))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'ccv-cbc:Article' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(ccv-cbc:JurisdictionLevelCode))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(ccv-cbc:JurisdictionLevelCode))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'ccv-cbc:JurisdictionLevelCode' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(ccv-cbc:Title))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(ccv-cbc:Title))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'ccv-cbc:Title' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:Description))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:Description))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The element 'cbc:Description' is mandatory </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="ccv:RequirementGroup/cbc:ID | ccv:RequirementGroup/ccv:Requirement/cbc:ID" mode="M10" priority="1002">
    <svrl:fired-rule context="ccv:RequirementGroup/cbc:ID | ccv:RequirementGroup/ccv:Requirement/cbc:ID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(@schemeAgencyID!='EU-COM-GROW') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(@schemeAgencyID!='EU-COM-GROW') )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The value of the attribute 'schemeAgencyID' must be 'EU-COM-GROW' </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(@schemeID!='CriterionRelatedIDs') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(@schemeID!='CriterionRelatedIDs') )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The value of the attribute 'schemeID' must be 'CriterionRelatedIDs' </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(@schemeVersionID!='1.0') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(@schemeVersionID!='1.0') )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The value of the attribute 'schemeVersionID' must be '1.0' </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="ccv:Criterion/cbc:ID" mode="M10" priority="1001">
    <svrl:fired-rule context="ccv:Criterion/cbc:ID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(@schemeAgencyID!='EU-COM-GROW') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(@schemeAgencyID!='EU-COM-GROW') )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The value of the attribute 'schemeAgencyID' must be 'EU-COM-GROW' </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="/espd-req:ESPDRequest/cac:AdditionalDocumentReference" mode="M10" priority="1000">
    <svrl:fired-rule context="/espd-req:ESPDRequest/cac:AdditionalDocumentReference" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( not(string(cbc:ID))=false() )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( not(string(cbc:ID))=false() )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The AdditionalDocumentReference referring to the CN published in TeD MUST use the element ID to identify the CN. </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(count(cbc:ID)=0) or not(cbc:DocumentTypeCode/text() = 'TED_CN') or ((count(cbc:ID))=1 and (string-length(cbc:ID)=17 and substring(cbc:ID,5,3)='/S ' and substring(cbc:ID,11,1)='-'))" />
      <xsl:otherwise>
        <svrl:failed-assert test="(count(cbc:ID)=0) or not(cbc:DocumentTypeCode/text() = 'TED_CN') or ((count(cbc:ID))=1 and (string-length(cbc:ID)=17 and substring(cbc:ID,5,3)='/S ' and substring(cbc:ID,11,1)='-'))">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The value of the ID MUST follow the pattern YYYY/S DDD-DDDDDD, where 'YYYY' is a year, '/S' is a constant label, and 'D' represent digits. Beware of the existence of a space (a blank) between the 'S' and the first 'D'. </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M10" priority="-1" />
  <xsl:template match="@*|node()" mode="M10" priority="-2">
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
