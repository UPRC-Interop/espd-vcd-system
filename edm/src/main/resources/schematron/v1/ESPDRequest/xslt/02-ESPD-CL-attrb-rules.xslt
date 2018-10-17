<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="2.0" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:ccv="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1" xmlns:ccv-cbc="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1" xmlns:cev="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1" xmlns:cev-cbc="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1" xmlns:espd-req="urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    <svrl:schematron-output schemaVersion="" title="Code list elements - mandatory attributes">
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
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:attribute name="id">cl-attrb-rules</xsl:attribute>
        <xsl:attribute name="name">cl-attrb-rules</xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M9" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>Code list elements - mandatory attributes</svrl:text>

<!--PATTERN cl-attrb-rules-->


	<!--RULE -->
<xsl:template match="cbc:TypeCode | ccv-cbc:JurisdictionLevelCode | cbc:ActivityTypeCode | cbc:LocaleCode | cbc:ContractTypeCode | cbc:CurrencyCode | cbc:ProcurementTypeCode | cbc:RoleCode" mode="M9" priority="1003">
    <svrl:fired-rule context="cbc:TypeCode | ccv-cbc:JurisdictionLevelCode | cbc:ActivityTypeCode | cbc:LocaleCode | cbc:ContractTypeCode | cbc:CurrencyCode | cbc:ProcurementTypeCode | cbc:RoleCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID and @listAgencyID and @listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID and @listAgencyID and @listVersionID">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>One or more of the mandatory attributes (listID, listAgengyID, listVersionID) are not provided </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="ext:ExtensionReasonCode" mode="M9" priority="1002">
    <svrl:fired-rule context="ext:ExtensionReasonCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID and @listAgencyID and @listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID and @listAgencyID and @listVersionID">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>One or more of the mandatory attributes (listID, listAgengyID, listVersionID) are not provided </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:PartyTypeCode | cbc:AddressTypeCode | cbc:AddressFormatCode | cbc:CountrySubentityCode" mode="M9" priority="1001">
    <svrl:fired-rule context="cbc:PartyTypeCode | cbc:AddressTypeCode | cbc:AddressFormatCode | cbc:CountrySubentityCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID and @listAgencyID and @listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID and @listAgencyID and @listVersionID">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>One or more of the mandatory attributes (listID, listAgengyID, listVersionID) are not provided </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE -->
