<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:espd-req="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2" xmlns:espd-resp="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationResponse-2" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    <svrl:schematron-output schemaVersion="" title="ESPD Code List Business Rules">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="ext" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="espd-req" uri="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationRequest-2" />
      <svrl:ns-prefix-in-attribute-values prefix="espd-resp" uri="urn:oasis:names:specification:ubl:schema:xsd:QualificationApplicationResponse-2" />
      <svrl:ns-prefix-in-attribute-values prefix="fn" uri="http://www.w3.org/2005/xpath-functions" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">code-list-values</xsl:attribute>
        <xsl:attribute name="name">code-list-values</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M7" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>ESPD Code List Business Rules</svrl:text>

<!--PATTERN code-list-values-->


	<!--RULE -->
<xsl:template match="cbc:ConfidentialityLevelCode" mode="M7" priority="1018">
    <svrl:fired-rule context="cbc:ConfidentialityLevelCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('CONFIDENTIALPUBLIC','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('CONFIDENTIALPUBLIC','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:ConfidentialityLevelCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:Country/cbc:IdentificationCode" mode="M7" priority="1017">
    <svrl:fired-rule context="cac:Country/cbc:IdentificationCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('ADAEAFAGAIALAMAOAQARASATAUAWAXAZBABBBDBEBFBGBHBIBJBLBMBNBOBQBRBSBTBVBWBYBZCACCCDCFCGCHCICKCLCMCNCOCRCUCVCWCXCYCZDEDJDKDMDODZECEEEGEHERESETFIFJFKFMFOFRGAGBGDGEGFGGGHGIGLGMGNGPGQGRGSGTGUGWGYHKHMHNHRHTHUIDIEILIMINIOIQIRISITJEJMJOJPKEKGKHKIKMKNKPKRKWKYKZLALBLCLILKLRLSLTLULVLYMAMCMDMEMFMGMHMKMLMMMNMOMPMQMRMSMTMUMVMWMXMYMZNANCNENFNGNINLNONPNRNUNZOMPAPEPFPGPHPKPLPMPNPRPSPTPWPYQARERORSRURWSASBSCSDSESGSHSISJSKSLSMSNSOSRSSSTSVSXSYSZTCTDTFTGTHTJTKTLTMTNTOTRTTTVTWTZUAUGUMUSUYUZVAVCVEVGVIVNVUWFWSXKYEYTZAZMZW','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('ADAEAFAGAIALAMAOAQARASATAUAWAXAZBABBBDBEBFBGBHBIBJBLBMBNBOBQBRBSBTBVBWBYBZCACCCDCFCGCHCICKCLCMCNCOCRCUCVCWCXCYCZDEDJDKDMDODZECEEEGEHERESETFIFJFKFMFOFRGAGBGDGEGFGGGHGIGLGMGNGPGQGRGSGTGUGWGYHKHMHNHRHTHUIDIEILIMINIOIQIRISITJEJMJOJPKEKGKHKIKMKNKPKRKWKYKZLALBLCLILKLRLSLTLULVLYMAMCMDMEMFMGMHMKMLMMMNMOMPMQMRMSMTMUMVMWMXMYMZNANCNENFNGNINLNONPNRNUNZOMPAPEPFPGPHPKPLPMPNPRPSPTPWPYQARERORSRURWSASBSCSDSESGSHSISJSKSLSMSNSOSRSSSTSVSXSYSZTCTDTFTGTHTJTKTLTMTNTOTRTTTVTWTZUAUGUMUSUYUZVAVCVEVGVIVNVUWFWSXKYEYTZAZMZW','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:Country/cbc:IdentificationCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:TypeCode" mode="M7" priority="1016">
    <svrl:fired-rule context="cbc:TypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('CRITERIONSUBCRITERIONREQUIREMENT_GROUPREQUIREMENT_SUBGROUPREQUIREMENTQUESTION_GROUPQUESTION_SUBGROUPQUESTIONCAPTIONADDITIONAL_DESCRIPTION_LINELEGISLATION','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('CRITERIONSUBCRITERIONREQUIREMENT_GROUPREQUIREMENT_SUBGROUPREQUIREMENTQUESTION_GROUPQUESTION_SUBGROUPQUESTIONCAPTIONADDITIONAL_DESCRIPTION_LINELEGISLATION','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:TypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="@currencyID" mode="M7" priority="1015">
    <svrl:fired-rule context="@currencyID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('AEDAFNALLAMDANGAOAARSAUDAWGAZNBAMBBDBDTBGNBHDBIFBMDBNDBOBBRLBSDBTNBWPBZDCADCDFCHFCLPCNYCOPCRCCUCCUPCVECZKDJFDKKDOPDZDEEKEGPERNETBEURFJDFKPGBPGELGHSGIPGMDGNFGTQGYDHKDHNLHRKHTGHUFIDRILSINRIQDIRRISKJMDJODJPYKESKGSKHRKMFKPWKRWKWDKYDKZTLAKLBPLKRLRDLSLLTLLVLLYDMADMDLMGAMKDMMKMNTMOPMROMURMVRMWKMXNMYRMZNNADNGNNIONOKNPRNZDOMRPABPENPGKPHPPKRPLNPYGQARRONRSDRUBRWFSARSBDSCRSDGSEKSGDSHPSLLSOSSRDSTDSYPSZLTHBTJSTMTTNDTOPTRYTTDTWDTZSUAHUGXUSDUSNUSSUYUUZSVEFVNDVUVWSTXAFXCDXOFXPFXTSXXXYERZARZMKZWLSQSTVDADFADPATSBEFCYPDEMESPFIMFRFGRDIEPITLLUFMCFMTLNLGPTESITSKKSMLVALXEUAFAALKAONAORARMARLARPARAAZMBGJBGKBGLBOPBRBBRCBREBRNBRRBRZCLERSDCSJCSKDDMECSGQEGNEGHCGWPILPILRISJLAJMGFMKNMLFMVQMXPMZMNFDPEHPEIPLZROLRURSDDSRGSURSVCTJRTMMTPETRLUAKUGSUYNVEBVNCYDDYUSYUFYUDYUNYURYUOYUGYUMZRNZRZZWCZWDZWNZWRMTPSSPZMWPENBYRBYN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('AEDAFNALLAMDANGAOAARSAUDAWGAZNBAMBBDBDTBGNBHDBIFBMDBNDBOBBRLBSDBTNBWPBZDCADCDFCHFCLPCNYCOPCRCCUCCUPCVECZKDJFDKKDOPDZDEEKEGPERNETBEURFJDFKPGBPGELGHSGIPGMDGNFGTQGYDHKDHNLHRKHTGHUFIDRILSINRIQDIRRISKJMDJODJPYKESKGSKHRKMFKPWKRWKWDKYDKZTLAKLBPLKRLRDLSLLTLLVLLYDMADMDLMGAMKDMMKMNTMOPMROMURMVRMWKMXNMYRMZNNADNGNNIONOKNPRNZDOMRPABPENPGKPHPPKRPLNPYGQARRONRSDRUBRWFSARSBDSCRSDGSEKSGDSHPSLLSOSSRDSTDSYPSZLTHBTJSTMTTNDTOPTRYTTDTWDTZSUAHUGXUSDUSNUSSUYUUZSVEFVNDVUVWSTXAFXCDXOFXPFXTSXXXYERZARZMKZWLSQSTVDADFADPATSBEFCYPDEMESPFIMFRFGRDIEPITLLUFMCFMTLNLGPTESITSKKSMLVALXEUAFAALKAONAORARMARLARPARAAZMBGJBGKBGLBOPBRBBRCBREBRNBRRBRZCLERSDCSJCSKDDMECSGQEGNEGHCGWPILPILRISJLAJMGFMKNMLFMVQMXPMZMNFDPEHPEIPLZROLRURSDDSRGSURSVCTJRTMMTPETRLUAKUGSUYNVEBVNCYDDYUSYUFYUDYUNYURYUOYUGYUMZRNZRZZWCZWDZWNZWRMTPSSPZMWPENBYRBYN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the attribute '@currencyID'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ValueCurrencyCode" mode="M7" priority="1014">
    <svrl:fired-rule context="cbc:ValueCurrencyCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('AEDAFNALLAMDANGAOAARSAUDAWGAZNBAMBBDBDTBGNBHDBIFBMDBNDBOBBRLBSDBTNBWPBZDCADCDFCHFCLPCNYCOPCRCCUCCUPCVECZKDJFDKKDOPDZDEEKEGPERNETBEURFJDFKPGBPGELGHSGIPGMDGNFGTQGYDHKDHNLHRKHTGHUFIDRILSINRIQDIRRISKJMDJODJPYKESKGSKHRKMFKPWKRWKWDKYDKZTLAKLBPLKRLRDLSLLTLLVLLYDMADMDLMGAMKDMMKMNTMOPMROMURMVRMWKMXNMYRMZNNADNGNNIONOKNPRNZDOMRPABPENPGKPHPPKRPLNPYGQARRONRSDRUBRWFSARSBDSCRSDGSEKSGDSHPSLLSOSSRDSTDSYPSZLTHBTJSTMTTNDTOPTRYTTDTWDTZSUAHUGXUSDUSNUSSUYUUZSVEFVNDVUVWSTXAFXCDXOFXPFXTSXXXYERZARZMKZWLSQSTVDADFADPATSBEFCYPDEMESPFIMFRFGRDIEPITLLUFMCFMTLNLGPTESITSKKSMLVALXEUAFAALKAONAORARMARLARPARAAZMBGJBGKBGLBOPBRBBRCBREBRNBRRBRZCLERSDCSJCSKDDMECSGQEGNEGHCGWPILPILRISJLAJMGFMKNMLFMVQMXPMZMNFDPEHPEIPLZROLRURSDDSRGSURSVCTJRTMMTPETRLUAKUGSUYNVEBVNCYDDYUSYUFYUDYUNYURYUOYUGYUMZRNZRZZWCZWDZWNZWRMTPSSPZMWPENBYRBYN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('AEDAFNALLAMDANGAOAARSAUDAWGAZNBAMBBDBDTBGNBHDBIFBMDBNDBOBBRLBSDBTNBWPBZDCADCDFCHFCLPCNYCOPCRCCUCCUPCVECZKDJFDKKDOPDZDEEKEGPERNETBEURFJDFKPGBPGELGHSGIPGMDGNFGTQGYDHKDHNLHRKHTGHUFIDRILSINRIQDIRRISKJMDJODJPYKESKGSKHRKMFKPWKRWKWDKYDKZTLAKLBPLKRLRDLSLLTLLVLLYDMADMDLMGAMKDMMKMNTMOPMROMURMVRMWKMXNMYRMZNNADNGNNIONOKNPRNZDOMRPABPENPGKPHPPKRPLNPYGQARRONRSDRUBRWFSARSBDSCRSDGSEKSGDSHPSLLSOSSRDSTDSYPSZLTHBTJSTMTTNDTOPTRYTTDTWDTZSUAHUGXUSDUSNUSSUYUUZSVEFVNDVUVWSTXAFXCDXOFXPFXTSXXXYERZARZMKZWLSQSTVDADFADPATSBEFCYPDEMESPFIMFRFGRDIEPITLLUFMCFMTLNLGPTESITSKKSMLVALXEUAFAALKAONAORARMARLARPARAAZMBGJBGKBGLBOPBRBBRCBREBRNBRRBRZCLERSDCSJCSKDDMECSGQEGNEGHCGWPILPILRISJLAJMGFMKNMLFMVQMXPMZMNFDPEHPEIPLZROLRURSDDSRGSURSVCTJRTMMTPETRLUAKUGSUYNVEBVNCYDDYUSYUFYUDYUNYURYUOYUGYUMZRNZRZZWCZWDZWNZWRMTPSSPZMWPENBYRBYN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:ValueCurrencyCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:AdditionalReferenceDocument/cbc:DocumentTypeCode" mode="M7" priority="1013">
    <svrl:fired-rule context="cac:AdditionalReferenceDocument/cbc:DocumentTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('TED_CNESPD_REQUESTTECC016TECC017TECC018TECC019TECC020TECC021TECC022TECC024OtherNOJCNROJCNLOJCNNOJPINROJPINLOJPINNOJCANROJCANLOJCAN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('TED_CNESPD_REQUESTTECC016TECC017TECC018TECC019TECC020TECC021TECC022TECC024OtherNOJCNROJCNLOJCNNOJPINROJPINLOJPINNOJCANROJCANLOJCAN','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:AdditionalReferenceDocument/cbc:DocumentTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:PartyIdentification/cbc:ID/@schemeID" mode="M7" priority="1012">
    <svrl:fired-rule context="cac:PartyIdentification/cbc:ID/@schemeID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('NATIONALVATLEIBRISDUNS','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('NATIONALVATLEIBRISDUNS','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the attribute 'cac:PartyIdentification/cbc:ID/@schemeID'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:EconomicOperatorParty/cac:Party/cbc:IndustryClassificationCode" mode="M7" priority="1011">
    <svrl:fired-rule context="cac:EconomicOperatorParty/cac:Party/cbc:IndustryClassificationCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('MICROSMALLMEDIUMSMELARGE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('MICROSMALLMEDIUMSMELARGE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:EconomicOperatorParty/cac:Party/cbc:IndustryClassificationCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" mode="M7" priority="1010">
    <svrl:fired-rule context="cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('SCLEGMOERONOENRONSCLE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('SCLEGMOERONOENRONSCLE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:EconomicOperatorParty/cac:EconomicOperatorRole/cbc:RoleCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="@languageID" mode="M7" priority="1009">
    <svrl:fired-rule context="@languageID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('PLPTELENESETFIROGAFRSKSLSVHUITLTLVMTBGNLDACSDEHR','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('PLPTELENESETFIROGAFRSKSLSVHUITLTLVMTBGNLDACSDEHR','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the attribute '@languageID'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" mode="M7" priority="1008">
    <svrl:fired-rule context="cac:TenderingCriterion/cbc:EvaluationMethodTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('PASSFAILWEIGHTED','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('PASSFAILWEIGHTED','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:TenderingCriterion/cbc:EvaluationMethodTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProcedureCode" mode="M7" priority="1007">
    <svrl:fired-rule context="cbc:ProcedureCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('PRIOROPENRESTRICTEDRESTRICTED_ACCELERATEDNEGOTIATEDOPEN_RECURNEGOTIATED_ACCELERATEDAWARDINFOAWARD_DIRECTCOMP_NEGOTIATIONCOMP_DIALOGUEDESIGN_CONTESTCONCESSIONCONCESSION_WO_PUBINNOVATIONAMINOT_SPECIFIEDQUALCONTESTS_RESULTNEGOTIATED_WO_CALLAWARD_WO_PUBOTHERNOT_APPLICABLENOT_SPECIFIEDLOVVDL','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('PRIOROPENRESTRICTEDRESTRICTED_ACCELERATEDNEGOTIATEDOPEN_RECURNEGOTIATED_ACCELERATEDAWARDINFOAWARD_DIRECTCOMP_NEGOTIATIONCOMP_DIALOGUEDESIGN_CONTESTCONCESSIONCONCESSION_WO_PUBINNOVATIONAMINOT_SPECIFIEDQUALCONTESTS_RESULTNEGOTIATED_WO_CALLAWARD_WO_PUBOTHERNOT_APPLICABLENOT_SPECIFIEDLOVVDL','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:ProcedureCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProfileExecutionID" mode="M7" priority="1006">
    <svrl:fired-rule context="cbc:ProfileExecutionID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('ESPD-EDMv2.0.0-REGULATEDESPD-EDMv2.0.0-SELFCONTAINEDESPD-EDMv2.1.0-REGULATEDESPD-EDMv2.1.0-SELFCONTAINEDESPD-EDMv1.0.2','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('ESPD-EDMv2.0.0-REGULATEDESPD-EDMv2.0.0-SELFCONTAINEDESPD-EDMv2.1.0-REGULATEDESPD-EDMv2.1.0-SELFCONTAINEDESPD-EDMv1.0.2','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:ProfileExecutionID'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:ProcurementTypeCode" mode="M7" priority="1005">
    <svrl:fired-rule context="cbc:ProcurementTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('WORKSSUPPLIESCOMBINEDSERVICESOTHERNOT_APPLICABLENOT_SPECIFIEDCON_PUBLIC_WORKSCON_SERVICE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('WORKSSUPPLIESCOMBINEDSERVICESOTHERNOT_APPLICABLENOT_SPECIFIEDCON_PUBLIC_WORKSCON_SERVICE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:ProcurementTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterionPropertyGroup/cbc:PropertyGroupTypeCode" mode="M7" priority="1004">
    <svrl:fired-rule context="cac:TenderingCriterionPropertyGroup/cbc:PropertyGroupTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('ON*ONTRUEONFALSE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('ON*ONTRUEONFALSE','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:TenderingCriterionPropertyGroup/cbc:PropertyGroupTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:QualificationApplicationTypeCode" mode="M7" priority="1003">
    <svrl:fired-rule context="cbc:QualificationApplicationTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('REGULATEDSELFCONTAINED','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('REGULATEDSELFCONTAINED','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:QualificationApplicationTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:TenderingCriterionProperty/cbc:ValueDataTypeCode" mode="M7" priority="1002">
    <svrl:fired-rule context="cac:TenderingCriterionProperty/cbc:ValueDataTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('AMOUNTCODECODE_COUNTRYDATEDESCRIPTIONEVIDENCE_IDENTIFIERINDICATORPERCENTAGEPERIODQUANTITY_INTEGERQUANTITY_YEARQUANTITYNONEIDENTIFIERURLMAXIMUM_AMOUNTMINIMUM_AMOUNTMAXIMUM_VALUE_NUMERICMINIMUM_VALUE_NUMERICTRANSLATION_TYPE_CODECERTIFICATION_LEVEL_DESCRIPTIONCOPY_QUALITY_TYPE_CODETIMELOT_IDENTIFIERWEIGHT_INDICATORCODE_BOOLEANECONOMIC_OPERATOR_IDENTIFIER','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('AMOUNTCODECODE_COUNTRYDATEDESCRIPTIONEVIDENCE_IDENTIFIERINDICATORPERCENTAGEPERIODQUANTITY_INTEGERQUANTITY_YEARQUANTITYNONEIDENTIFIERURLMAXIMUM_AMOUNTMINIMUM_AMOUNTMAXIMUM_VALUE_NUMERICMINIMUM_VALUE_NUMERICTRANSLATION_TYPE_CODECERTIFICATION_LEVEL_DESCRIPTIONCOPY_QUALITY_TYPE_CODETIMELOT_IDENTIFIERWEIGHT_INDICATORCODE_BOOLEANECONOMIC_OPERATOR_IDENTIFIER','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:TenderingCriterionProperty/cbc:ValueDataTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cac:ProcurementProject/cbc:ProcurementSubTypeCode" mode="M7" priority="1001">
    <svrl:fired-rule context="cac:ProcurementProject/cbc:ProcurementSubTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('123456789101112131415161718192021222324252627','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('123456789101112131415161718192021222324252627','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cac:ProcurementProject/cbc:ProcurementSubTypeCode'.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:WeightingTypeCode" mode="M7" priority="1000">
    <svrl:fired-rule context="cbc:WeightingTypeCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( false() or ( (contains(translate('PERCENTAGENUMERIC','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) ) " />
      <xsl:otherwise>
        <svrl:failed-assert test="( false() or ( (contains(translate('PERCENTAGENUMERIC','ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),concat('',translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'')) ) ) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>Invalid value: '<xsl:text />
            <xsl:value-of select="." />
            <xsl:text />' for the element 'cbc:WeightingTypeCode'.</svrl:text>
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