<xsl:template match="cbc:IdentificationCode | cbc:CoordinateSystemCode | cbc:LatitudeDirectionCode | cbc:LongitudeDirectionCode | cbc:LocationTypeCode | cbc:DescriptionCode | cbc:TaxLevelCode | cbc:ExemptionReasonCode | cbc:TaxTypeCode | cbc:CompanyLegalFormCode | cbc:CompanyLiquidationStatusCode | cbc:CorporateRegistrationTypeCode | cbc:ChannelCode | cbc:GenderCode | cbc:AccountTypeCode | cbc:AccountFormatCode | cbc:DocumentTypeCode | cbc:LocaleCode | cbc:DocumentStatusCode | cbc:MimeCode | cbc:FormatCode | cbc:EncodingCode | cbc:CharacterSetCode | cbc:ValidationResultCode | cbc:ServiceTypeCode | cbc:AwardingMethodTypeCode | cbc:PriceEvaluationCode | cbc:FundingProgramCode | cbc:PaymentFrequencyCode | cbc:GuaranteeTypeCode | cbc:ReferenceEventCode | cbc:SourceCurrencyCode | cbc:TargetCurrencyCode | cbc:MathematicOperatorCode | cbc:LossRiskResponsibilityCode | cbc:AllowanceChargeReasonCode | cbc:AccountingCostCode | cbc:AccountingCost | cbc:TaxExemptionReasonCode | cbc:ShippingPriorityLevelCode | cbc:HandlingCode | cbc:TariffCode | cbc:TransportEventTypeCode | cbc:ConditionCode | cbc:StatusReasonCode | cbc:TransportServiceCode | cbc:TariffClassCode | cbc:FreightRateClassCode | cbc:TransportEquipmentTypeCode | cbc:ProviderTypeCode | cbc:OwnerTypeCode | cbc:SizeTypeCode | cbc:DispositionCode | cbc:FullnessIndicationCode | cbc:TrackingDeviceCode | cbc:SealStatusCode | cbc:SealIssuerTypeCode | cbc:DirectionCode | cbc:TransportMeansTypeCode | cbc:TradeServiceCode | cbc:TransportEmergencyCardCode | cbc:PackingCriteriaCode | cbc:HazardousRegulationCode | cbc:InhalationToxicityZoneCode | cbc:TransportAuthorizationCode | cbc:TransportHandlingUnitTypeCode | cbc:HandlingCode | cbc:LineStatusCode | cbc:PositionCode | cbc:NatureCode | cbc:CargoTypeCode | cbc:CommodityCode | cbc:ItemClassificationCode | cbc:ActionCode | cbc:UNDGCode | cbc:EmergencyProceduresCode | cbc:MedicalFirstAidGuideCode | cbc:HazardousCategoryCode | cbc:EmergencyProceduresCode | cbc:NameCode | cbc:ImportanceCode | cbc:ImportanceCode | cbc:CertificateTypeCode | cbc:PackageLevelCode | cbc:PackagingTypeCode | cbc:PreferenceCriterionCode | cbc:CustomsStatusCode | cbc:AccountingCostCode | cbc:PaymentPurposeCode | cbc:OrderTypeCode | cbc:PriceTypeCode | cbc:StatusCode | cbc:ShortageActionCode | cbc:RejectReasonCode | cbc:RejectActionCode | cbc:QuantityDiscrepancyCode | cbc:TimingComplaintCode | cbc:TransportModeCode |cbc:TransportMeansTypeCode |cbc:TransitDirectionCode | cbc:EnvironmentalEmissionTypeCode | cbc:CalculationMethodCode | cbc:FullnessIndicationCode | cbc:WeekDayCode | cbc:CompanyLegalFormCode | cbc:EvaluationCriterionTypeCode | cbc:ExpressionCode | cbc:EvidenceTypeCode | cbc:TendererRequirementTypeCode | cbc:SubcontractingConditionsCode | cbc:TenderEnvelopeTypeCode | cbc:ExecutionRequirementCode | cbc:WeightingAlgorithmCode | cbc:AwardingCriterionTypeCode | cbc:CalculationExpressionCode | cbc:ProcurementSubTypeCode | cbc:QualityControlCode" mode="M9" priority="1000">
    <svrl:fired-rule context="cbc:IdentificationCode | cbc:CoordinateSystemCode | cbc:LatitudeDirectionCode | cbc:LongitudeDirectionCode | cbc:LocationTypeCode | cbc:DescriptionCode | cbc:TaxLevelCode | cbc:ExemptionReasonCode | cbc:TaxTypeCode | cbc:CompanyLegalFormCode | cbc:CompanyLiquidationStatusCode | cbc:CorporateRegistrationTypeCode | cbc:ChannelCode | cbc:GenderCode | cbc:AccountTypeCode | cbc:AccountFormatCode | cbc:DocumentTypeCode | cbc:LocaleCode | cbc:DocumentStatusCode | cbc:MimeCode | cbc:FormatCode | cbc:EncodingCode | cbc:CharacterSetCode | cbc:ValidationResultCode | cbc:ServiceTypeCode | cbc:AwardingMethodTypeCode | cbc:PriceEvaluationCode | cbc:FundingProgramCode | cbc:PaymentFrequencyCode | cbc:GuaranteeTypeCode | cbc:ReferenceEventCode | cbc:SourceCurrencyCode | cbc:TargetCurrencyCode | cbc:MathematicOperatorCode | cbc:LossRiskResponsibilityCode | cbc:AllowanceChargeReasonCode | cbc:AccountingCostCode | cbc:AccountingCost | cbc:TaxExemptionReasonCode | cbc:ShippingPriorityLevelCode | cbc:HandlingCode | cbc:TariffCode | cbc:TransportEventTypeCode | cbc:ConditionCode | cbc:StatusReasonCode | cbc:TransportServiceCode | cbc:TariffClassCode | cbc:FreightRateClassCode | cbc:TransportEquipmentTypeCode | cbc:ProviderTypeCode | cbc:OwnerTypeCode | cbc:SizeTypeCode | cbc:DispositionCode | cbc:FullnessIndicationCode | cbc:TrackingDeviceCode | cbc:SealStatusCode | cbc:SealIssuerTypeCode | cbc:DirectionCode | cbc:TransportMeansTypeCode | cbc:TradeServiceCode | cbc:TransportEmergencyCardCode | cbc:PackingCriteriaCode | cbc:HazardousRegulationCode | cbc:InhalationToxicityZoneCode | cbc:TransportAuthorizationCode | cbc:TransportHandlingUnitTypeCode | cbc:HandlingCode | cbc:LineStatusCode | cbc:PositionCode | cbc:NatureCode | cbc:CargoTypeCode | cbc:CommodityCode | cbc:ItemClassificationCode | cbc:ActionCode | cbc:UNDGCode | cbc:EmergencyProceduresCode | cbc:MedicalFirstAidGuideCode | cbc:HazardousCategoryCode | cbc:EmergencyProceduresCode | cbc:NameCode | cbc:ImportanceCode | cbc:ImportanceCode | cbc:CertificateTypeCode | cbc:PackageLevelCode | cbc:PackagingTypeCode | cbc:PreferenceCriterionCode | cbc:CustomsStatusCode | cbc:AccountingCostCode | cbc:PaymentPurposeCode | cbc:OrderTypeCode | cbc:PriceTypeCode | cbc:StatusCode | cbc:ShortageActionCode | cbc:RejectReasonCode | cbc:RejectActionCode | cbc:QuantityDiscrepancyCode | cbc:TimingComplaintCode | cbc:TransportModeCode |cbc:TransportMeansTypeCode |cbc:TransitDirectionCode | cbc:EnvironmentalEmissionTypeCode | cbc:CalculationMethodCode | cbc:FullnessIndicationCode | cbc:WeekDayCode | cbc:CompanyLegalFormCode | cbc:EvaluationCriterionTypeCode | cbc:ExpressionCode | cbc:EvidenceTypeCode | cbc:TendererRequirementTypeCode | cbc:SubcontractingConditionsCode | cbc:TenderEnvelopeTypeCode | cbc:ExecutionRequirementCode | cbc:WeightingAlgorithmCode | cbc:AwardingCriterionTypeCode | cbc:CalculationExpressionCode | cbc:ProcurementSubTypeCode | cbc:QualityControlCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@listID and @listAgencyID and @listVersionID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@listID and @listAgencyID and @listVersionID">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>One or more of the mandatory attributes (listID, listAgengyID, listVersionID) are not provided </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M9" priority="-1" />
  <xsl:template match="@*|node()" mode="M9" priority="-2">
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
