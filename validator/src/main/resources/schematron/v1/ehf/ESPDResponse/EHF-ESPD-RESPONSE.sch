<schema xmlns="http://purl.oclc.org/dsdl/schematron"
        xmlns:u="utils"
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        schemaVersion="iso"
        queryBinding="xslt2">

    <title>EHF ESPD 1.0 Response</title>

    <ns prefix="cac"
       uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
    <ns prefix="cbc"
       uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
    <ns prefix="ccv-cbc"
       uri="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonBasicComponents-1"/>
    <ns prefix="cev-cac"
       uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1"/>
    <ns prefix="cev-cbc"
       uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonBasicComponents-1"/>
    <ns prefix="cev"
       uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1"/>
    <ns prefix="espd"
       uri="urn:grow:names:specification:ubl:schema:xsd:ESPDResponse-1"/>
    <ns prefix="espd-req"
       uri="urn:grow:names:specification:ubl:schema:xsd:ESPDRequest-1"/>
    <ns prefix="ext"
       uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"/>
    <ns prefix="espd-cbc"
       uri="urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonBasicComponents-1"/>
    <ns prefix="ccv"
       uri="urn:isa:names:specification:ubl:schema:xsd:CCV-CommonAggregateComponents-1"/>
    <ns prefix="espd-cac"
       uri="urn:grow:names:specification:ubl:schema:xsd:ESPD-CommonAggregateComponents-1"/>
    <ns prefix="cev-cac"
       uri="urn:isa:names:specification:ubl:schema:xsd:CEV-CommonAggregateComponents-1"/>
    <ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
    <ns prefix="u" uri="utils"/>

    

    <function xmlns="http://www.w3.org/1999/XSL/Transform"
             name="u:mod11"
             as="xs:boolean">
        <param name="val"/>
        <variable name="length" select="string-length($val) - 1"/>
        <variable name="digits"
                select="reverse(for $i in string-to-codepoints(substring($val, 0, $length + 1)) return $i - 48)"/>
        <variable name="weightedSum"
                select="sum(for $i in (0 to $length - 1) return $digits[$i + 1] * (($i mod 6) + 2))"/>
        <value-of select="number($val) &gt; 0 and (11 - ($weightedSum mod 11)) mod 11 = number(substring($val, $length + 1, 1))"/>
    </function>

    <pattern>

      <rule context="ccv:Response">
        
    </rule>
      <rule context="cbc:* | ccv-cbc:* | cev-cbc:* | espd-cbc:*">
        <assert id="EHF-ESPD-R001" test=". != ''" flag="fatal">Document MUST not contain empty elements.</assert>
      </rule>
      <rule context="cac:* | cev:* | ccv:* | espd-cac:*">
        <assert id="EHF-ESPD-R002" test="count(*) != 0" flag="fatal">Document MUST not contain empty elements.</assert>
      </rule>

   </pattern>
    <pattern>

      <let name="special_rg"
           value="tokenize('c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16 096686e1-82ca-4de0-8710-d74d90da0f0c 96defecc-7d32-4957-82e9-aad5f3c5b736 96f00020-0a25-402e-b850-2378e83b5695', '\s')"/>

    

      <rule context="ccv:RequirementGroup[/espd:ESPDResponse][@pi='GROUP_FULFILLED.ON_TRUE'][normalize-space(../ccv:Requirement[1]/ccv:Response/ccv-cbc:Indicator) = 'false']//ccv:RequirementGroup[@pi]//ccv:Requirement">
        <assert id="EHF-ESPD-R112" test="not(ccv:Response)" flag="fatal">Response MUST NOT be provided when response is not expected.</assert>
      </rule>

      <rule context="ccv:RequirementGroup[/espd:ESPDResponse][@pi='GROUP_FULFILLED.ON_TRUE'][normalize-space(../ccv:Requirement[1]/ccv:Response/ccv-cbc:Indicator) = 'false']//ccv:Requirement">
        <assert id="EHF-ESPD-R110" test="not(ccv:Response)" flag="fatal">Response MUST NOT be provided when response is not expected.</assert>
      </rule>

      <rule context="ccv:RequirementGroup[/espd:ESPDResponse][@pi='GROUP_FULFILLED.ON_FALSE'][normalize-space(../ccv:Requirement[1]/ccv:Response/ccv-cbc:Indicator) = 'true']//ccv:Requirement">
        <assert id="EHF-ESPD-R111" test="not(ccv:Response)" flag="fatal">Response MUST NOT be provided when response is not expected.</assert>
      </rule>

      <rule context="ccv:RequirementGroup[/espd:ESPDResponse][some $code in $special_rg satisfies normalize-space(cbc:ID) = $code]/ccv:Requirement">
        <assert id="EHF-ESPD-R113"
                 test="ccv:Response or count(../ccv:Requirement/ccv:Response) = 0"
                 flag="fatal">Response MUST be provided for all requirements in this group.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse]">
        <assert id="EHF-ESPD-R100" test="ccv:Response" flag="fatal">Response MUST be provided.</assert>
        <assert id="EHF-ESPD-R101"
                 test="not(ccv:Response) or count(ccv:Response/*) - count(ccv:Response/cbc:ID) = 1"
                 flag="fatal">Response MUST contain only the specified response type.</assert>
      </rule>
      <rule context="ccv:Requirement[/espd-req:ESPDRequest]">
        <assert id="EHF-ESPD-R102" test="not(ccv:Response)" flag="fatal">Response MUST NOT be provided in ESPD Request.</assert>
      </rule>

    

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'AMOUNT']/ccv:Response">
        <assert id="EHF-ESPD-R120" test="cbc:Amount" flag="fatal">Amount element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'CODE']/ccv:Response">
        <assert id="EHF-ESPD-R121" test="ccv-cbc:Code" flag="fatal">Code element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'CODE_COUNTRY']/ccv:Response">
        <assert id="EHF-ESPD-R122" test="ccv-cbc:Code" flag="fatal">Code element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'DATE']/ccv:Response">
        <assert id="EHF-ESPD-R123" test="cbc:Date" flag="fatal">Date element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'DESCRIPTION']/ccv:Response">
        <assert id="EHF-ESPD-R124" test="cbc:Description" flag="fatal">Description element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'EVIDENCE_URL']/ccv:Response">
        <assert id="EHF-ESPD-R125" test="cev-cac:Evidence" flag="fatal">Evidence element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'INDICATOR']/ccv:Response">
        <assert id="EHF-ESPD-R126" test="ccv-cbc:Indicator" flag="fatal">Indicator element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'PERCENTAGE']/ccv:Response">
        <assert id="EHF-ESPD-R127" test="cbc:Percent" flag="fatal">Percent element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'PERIOD']/ccv:Response">
        <assert id="EHF-ESPD-R128" test="cac:Period" flag="fatal">Period element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'QUANTITY_INTEGER']/ccv:Response">
        <assert id="EHF-ESPD-R129" test="cbc:Quantity[not(@unitCode)]" flag="fatal">Quantity element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'QUANTITY_YEAR']/ccv:Response">
        <assert id="EHF-ESPD-R130"
                 test="cbc:Quantity[@unitCode = 'YEAR']"
                 flag="fatal">Quantity element MUST be provided as response when specified.</assert>
      </rule>

      <rule context="ccv:Requirement[/espd:ESPDResponse][@responseDataType = 'QUANTITY']/ccv:Response">
        <assert id="EHF-ESPD-R131"
                 test="cbc:Quantity[@unitCode and @unitCode != 'YEAR']"
                 flag="fatal">Quantity element MUST be provided as response when specified.</assert>
      </rule>

    

      <rule context="cac:AdditionalDocumentReference[/espd:ESPDResponse][normalize-space(cbc:DocumentTypeCode) = 'TED_CN']">
        <assert id="EHF-ESPD-R211"
                 test="not(cbc:IssueDate) and not(cbc:IssueTime)"
                 flag="fatal">TED reference MUST NOT contain issue date and issue time.</assert>
        <assert id="EHF-ESPD-R212" test="cbc:ID" flag="fatal">TED reference MUST contain an identifier.</assert>
        <assert id="EHF-ESPD-R213"
                 test="matches(normalize-space(cbc:ID/text()), '^[0-9]{4}/S [0-9]{3}\-[0-9]{6}$')"
                 flag="fatal">TED reference MUST match '[][][][]/S [][][]-[][][][][][]' (e.g. 2015/S 252-461137).</assert>
        <assert id="EHF-ESPD-R214"
                 test="normalize-space(cbc:ID/text()) != '0000/S 000-000000'"
                 flag="fatal">TED reference MUST not be a temporary value.</assert>
      </rule>

      <rule context="cac:AdditionalDocumentReference[/espd:ESPDResponse][normalize-space(cbc:DocumentTypeCode) = 'ESPD_REQUEST']">
        <assert id="EHF-ESPD-R221" test="not(cac:Attachment/cbc:URI)" flag="fatal">NGOJ reference MUST NOT contain issue date, issue time and attachment.</assert>
        <assert id="EHF-ESPD-R222" test="cbc:IssueDate" flag="fatal">ESPD Request reference MUST contain an issue date.</assert>
      </rule>

      <rule context="cac:AdditionalDocumentReference[normalize-space(cbc:DocumentTypeCode) = 'NGOJ']">
        <assert id="EHF-ESPD-R231"
                 test="not(cac:Attachment) and not(cbc:IssueDate) and not(cbc:IssueTime)"
                 flag="fatal">Doffin reference MUST NOT contain issue date, issue time and attachment.</assert>
        <assert id="EHF-ESPD-R233"
                 test="matches(normalize-space(cbc:ID/text()), '^[0-9]{4}\-[0-9]{6}$')"
                 flag="fatal">Doffin reference MUST match 'YYYY-[][][][][][]' (e.g. 2017-461137).</assert>
      </rule>

      <rule context="cac:AdditionalDocumentReference">
        
        <assert id="EHF-ESPD-R200" test="not(/espd-req:ESPDRequest)" flag="fatal">ESPD Request MUST contain only document reference 'NGOJ'.</assert>
        <assert id="EHF-ESPD-R201" test="not(/espd:ESPDResponse)" flag="fatal">ESPD Responst MUST contain only document references 'TED_CN', 'ESPD_REQUEST' and 'NGOJ'.</assert>
      </rule>

    

      <rule context="cbc:IssueDate | cbc:BirthDate | cbc:Date">
        <assert id="EHF-ESPD-R020"
                 test="(normalize-space(text()) castable as xs:date) and string-length(normalize-space(text())) = 10"
                 flag="fatal">A date must be formatted YYYY-MM-DD.</assert>
      </rule>

      <rule context="cbc:IssueTime">
        <assert id="EHF-ESPD-R021"
                 test="(normalize-space(text()) castable as xs:time) and string-length(normalize-space(text())) = 8"
                 flag="fatal">A time must be formatted hh:mm:ss.</assert>
      </rule>

      <rule context="espd-cac:EconomicOperatorParty/cac:Party/cac:PartyIdentification/cbc:ID | cbc:EndpointID">
        <assert id="EHF-ESPD-R022"
                 test="matches(normalize-space(text()), '^[0-9]{9}$') and u:mod11(normalize-space(text()))"
                 flag="fatal">MUST be a valid Norwegian organization number. Only numerical value allowed</assert>
      </rule>

   </pattern>
    <pattern xmlns:ns2="http://www.schematron-quickfix.com/validator/process">
      <let name="clCurrencyCode"
           value="tokenize('AED AFN ALL AMD ANG AOA ARS AUD AWG AZN BAM BBD BDT BGN BHD BIF BMD BND BOB BOV BRL BSD BTN BWP BYR BZD CAD CDF CHE CHF CHW CLF CLP CNY COP COU CRC CUP CVE CYP CZK DJF DKK DOP DZD EEK EGP ERN ETB EUR FJD FKP GBP GEL GHS GIP GMD GNF GTQ GYD HKD HNL HRK HTG HUF IDR ILS INR IQD IRR ISK JMD JOD JPY KES KGS KHR KMF KPW KRW KWD KYD KZT LAK LBP LKR LRD LSL LTL LVL LYD MAD MDL MGA MKD MMK MNT MOP MRO MTL MUR MVR MWK MXN MXV MYR MZN NAD NGN NIO NOK NPR NZD OMR PAB PEN PGK PHP PKR PLN PYG QAR RON RSD RUB RWF SAR SBD SCR SDG SSP SEK SGD SHP SKK SLL SOS SRD STD SYP SZL THB TJS TMM TND TOP TRY TTD TWD TZS UAH UGX USD USN USS UYU UZS VEB VND VUV WST XAF XAG XAU XBA XBB XBC XBD XCD XDR XFO XFU XOF XPD XPF XPT XTS XXX YER ZAR ZMK ZWD', '\s')"/>
      <let name="clUNECERec20"
           value="tokenize('10 11 13 14 15 20 21 22 23 24 25 27 28 33 34 35 37 38 40 41 56 57 58 59 60 61 64 66 74 76 77 78 80 81 84 85 87 89 91 1I 2A 2B 2C 2G 2H 2I 2J 2K 2L 2M 2N 2P 2Q 2R 2U 2X 2Y 2Z 3B 3C 4C 4G 4H 4K 4L 4M 4N 4O 4P 4Q 4R 4T 4U 4W 4X 5A 5B 5E 5J A1 A10 A11 A12 A13 A14 A15 A16 A17 A18 A19 A2 A20 A21 A22 A23 A24 A25 A26 A27 A28 A29 A3 A30 A31 A32 A33 A34 A35 A36 A37 A38 A39 A4 A40 A41 A42 A43 A44 A45 A47 A48 A49 A5 A50 A51 A52 A53 A54 A55 A56 A57 A58 A59 A6 A60 A61 A62 A63 A64 A65 A66 A67 A68 A69 A7 A70 A71 A73 A74 A75 A76 A77 A78 A79 A8 A80 A81 A82 A83 A84 A85 A86 A87 A88 A89 A9 A90 A91 A93 A94 A95 A96 A97 A98 A99 AA AB ACR ACT AD AE AH AI AK AL AMH AMP ANN APZ AQ ARE AS ASM ASU ATM ATT AWG AY AZ B1 B10 B11 B12 B13 B14 B15 B16 B17 B18 B19 B20 B21 B22 B23 B24 B25 B26 B27 B28 B29 B3 B30 B31 B32 B33 B34 B35 B36 B37 B38 B39 B4 B40 B41 B42 B43 B44 B45 B46 B47 B48 B49 B50 B51 B52 B53 B54 B55 B56 B57 B58 B59 B60 B61 B62 B63 B64 B65 B66 B67 B68 B69 B7 B70 B71 B72 B73 B74 B75 B76 B77 B78 B79 B8 B80 B81 B82 B83 B84 B85 B86 B87 B88 B89 B90 B91 B92 B93 B94 B95 B96 B97 B98 B99 BAR BB BFT BHP BIL BLD BLL BP BPM BQL BTU BUA BUI C0 C10 C11 C12 C13 C14 C15 C16 C17 C18 C19 C20 C21 C22 C23 C24 C25 C26 C27 C28 C29 C3 C30 C31 C32 C33 C34 C35 C36 C37 C38 C39 C40 C41 C42 C43 C44 C45 C46 C47 C48 C49 C50 C51 C52 C53 C54 C55 C56 C57 C58 C59 C60 C61 C62 C63 C64 C65 C66 C67 C68 C69 C7 C70 C71 C72 C73 C74 C75 C76 C78 C79 C8 C80 C81 C82 C83 C84 C85 C86 C87 C88 C89 C9 C90 C91 C92 C93 C94 C95 C96 C97 C99 CCT CDL CEL CEN CG CGM CKG CLF CLT CMK CMQ CMT CNP CNT COU CTG CTM CTN CUR CWA CWI D03 D04 D1 D10 D11 D12 D13 D15 D16 D17 D18 D19 D2 D20 D21 D22 D23 D24 D25 D26 D27 D29 D30 D31 D32 D33 D34 D35 D36 D37 D38 D39 D41 D42 D43 D44 D45 D46 D47 D48 D49 D5 D50 D51 D52 D53 D54 D55 D56 D57 D58 D59 D6 D60 D61 D62 D63 D65 D68 D69 D70 D71 D72 D73 D74 D75 D76 D77 D78 D80 D81 D82 D83 D85 D86 D87 D88 D89 D9 D91 D93 D94 D95 DAA DAD DAY DB DD DEC DG DJ DLT DMA DMK DMO DMQ DMT DN DPC DPR DPT DRA DRI DRL DT DTN DU DWT DX DZN DZP E01 E07 E08 E09 E10 E11 E12 E14 E15 E16 E17 E18 E19 E20 E21 E22 E23 E25 E27 E28 E30 E31 E32 E33 E34 E35 E36 E37 E38 E39 E4 E40 E41 E42 E43 E44 E45 E46 E47 E48 E49 E50 E51 E52 E53 E54 E55 E56 E57 E58 E59 E60 E61 E62 E63 E64 E65 E66 E67 E68 E69 E70 E71 E72 E73 E74 E75 E76 E77 E78 E79 E80 E81 E82 E83 E84 E85 E86 E87 E88 E89 E90 E91 E92 E93 E94 E95 E96 E97 E98 E99 EA EB EQ F01 F02 F03 F04 F05 F06 F07 F08 F10 F11 F12 F13 F14 F15 F16 F17 F18 F19 F20 F21 F22 F23 F24 F25 F26 F27 F28 F29 F30 F31 F32 F33 F34 F35 F36 F37 F38 F39 F40 F41 F42 F43 F44 F45 F46 F47 F48 F49 F50 F51 F52 F53 F54 F55 F56 F57 F58 F59 F60 F61 F62 F63 F64 F65 F66 F67 F68 F69 F70 F71 F72 F73 F74 F75 F76 F77 F78 F79 F80 F81 F82 F83 F84 F85 F86 F87 F88 F89 F90 F91 F92 F93 F94 F95 F96 F97 F98 F99 FAH FAR FBM FC FF FH FIT FL FOT FP FR FS FTK FTQ G01 G04 G05 G06 G08 G09 G10 G11 G12 G13 G14 G15 G16 G17 G18 G19 G2 G20 G21 G23 G24 G25 G26 G27 G28 G29 G3 G30 G31 G32 G33 G34 G35 G36 G37 G38 G39 G40 G41 G42 G43 G44 G45 G46 G47 G48 G49 G50 G51 G52 G53 G54 G55 G56 G57 G58 G59 G60 G61 G62 G63 G64 G65 G66 G67 G68 G69 G70 G71 G72 G73 G74 G75 G76 G77 G78 G79 G80 G81 G82 G83 G84 G85 G86 G87 G88 G89 G90 G91 G92 G93 G94 G95 G96 G97 G98 G99 GB GBQ GDW GE GF GFI GGR GIA GIC GII GIP GJ GL GLD GLI GLL GM GO GP GQ GRM GRN GRO GRT GT GV GWH H03 H04 H05 H06 H07 H08 H09 H10 H11 H12 H13 H14 H15 H16 H18 H19 H20 H21 H22 H23 H24 H25 H26 H27 H28 H29 H30 H31 H32 H33 H34 H35 H36 H37 H38 H39 H40 H41 H42 H43 H44 H45 H46 H47 H48 H49 H50 H51 H52 H53 H54 H55 H56 H57 H58 H59 H60 H61 H62 H63 H64 H65 H66 H67 H68 H69 H70 H71 H72 H73 H74 H75 H76 H77 H78 H79 H80 H81 H82 H83 H84 H85 H87 H88 H89 H90 H91 H92 H93 H94 H95 H96 H98 H99 HA HAR HBA HBX HC HDW HEA HGM HH HIU HJ HKM HLT HM HMQ HMT HN HP HPA HTZ HUR IA IE INH INK INQ ISD IU IV J10 J12 J13 J14 J15 J16 J17 J18 J19 J2 J20 J21 J22 J23 J24 J25 J26 J27 J28 J29 J30 J31 J32 J33 J34 J35 J36 J38 J39 J40 J41 J42 J43 J44 J45 J46 J47 J48 J49 J50 J51 J52 J53 J54 J55 J56 J57 J58 J59 J60 J61 J62 J63 J64 J65 J66 J67 J68 J69 J70 J71 J72 J73 J74 J75 J76 J78 J79 J81 J82 J83 J84 J85 J87 J89 J90 J91 J92 J93 J94 J95 J96 J97 J98 J99 JE JK JM JNT JOU JPS JWL K1 K10 K11 K12 K13 K14 K15 K16 K17 K18 K19 K2 K20 K21 K22 K23 K24 K25 K26 K27 K28 K3 K30 K31 K32 K33 K34 K35 K36 K37 K38 K39 K40 K41 K42 K43 K45 K46 K47 K48 K49 K5 K50 K51 K52 K53 K54 K55 K58 K59 K6 K60 K61 K62 K63 K64 K65 K66 K67 K68 K69 K70 K71 K73 K74 K75 K76 K77 K78 K79 K80 K81 K82 K83 K84 K85 K86 K87 K88 K89 K90 K91 K92 K93 K94 K95 K96 K97 K98 K99 KA KAT KB KBA KCC KDW KEL KGM KGS KHY KHZ KI KIC KIP KJ KJO KL KLK KLX KMA KMH KMK KMQ KMT KNI KNM KNS KNT KO KPA KPH KPO KPP KR KSD KSH KT KTN KUR KVA KVR KVT KW KWH KWO KWT KX L10 L11 L12 L13 L14 L15 L16 L17 L18 L19 L2 L20 L21 L23 L24 L25 L26 L27 L28 L29 L30 L31 L32 L33 L34 L35 L36 L37 L38 L39 L40 L41 L42 L43 L44 L45 L46 L47 L48 L49 L50 L51 L52 L53 L54 L55 L56 L57 L58 L59 L60 L63 L64 L65 L66 L67 L68 L69 L70 L71 L72 L73 L74 L75 L76 L77 L78 L79 L80 L81 L82 L83 L84 L85 L86 L87 L88 L89 L90 L91 L92 L93 L94 L95 L96 L98 L99 LA LAC LBR LBT LD LEF LF LH LK LM LN LO LP LPA LR LS LTN LTR LUB LUM LUX LY M1 M10 M11 M12 M13 M14 M15 M16 M17 M18 M19 M20 M21 M22 M23 M24 M25 M26 M27 M29 M30 M31 M32 M33 M34 M35 M36 M37 M38 M39 M4 M40 M41 M42 M43 M44 M45 M46 M47 M48 M49 M5 M50 M51 M52 M53 M55 M56 M57 M58 M59 M60 M61 M62 M63 M64 M65 M66 M67 M68 M69 M7 M70 M71 M72 M73 M74 M75 M76 M77 M78 M79 M80 M81 M82', '\s')"/>
      <let name="clCriterionJurisdictionLevel"
           value="tokenize('EU_REGULATION EU_DIRECTIVE EU_DECISION NATIONAL_LEGISLATION SUBNATIONAL_LEGISLATION', '\s')"/>
      <let name="clCriteriaTypeCode"
           value="tokenize('CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION CRITERION.EXCLUSION.CONVICTIONS.CORRUPTION CRITERION.EXCLUSION.CONVICTIONS.FRAUD CRITERION.EXCLUSION.CONVICTIONS.TERRORIST_OFFENCES CRITERION.EXCLUSION.CONVICTIONS.MONEY_LAUNDERING CRITERION.EXCLUSION.CONVICTIONS.CHILD_LABOUR-HUMAN_TRAFFICKING CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_TAXES CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_SOCIAL_SECURITY CRITERION.EXCLUSION.SOCIAL.ENVIRONMENTAL_LAW CRITERION.EXCLUSION.SOCIAL.SOCIAL_LAW CRITERION.EXCLUSION.SOCIAL.LABOUR_LAW CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY CRITERION.EXCLUSION.BUSINESS.INSOLVENCY CRITERION.EXCLUSION.BUSINESS.CREDITORS_ARRANGEMENT CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY_ANALOGOUS CRITERION.EXCLUSION.BUSINESS.LIQUIDATOR_ADMINISTERED CRITERION.EXCLUSION.BUSINESS.ACTIVITIES_SUSPENDED CRITERION.EXCLUSION.MISCONDUCT.MC_PROFESSIONAL CRITERION.EXCLUSION.MISCONDUCT.MARKET_DISTORTION CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PARTICIPATION CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PREPARATION CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.EARLY_TERMINATION CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.MISINTERPRETATION CRITERION.EXCLUSION.NATIONAL.OTHER CRITERION.SELECTION.ALL_SATISFIED CRITERION.SELECTION.SUITABILITY.PROFESSIONAL_REGISTER_ENROLMENT CRITERION.SELECTION.SUITABILITY.TRADE_REGISTER_ENROLMENT CRITERION.SELECTION.SUITABILITY.AUTHORISATION CRITERION.SELECTION.SUITABILITY.MEMBERSHIP CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.GENERAL_YEARLY CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.AVERAGE_YEARLY CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_YEARLY CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_AVERAGE CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SET_UP CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.FINANCIAL_RATIO CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.RISK_INDEMNITY_INSURANCE CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.OTHER_REQUIREMENTS CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.WORKS_PERFORMANCE CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SUPPLIES_DELIVERY_PERFORMANCE CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SERVICES_DELIVERY_PERFORMANCE CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_QUALITY_CONTROL CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_CARRYING_WORKS CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_QUALITY_ENSURING CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_STUDY_RESEARCH CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.SUPPLY_CHAIN_MANAGEMENT CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.CHECKS.ALLOWANCE_OF_CHECKS CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.PROFESSIONAL_QUALIFICATIONS CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.ENVIRONMENTAL_MANAGEMENT_MEASURES CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.MANAGERIAL_STAFF CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.AVERAGE_ANNUAL_MANPOWER CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.EQUIPMENT CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.SUBCONTRACTING_PROPORTION CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.NO_AUTHENTICATED_ARTEFACTS CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.AUTHENTICATED_ARTEFACTS &#xA;            CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE&#xA;         &#xA;            CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE&#xA;         &#xA;            CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE&#xA;         CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE', '\s')"/>
      <let name="clDocRefContentType"
           value="tokenize('TED_CN ESPD_REQUEST NGOJ', '\s')"/>
      <let name="clCountryCodeIdentifier"
           value="tokenize('AD AE AF AG AI AL AM AO AQ AR AS AT AU AW AX AZ BA BB BD BE BF BG BH BI BJ BL BM BN BO BQ BR BS BT BV BW BY BZ CA CC CD CF CG CH CI CK CL CM CN CO CR CU CV CW CX CY CZ DE DJ DK DM DO DZ EC EE EG EH ER ES ET FI FJ FK FM FO FR GA GB GD GE GF GG GH GI GL GM GN GP GQ GR GS GT GU GW GY HK HM HN HR HT HU ID IE IL IM IN IO IQ IR IS IT JE JM JO JP KE KG KH KI KM KN KP KR KW KY KZ LA LB LC LI LK LR LS LT LU LV LY MA MC MD ME MF MG MH MK ML MM MN MO MP MQ MR MS MT MU MV MW MX MY MZ NA NC NE NF NG NI NL NO NP NR NU NZ OM PA PE PF PG PH PK PL PM PN PR PS PT PW PY QA RE RO RS RU RW SA SB SC SD SE SG SH SI SJ SK SL SM SN SO SR SS ST SV SX SY SZ TC TD TF TG TH TJ TK TL TM TN TO TR TT TV TW TZ UA UG UM US UY UZ VA VC VE VG VI VN VU WF WS XK YE YT ZA ZM ZW', '\s')"/>
      <let name="clResponseDataType"
           value="tokenize('AMOUNT CODE CODE_COUNTRY DATE DESCRIPTION EVIDENCE_URL INDICATOR PERCENTAGE PERIOD QUANTITY_INTEGER QUANTITY_YEAR QUANTITY', '\s')"/>
      <let name="clProfileID" value="tokenize('', '\s')"/>
      <let name="clPeriodMeasureTypeCodes" value="tokenize('MONTH YEAR', '\s')"/>
      <let name="clLanguageCodeEU"
           value="tokenize('PL PT EL EN ES ET FI RO GA FR SK SL SV HU IT LT LV MT BG NL DA CS DE HR', '\s')"/>
      <rule context="/espd:ESPDResponse">
         <assert test="cbc:CustomizationID" flag="fatal" id="EHF-ESPD-RES-B00101">Element 'cbc:CustomizationID' MUST be provided.</assert>
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B00102">Element 'cbc:ID' MUST be provided.</assert>
         <assert test="cbc:IssueDate" flag="fatal" id="EHF-ESPD-RES-B00103">Element 'cbc:IssueDate' MUST be provided.</assert>
         <assert test="cbc:ContractFolderID" flag="fatal" id="EHF-ESPD-RES-B00104">Element 'cbc:ContractFolderID' MUST be provided.</assert>
         <assert test="cac:ContractingParty" flag="fatal" id="EHF-ESPD-RES-B00105">Element 'cac:ContractingParty' MUST be provided.</assert>
         <assert test="espd-cac:EconomicOperatorParty"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00106">Element 'espd-cac:EconomicOperatorParty' MUST be provided.</assert>
         <assert test="cac:ProcurementProjectLot"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00107">Element 'cac:ProcurementProjectLot' MUST be provided.</assert>
         <assert test="ccv:Criterion" flag="fatal" id="EHF-ESPD-RES-B00108">Element 'ccv:Criterion' MUST be provided.</assert>
         <assert test="not(@*:schemaLocation)" flag="fatal" id="EHF-ESPD-RES-B00109">Document MUST not contain schema location.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:UBLVersionID">
         <assert test="normalize-space(text()) = '2.1'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00201">Element 'cbc:UBLVersionID' MUST contain value '2.1'.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'OASIS-UBL-TC'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00202">Attribute 'schemeAgencyID' MUST contain value 'OASIS-UBL-TC'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B00203">Attribute 'schemeAgencyID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:CustomizationID">
         <assert test="normalize-space(text()) = 'urn:www.cenbii.eu:transaction:biitrns092:ver3.0:extended:urn:fdc:difi.no:2017:ehf:spec:1.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00401">Element 'cbc:CustomizationID' MUST contain value 'urn:www.cenbii.eu:transaction:biitrns092:ver3.0:extended:urn:fdc:difi.no:2017:ehf:spec:1.0'.</assert>
         <assert test="not(@schemeName) or @schemeName = 'CustomizationID'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00402">Attribute 'schemeName' MUST contain value 'CustomizationID'</assert>
         <assert test="@schemeName" flag="fatal" id="EHF-ESPD-RES-B00403">Attribute 'schemeName' MUST be present.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'BII'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00404">Attribute 'schemeAgencyID' MUST contain value 'BII'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B00405">Attribute 'schemeAgencyID' MUST be present.</assert>
         <assert test="not(@schemeVersionID) or @schemeVersionID = '3.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00406">Attribute 'schemeVersionID' MUST contain value '3.0'</assert>
         <assert test="@schemeVersionID" flag="fatal" id="EHF-ESPD-RES-B00407">Attribute 'schemeVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:ProfileID">
         <assert test="(some $code in $clProfileID satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00801">Value MUST be part of code list 'Profile identifiers'.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:ID">
         <assert test="not(@schemeID) or @schemeID = 'ISO/IEC 9834-8:2008 - 4UUID'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00901">Attribute 'schemeID' MUST contain value 'ISO/IEC 9834-8:2008 - 4UUID'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B00902">Attribute 'schemeID' MUST be present.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00903">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B00904">Attribute 'schemeAgencyID' MUST be present.</assert>
         <assert test="not(@schemeAgencyName) or @schemeAgencyName = 'DG GROW (European Commission)'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00905">Attribute 'schemeAgencyName' MUST contain value 'DG GROW (European Commission)'</assert>
         <assert test="@schemeAgencyName" flag="fatal" id="EHF-ESPD-RES-B00906">Attribute 'schemeAgencyName' MUST be present.</assert>
         <assert test="not(@schemeVersionID) or @schemeVersionID = '1.1'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B00907">Attribute 'schemeVersionID' MUST contain value '1.1'</assert>
         <assert test="@schemeVersionID" flag="fatal" id="EHF-ESPD-RES-B00908">Attribute 'schemeVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:CopyIndicator"/>
      <rule context="/espd:ESPDResponse/cbc:VersionID">
         <assert test="normalize-space(text()) = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B01501">Element 'cbc:VersionID' MUST contain value '1.0.2'.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B01502">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B01503">Attribute 'schemeAgencyID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cbc:IssueDate"/>
      <rule context="/espd:ESPDResponse/cbc:IssueTime"/>
      <rule context="/espd:ESPDResponse/cbc:ContractFolderID">
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'TeD'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B01901">Attribute 'schemeAgencyID' MUST contain value 'TeD'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B01902">Attribute 'schemeAgencyID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cbc:EconomicOperatorGroupName"/>
      <rule context="/espd:ESPDResponse/cac:ContractingParty">
         <assert test="cac:Party" flag="fatal" id="EHF-ESPD-RES-B02201">Element 'cac:Party' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party">
         <assert test="cac:PartyIdentification"
                 flag="fatal"
                 id="EHF-ESPD-RES-B02301">Element 'cac:PartyIdentification' MUST be provided.</assert>
         <assert test="cac:PartyName" flag="fatal" id="EHF-ESPD-RES-B02302">Element 'cac:PartyName' MUST be provided.</assert>
         <assert test="cac:PostalAddress" flag="fatal" id="EHF-ESPD-RES-B02303">Element 'cac:PostalAddress' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cbc:EndpointID">
         <assert test="not(@schemeID) or @schemeID = 'NO:ORGNR'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B02401">Attribute 'schemeID' MUST contain value 'NO:ORGNR'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B02402">Attribute 'schemeID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PartyIdentification">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B02601">Element 'cbc:ID' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PartyIdentification/cbc:ID">
         <assert test="not(@schemeID) or @schemeID = 'NO:ORGNR'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B02701">Attribute 'schemeID' MUST contain value 'NO:ORGNR'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B02702">Attribute 'schemeID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PartyName">
         <assert test="cbc:Name" flag="fatal" id="EHF-ESPD-RES-B02901">Element 'cbc:Name' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PartyName/cbc:Name"/>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PostalAddress">
         <assert test="cac:Country" flag="fatal" id="EHF-ESPD-RES-B03101">Element 'cac:Country' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PostalAddress/cac:Country">
         <assert test="cbc:IdentificationCode" flag="fatal" id="EHF-ESPD-RES-B03201">Element 'cbc:IdentificationCode' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PostalAddress/cac:Country/cbc:IdentificationCode">
         <assert test="normalize-space(text()) = 'NO'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B03301">Element 'cbc:IdentificationCode' MUST contain value 'NO'.</assert>
         <assert test="not(@listID) or @listID = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B03302">Attribute 'listID' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B03303">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B03304">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B03305">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listName) or @listName = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B03306">Attribute 'listName' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listName" flag="fatal" id="EHF-ESPD-RES-B03307">Attribute 'listName' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B03308">Attribute 'listVersionID' MUST contain value '1.0.2'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B03309">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PostalAddress/cac:Country/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B03202">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/cac:PostalAddress/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B03102">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/cac:Party/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B02304">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ContractingParty/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B02202">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty">
         <assert test="cac:Party" flag="fatal" id="EHF-ESPD-RES-B03801">Element 'cac:Party' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cbc:SMEIndicator"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/espd-cbc:NaturalPersonRoleDescription"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/espd-cbc:NaturalPersonRegistrationCountryCode">
         <assert test="(some $code in $clCountryCodeIdentifier satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B04201">Value MUST be part of code list 'ISO 3166 Alpha-2 Country codes'.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person">
         <assert test="cbc:FirstName" flag="fatal" id="EHF-ESPD-RES-B04501">Element 'cbc:FirstName' MUST be provided.</assert>
         <assert test="cbc:FamilyName" flag="fatal" id="EHF-ESPD-RES-B04502">Element 'cbc:FamilyName' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cbc:FirstName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cbc:FamilyName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cbc:BirthDate"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cbc:BirthplaceName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:Contact"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:Contact/cbc:Telephone"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:Contact/cbc:ElectronicMail"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:Contact/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B05001">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress">
         <assert test="cac:Country" flag="fatal" id="EHF-ESPD-RES-B05301">Element 'cac:Country' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:Postbox"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:StreetName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:CityName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cbc:PostalZone"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cac:Country">
         <assert test="cbc:IdentificationCode" flag="fatal" id="EHF-ESPD-RES-B05801">Element 'cbc:IdentificationCode' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cac:Country/cbc:IdentificationCode">
         <assert test="(some $code in $clCountryCodeIdentifier satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B05901">Value MUST be part of code list 'ISO 3166 Alpha-2 Country codes'.</assert>
         <assert test="not(@listID) or @listID = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B05902">Attribute 'listID' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B05903">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B05904">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B05905">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listName) or @listName = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B05906">Attribute 'listName' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listName" flag="fatal" id="EHF-ESPD-RES-B05907">Attribute 'listName' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B05908">Attribute 'listVersionID' MUST contain value '1.0.2'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B05909">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/cac:Country/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B05802">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/cac:ResidenceAddress/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B05302">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/cac:Person/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B04503">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/cac:AgentParty/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B04401">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/cac:PowerOfAttorney/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B04301">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/espd-cac:RepresentativeNaturalPerson/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B04001">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party">
         <assert test="cac:PartyIdentification"
                 flag="fatal"
                 id="EHF-ESPD-RES-B06401">Element 'cac:PartyIdentification' MUST be provided.</assert>
         <assert test="cac:PartyName" flag="fatal" id="EHF-ESPD-RES-B06402">Element 'cac:PartyName' MUST be provided.</assert>
         <assert test="cac:PostalAddress" flag="fatal" id="EHF-ESPD-RES-B06403">Element 'cac:PostalAddress' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cbc:WebsiteURI"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cbc:EndpointID">
         <assert test="not(@schemeID) or @schemeID = 'NO:ORGNR'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B06601">Attribute 'schemeID' MUST contain value 'NO:ORGNR'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B06602">Attribute 'schemeID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PartyIdentification">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B06801">Element 'cbc:ID' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PartyIdentification/cbc:ID">
         <assert test="not(@schemeID) or @schemeID = 'National_Number'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B06901">Attribute 'schemeID' MUST contain value 'National_Number'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B06902">Attribute 'schemeID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PartyName">
         <assert test="cbc:Name" flag="fatal" id="EHF-ESPD-RES-B07101">Element 'cbc:Name' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PartyName/cbc:Name"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress">
         <assert test="cac:Country" flag="fatal" id="EHF-ESPD-RES-B07301">Element 'cac:Country' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cbc:Postbox"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cbc:StreetName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cbc:CityName"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cbc:PostalZone"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cac:Country">
         <assert test="cbc:IdentificationCode" flag="fatal" id="EHF-ESPD-RES-B07801">Element 'cbc:IdentificationCode' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cac:Country/cbc:IdentificationCode">
         <assert test="normalize-space(text()) = 'NO'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B07901">Element 'cbc:IdentificationCode' MUST contain value 'NO'.</assert>
         <assert test="not(@listID) or @listID = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B07902">Attribute 'listID' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B07903">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B07904">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B07905">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listName) or @listName = 'CountryCodeIdentifier'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B07906">Attribute 'listName' MUST contain value 'CountryCodeIdentifier'</assert>
         <assert test="@listName" flag="fatal" id="EHF-ESPD-RES-B07907">Attribute 'listName' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B07908">Attribute 'listVersionID' MUST contain value '1.0.2'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B07909">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/cac:Country/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B07802">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:PostalAddress/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B07302">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:Contact"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:Contact/cbc:Name"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:Contact/cbc:Telephone"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:Contact/cbc:ElectronicMail"/>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/cac:Contact/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B08401">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/cac:Party/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B06404">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/espd-cac:EconomicOperatorParty/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B03802">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ProcurementProjectLot">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B08801">Element 'cbc:ID' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ProcurementProjectLot/cbc:ID">
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B08901">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B08902">Attribute 'schemeAgencyID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:ProcurementProjectLot/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B08802">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B09101">Element 'cbc:ID' MUST be provided.</assert>
         <assert test="cbc:TypeCode" flag="fatal" id="EHF-ESPD-RES-B09102">Element 'cbc:TypeCode' MUST be provided.</assert>
         <assert test="cbc:Name" flag="fatal" id="EHF-ESPD-RES-B09103">Element 'cbc:Name' MUST be provided.</assert>
         <assert test="cbc:Description" flag="fatal" id="EHF-ESPD-RES-B09104">Element 'cbc:Description' MUST be provided.</assert>
         <assert test="ccv:RequirementGroup" flag="fatal" id="EHF-ESPD-RES-B09105">Element 'ccv:RequirementGroup' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/cbc:ID">
         <assert test="not(@schemeID) or @schemeID = 'CriteriaID'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09301">Attribute 'schemeID' MUST contain value 'CriteriaID'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B09302">Attribute 'schemeID' MUST be present.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09303">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B09304">Attribute 'schemeAgencyID' MUST be present.</assert>
         <assert test="not(@schemeVersionID) or @schemeVersionID = '1.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09305">Attribute 'schemeVersionID' MUST contain value '1.0'</assert>
         <assert test="@schemeVersionID" flag="fatal" id="EHF-ESPD-RES-B09306">Attribute 'schemeVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/cbc:TypeCode">
         <assert test="(some $code in $clCriteriaTypeCode satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09701">Value MUST be part of code list 'Criteria Type Codes'.</assert>
         <assert test="not(@listID) or @listID = 'CriteriaTypeCode'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09702">Attribute 'listID' MUST contain value 'CriteriaTypeCode'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B09703">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09704">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B09705">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B09706">Attribute 'listVersionID' MUST contain value '1.0.2'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B09707">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/cbc:Name"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/cbc:Description"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference">
         <assert test="ccv-cbc:Title" flag="fatal" id="EHF-ESPD-RES-B10301">Element 'ccv-cbc:Title' MUST be provided.</assert>
         <assert test="ccv-cbc:JurisdictionLevelCode"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10302">Element 'ccv-cbc:JurisdictionLevelCode' MUST be provided.</assert>
         <assert test="ccv-cbc:Article" flag="fatal" id="EHF-ESPD-RES-B10303">Element 'ccv-cbc:Article' MUST be provided.</assert>
         <assert test="cbc:URI" flag="fatal" id="EHF-ESPD-RES-B10304">Element 'cbc:URI' MUST be provided.</assert>
         <assert test="not(@langID) or (some $code in $clLanguageCodeEU satisfies $code = @langID)"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10305">Value MUST be part of code list 'ISO 639-1 Language Codes'.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:Title"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:Title/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B10501">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/cbc:Description"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:JurisdictionLevelCode">
         <assert test="(some $code in $clCriterionJurisdictionLevel satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10701">Value MUST be part of code list 'Criterion Jurisdiction Level'.</assert>
         <assert test="not(@listID) or @listID = 'CriterionJurisdictionLevel'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10702">Attribute 'listID' MUST contain value 'CriterionJurisdictionLevel'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B10703">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10704">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B10705">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0.2'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B10706">Attribute 'listVersionID' MUST contain value '1.0.2'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B10707">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:JurisdictionLevelCode/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B10708">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:Article"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/ccv-cbc:Article/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B11101">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/cbc:URI"/>
      <rule context="/espd:ESPDResponse/ccv:Criterion/ccv:LegislationReference/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B10306">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:RequirementGroup">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B11301">Element 'cbc:ID' MUST be provided.</assert>
      </rule>
      <rule context="ccv:RequirementGroup/cbc:ID">
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B11501">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B11502">Attribute 'schemeAgencyID' MUST be present.</assert>
         <assert test="not(@schemeVersionID) or @schemeVersionID = '1.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B11503">Attribute 'schemeVersionID' MUST contain value '1.0'</assert>
         <assert test="@schemeVersionID" flag="fatal" id="EHF-ESPD-RES-B11504">Attribute 'schemeVersionID' MUST be present.</assert>
      </rule>
      <rule context="ccv:Requirement">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B11801">Element 'cbc:ID' MUST be provided.</assert>
         <assert test="cbc:Description" flag="fatal" id="EHF-ESPD-RES-B11802">Element 'cbc:Description' MUST be provided.</assert>
         <assert test="@responseDataType" flag="fatal" id="EHF-ESPD-RES-B11803">Attribute 'responseDataType' MUST be present.</assert>
         <assert test="not(@responseDataType) or (some $code in $clResponseDataType satisfies $code = @responseDataType)"
                 flag="fatal"
                 id="EHF-ESPD-RES-B11804">Value MUST be part of code list 'Response Data Types'.</assert>
      </rule>
      <rule context="ccv:Requirement/cbc:ID">
         <assert test="not(@schemeID) or @schemeID = 'CriterionRelatedIDs'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B12001">Attribute 'schemeID' MUST contain value 'CriterionRelatedIDs'</assert>
         <assert test="@schemeID" flag="fatal" id="EHF-ESPD-RES-B12002">Attribute 'schemeID' MUST be present.</assert>
         <assert test="not(@schemeAgencyID) or @schemeAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B12003">Attribute 'schemeAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@schemeAgencyID" flag="fatal" id="EHF-ESPD-RES-B12004">Attribute 'schemeAgencyID' MUST be present.</assert>
         <assert test="not(@schemeVersionID) or @schemeVersionID = '1.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B12005">Attribute 'schemeVersionID' MUST contain value '1.0'</assert>
         <assert test="@schemeVersionID" flag="fatal" id="EHF-ESPD-RES-B12006">Attribute 'schemeVersionID' MUST be present.</assert>
      </rule>
      <rule context="ccv:Requirement/cbc:Description"/>
      <rule context="ccv:Requirement/ccv:Response"/>
      <rule context="ccv:Requirement/ccv:Response/cbc:ID"/>
      <rule context="ccv:Requirement/ccv:Response/ccv-cbc:Indicator"/>
      <rule context="ccv:Requirement/ccv:Response/ccv-cbc:Indicator/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B12701">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cbc:Description"/>
      <rule context="ccv:Requirement/ccv:Response/cbc:Amount">
         <assert test="@currencyID" flag="fatal" id="EHF-ESPD-RES-B12901">Attribute 'currencyID' MUST be present.</assert>
         <assert test="not(@currencyID) or (some $code in $clCurrencyCode satisfies $code = @currencyID)"
                 flag="fatal"
                 id="EHF-ESPD-RES-B12902">Value MUST be part of code list 'ISO 4217 Currency Codes'.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/ccv-cbc:Code"/>
      <rule context="ccv:Requirement/ccv:Response/ccv-cbc:Code/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B13101">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cbc:Date"/>
      <rule context="ccv:Requirement/ccv:Response/cbc:Percent"/>
      <rule context="ccv:Requirement/ccv:Response/cbc:Quantity">
         <assert test="not(@unitCode) or (some $code in $clPeriodMeasureTypeCodes satisfies $code = @unitCode) or (some $code in $clUNECERec20 satisfies $code = @unitCode)"
                 flag="fatal"
                 id="EHF-ESPD-RES-B13401">Value MUST be part of code list 'Period Types' or 'UN/ECE Recommandation 20'.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cac:Period">
         <assert test="cbc:Description" flag="fatal" id="EHF-ESPD-RES-B13601">Element 'cbc:Description' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cac:Period/cbc:Description"/>
      <rule context="ccv:Requirement/ccv:Response/cac:Period/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B13602">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence"/>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cbc:EvidenceName"/>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cbc:EmbeddedEvidenceIndicator">
         <assert test="normalize-space(text()) = 'false'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B14001">Element 'cev-cbc:EmbeddedEvidenceIndicator' MUST contain value 'false'.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceIssuerParty">
         <assert test="cac:PartyName" flag="fatal" id="EHF-ESPD-RES-B14101">Element 'cac:PartyName' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceIssuerParty/cac:PartyName">
         <assert test="cbc:Name" flag="fatal" id="EHF-ESPD-RES-B14201">Element 'cbc:Name' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceIssuerParty/cac:PartyName/cbc:Name"/>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceIssuerParty/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B14102">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference">
         <assert test="cac:Attachment" flag="fatal" id="EHF-ESPD-RES-B14401">Element 'cac:Attachment' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cbc:ID"/>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cac:Attachment">
         <assert test="cac:ExternalReference" flag="fatal" id="EHF-ESPD-RES-B14601">Element 'cac:ExternalReference' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cac:Attachment/cac:ExternalReference">
         <assert test="cbc:URI" flag="fatal" id="EHF-ESPD-RES-B14701">Element 'cbc:URI' MUST be provided.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cac:Attachment/cac:ExternalReference/cbc:URI"/>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cac:Attachment/cac:ExternalReference/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B14702">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/cac:Attachment/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B14602">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/cev-cac:Evidence/cev-cac:EvidenceDocumentReference/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B14402">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:Requirement/ccv:Response/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B12501">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:RequirementGroup/ccv:RequirementGroup"/>
      <rule context="ccv:RequirementGroup/ccv:RequirementGroup/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B14901">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="ccv:RequirementGroup/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B11302">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/ccv:Criterion/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B09106">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference">
         <assert test="cbc:ID" flag="fatal" id="EHF-ESPD-RES-B15001">Element 'cbc:ID' MUST be provided.</assert>
         <assert test="cbc:DocumentTypeCode" flag="fatal" id="EHF-ESPD-RES-B15002">Element 'cbc:DocumentTypeCode' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cbc:ID"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cbc:IssueDate"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cbc:IssueTime"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cbc:DocumentTypeCode">
         <assert test="(some $code in $clDocRefContentType satisfies $code = normalize-space(text()))"
                 flag="fatal"
                 id="EHF-ESPD-RES-B15401">Value MUST be part of code list 'Document Reference Content Types'.</assert>
         <assert test="not(@listID) or @listID = 'ReferencesTypeCodes'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B15402">Attribute 'listID' MUST contain value 'ReferencesTypeCodes'</assert>
         <assert test="@listID" flag="fatal" id="EHF-ESPD-RES-B15403">Attribute 'listID' MUST be present.</assert>
         <assert test="not(@listAgencyID) or @listAgencyID = 'EU-COM-GROW'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B15404">Attribute 'listAgencyID' MUST contain value 'EU-COM-GROW'</assert>
         <assert test="@listAgencyID" flag="fatal" id="EHF-ESPD-RES-B15405">Attribute 'listAgencyID' MUST be present.</assert>
         <assert test="not(@listVersionID) or @listVersionID = '1.0'"
                 flag="fatal"
                 id="EHF-ESPD-RES-B15406">Attribute 'listVersionID' MUST contain value '1.0'</assert>
         <assert test="@listVersionID" flag="fatal" id="EHF-ESPD-RES-B15407">Attribute 'listVersionID' MUST be present.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment">
         <assert test="cac:ExternalReference" flag="fatal" id="EHF-ESPD-RES-B15801">Element 'cac:ExternalReference' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference">
         <assert test="cbc:Description" flag="fatal" id="EHF-ESPD-RES-B15901">Element 'cbc:Description' MUST be provided.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:URI"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:FileName"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/cbc:Description"/>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/cac:ExternalReference/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B15902">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/cac:Attachment/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B15802">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/cac:AdditionalDocumentReference/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B15003">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
      <rule context="/espd:ESPDResponse/*">
         <assert test="false()" flag="fatal" id="EHF-ESPD-RES-B00110">Document MUST NOT contain elements not part of the data model.</assert>
      </rule>
   </pattern>
    <pattern>
      <rule context="espd-req:ESPDRequest | espd:ESPDResponse"/>
      <rule context="ccv:Criterion[cbc:ID[text() = '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab']]">
         <assert id="EHF-ESPD-CT-C0101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION".</assert>
         <assert id="EHF-ESPD-CT-C0102"
                 test="normalize-space(cbc:Name) = 'Participation in a criminal organisation'"
                 flag="fatal">Element 'Name' MUST have value "Participation in a criminal organisation".</assert>
         <assert id="EHF-ESPD-CT-C0103"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for participation in a criminal organisation, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Council Framework Decision 2008/841/JHA of 24 October 2008 on the fight against organised crime (OJ L 300, 11.11.2008, p. 42).'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for participation in a criminal organisation, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Council Framework Decision 2008/841/JHA of 24 October 2008 on the fight against organised crime (OJ L 300, 11.11.2008, p. 42).".</assert>
         <assert id="EHF-ESPD-CT-C0104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0122"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0112"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0114"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c27b7c4e-c837-4529-b867-ed55ce639db5']]">
         <assert id="EHF-ESPD-CT-C0201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.CORRUPTION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.CORRUPTION".</assert>
         <assert id="EHF-ESPD-CT-C0202"
                 test="normalize-space(cbc:Name) = 'Corruption'"
                 flag="fatal">Element 'Name' MUST have value "Corruption".</assert>
         <assert id="EHF-ESPD-CT-C0203"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for corruption, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 3 of the Convention on the fight against corruption involving officials of the European Communities or officials of Member States of the European Union, OJ C 195, 25.6.1997, p. 1, and in Article 2(1) of Council Framework Decision 2003/568/JHA of 22 July 2003 on combating corruption in the private sector (OJ L 192, 31.7.2003, p. 54). This exclusion ground also includes corruption as defined in the national law of the contracting authority (contracting entity) or the economic operator.'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for corruption, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 3 of the Convention on the fight against corruption involving officials of the European Communities or officials of Member States of the European Union, OJ C 195, 25.6.1997, p. 1, and in Article 2(1) of Council Framework Decision 2003/568/JHA of 22 July 2003 on combating corruption in the private sector (OJ L 192, 31.7.2003, p. 54). This exclusion ground also includes corruption as defined in the national law of the contracting authority (contracting entity) or the economic operator.".</assert>
         <assert id="EHF-ESPD-CT-C0204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion 'c27b7c4e-c837-4529-b867-ed55ce639db5' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0222"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'c27b7c4e-c837-4529-b867-ed55ce639db5' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c27b7c4e-c837-4529-b867-ed55ce639db5']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0212"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0214"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '297d2323-3ede-424e-94bc-a91561e6f320']]">
         <assert id="EHF-ESPD-CT-C0301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.FRAUD'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.FRAUD".</assert>
         <assert id="EHF-ESPD-CT-C0302"
                 test="normalize-space(cbc:Name) = 'Fraud'"
                 flag="fatal">Element 'Name' MUST have value "Fraud".</assert>
         <assert id="EHF-ESPD-CT-C0303"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for fraud, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? Within the meaning of Article 1 of the Convention on the protection of the European Communities'' financial interests (OJ C 316, 27.11.1995, p. 48).'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for fraud, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? Within the meaning of Article 1 of the Convention on the protection of the European Communities' financial interests (OJ C 316, 27.11.1995, p. 48).".</assert>
         <assert id="EHF-ESPD-CT-C0304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion '297d2323-3ede-424e-94bc-a91561e6f320' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0322"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '297d2323-3ede-424e-94bc-a91561e6f320' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '297d2323-3ede-424e-94bc-a91561e6f320']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0312"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0314"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd486fb70-86b3-4e75-97f2-0d71b5697c7d']]">
         <assert id="EHF-ESPD-CT-C0401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.TERRORIST_OFFENCES'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.TERRORIST_OFFENCES".</assert>
         <assert id="EHF-ESPD-CT-C0402"
                 test="normalize-space(cbc:Name) = 'Terrorist offences or offences linked to terrorist activities'"
                 flag="fatal">Element 'Name' MUST have value "Terrorist offences or offences linked to terrorist activities".</assert>
         <assert id="EHF-ESPD-CT-C0403"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for terrorist offences or offences linked to terrorist activities, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Articles 1 and 3 of Council Framework Decision of 13 June 2002 on combating terrorism (OJ L 164, 22.6.2002, p. 3). This exclusion ground also includes inciting or aiding or abetting or attempting to commit an offence, as referred to in Article 4 of that Framework Decision.'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for terrorist offences or offences linked to terrorist activities, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Articles 1 and 3 of Council Framework Decision of 13 June 2002 on combating terrorism (OJ L 164, 22.6.2002, p. 3). This exclusion ground also includes inciting or aiding or abetting or attempting to commit an offence, as referred to in Article 4 of that Framework Decision.".</assert>
         <assert id="EHF-ESPD-CT-C0404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion 'd486fb70-86b3-4e75-97f2-0d71b5697c7d' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0422"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'd486fb70-86b3-4e75-97f2-0d71b5697c7d' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd486fb70-86b3-4e75-97f2-0d71b5697c7d']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0412"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0414"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '47112079-6fec-47a3-988f-e561668c3aef']]">
         <assert id="EHF-ESPD-CT-C0501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.MONEY_LAUNDERING'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.MONEY_LAUNDERING".</assert>
         <assert id="EHF-ESPD-CT-C0502"
                 test="normalize-space(cbc:Name) = 'Money laundering or terrorist financing'"
                 flag="fatal">Element 'Name' MUST have value "Money laundering or terrorist financing".</assert>
         <assert id="EHF-ESPD-CT-C0503"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for money laundering or terrorist financing, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 1 of Directive 2005/60/EC of the European Parliament and of the Council of 26 October 2005 on the prevention of the use of the financial system for the purpose of money laundering and terrorist financing (OJ L 309, 25.11.2005, p. 15).'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for money laundering or terrorist financing, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 1 of Directive 2005/60/EC of the European Parliament and of the Council of 26 October 2005 on the prevention of the use of the financial system for the purpose of money laundering and terrorist financing (OJ L 309, 25.11.2005, p. 15).".</assert>
         <assert id="EHF-ESPD-CT-C0504" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion '47112079-6fec-47a3-988f-e561668c3aef' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '47112079-6fec-47a3-988f-e561668c3aef' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '47112079-6fec-47a3-988f-e561668c3aef']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0511"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0512"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0513"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0514"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0515"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd789d01a-fe03-4ccd-9898-73f9cfa080d1']]">
         <assert id="EHF-ESPD-CT-C0601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONVICTIONS.CHILD_LABOUR-HUMAN_TRAFFICKING'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONVICTIONS.CHILD_LABOUR-HUMAN_TRAFFICKING".</assert>
         <assert id="EHF-ESPD-CT-C0602"
                 test="normalize-space(cbc:Name) = 'Child labour and other forms of trafficking in human beings'"
                 flag="fatal">Element 'Name' MUST have value "Child labour and other forms of trafficking in human beings".</assert>
         <assert id="EHF-ESPD-CT-C0603"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for child labour and other forms of trafficking in human beings, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Directive 2011/36/EU of the European Parliament and of the Council of 5 April 2011 on preventing and combating trafficking in human beings and protecting its victims, and replacing Council Framework Decision 2002/629/JHA (OJ L 101, 15.4.2011, p. 1).'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for child labour and other forms of trafficking in human beings, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Directive 2011/36/EU of the European Parliament and of the Council of 5 April 2011 on preventing and combating trafficking in human beings and protecting its victims, and replacing Council Framework Decision 2002/629/JHA (OJ L 101, 15.4.2011, p. 1).".</assert>
         <assert id="EHF-ESPD-CT-C0604" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c637c0c-7703-4389-ba52-02997a055bd7'"
                 flag="fatal">Criterion 'd789d01a-fe03-4ccd-9898-73f9cfa080d1' MUST have RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0622"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'd789d01a-fe03-4ccd-9898-73f9cfa080d1' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd789d01a-fe03-4ccd-9898-73f9cfa080d1']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0611"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0612"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0613"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0614"
                 test="normalize-space(ccv-cbc:Article) = '57(1)'"
                 flag="fatal">Element 'Article' MUST have value "57(1)".</assert>
         <assert id="EHF-ESPD-CT-C0615"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4']]">
         <assert id="EHF-ESPD-CT-C0701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_TAXES'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_TAXES".</assert>
         <assert id="EHF-ESPD-CT-C0702"
                 test="normalize-space(cbc:Name) = 'Payment of taxes'"
                 flag="fatal">Element 'Name' MUST have value "Payment of taxes".</assert>
         <assert id="EHF-ESPD-CT-C0703"
                 test="normalize-space(cbc:Description) = 'Has the economic operator breached its obligations relating to the payment of taxes, both in the country in which it is established and in Member State of the contracting authority or contracting entity if other than the country of establishment?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator breached its obligations relating to the payment of taxes, both in the country in which it is established and in Member State of the contracting authority or contracting entity if other than the country of establishment?".</assert>
         <assert id="EHF-ESPD-CT-C0704" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '098fd3cc-466e-4233-af1a-affe09471bce'"
                 flag="fatal">Criterion 'b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4' MUST have RequirementGroup '098fd3cc-466e-4233-af1a-affe09471bce' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0722"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b61bbeb7-690e-4a40-bc68-d6d4ecfaa3d4']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0711"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0712"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0713"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0714"
                 test="normalize-space(ccv-cbc:Article) = '57(2)'"
                 flag="fatal">Element 'Article' MUST have value "57(2)".</assert>
         <assert id="EHF-ESPD-CT-C0715"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7d85e333-bbab-49c0-be8d-c36d71a72f5e']]">
         <assert id="EHF-ESPD-CT-C0801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_SOCIAL_SECURITY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONTRIBUTIONS.PAYMENT_OF_SOCIAL_SECURITY".</assert>
         <assert id="EHF-ESPD-CT-C0802"
                 test="normalize-space(cbc:Name) = 'Payment of social security contributions'"
                 flag="fatal">Element 'Name' MUST have value "Payment of social security contributions".</assert>
         <assert id="EHF-ESPD-CT-C0803"
                 test="normalize-space(cbc:Description) = 'Has the economic operator breached its obligations relating to the payment social security contributions, both in the country in which it is established and in Member State of the contracting authority or contracting entity if other than the country of establishment?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator breached its obligations relating to the payment social security contributions, both in the country in which it is established and in Member State of the contracting authority or contracting entity if other than the country of establishment?".</assert>
         <assert id="EHF-ESPD-CT-C0804" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '098fd3cc-466e-4233-af1a-affe09471bce'"
                 flag="fatal">Criterion '7d85e333-bbab-49c0-be8d-c36d71a72f5e' MUST have RequirementGroup '098fd3cc-466e-4233-af1a-affe09471bce' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C0822"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '7d85e333-bbab-49c0-be8d-c36d71a72f5e' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7d85e333-bbab-49c0-be8d-c36d71a72f5e']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0811"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0812"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0813"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0814"
                 test="normalize-space(ccv-cbc:Article) = '57(2)'"
                 flag="fatal">Element 'Article' MUST have value "57(2)".</assert>
         <assert id="EHF-ESPD-CT-C0815"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a80ddb62-d25b-4e4e-ae22-3968460dc0a9']]">
         <assert id="EHF-ESPD-CT-C0901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.SOCIAL.ENVIRONMENTAL_LAW'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.SOCIAL.ENVIRONMENTAL_LAW".</assert>
         <assert id="EHF-ESPD-CT-C0902"
                 test="normalize-space(cbc:Name) = 'Breaching of obligations in the fields of environmental law'"
                 flag="fatal">Element 'Name' MUST have value "Breaching of obligations in the fields of environmental law".</assert>
         <assert id="EHF-ESPD-CT-C0903"
                 test="normalize-space(cbc:Description) = 'Has the economic operator, to its knowledge, breached its obligations in the fields of environmental law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator, to its knowledge, breached its obligations in the fields of environmental law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.".</assert>
         <assert id="EHF-ESPD-CT-C0904" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C0921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '976b5acb-c00f-46ca-8f83-5ce6abfdfe43'"
                 flag="fatal">Criterion 'a80ddb62-d25b-4e4e-ae22-3968460dc0a9' MUST have RequirementGroup '976b5acb-c00f-46ca-8f83-5ce6abfdfe43' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a80ddb62-d25b-4e4e-ae22-3968460dc0a9']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C0911"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C0912"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C0913"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C0914"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C0915"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a261a395-ed17-4939-9c75-b9ff1109ca6e']]">
         <assert id="EHF-ESPD-CT-C1001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.SOCIAL.SOCIAL_LAW'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.SOCIAL.SOCIAL_LAW".</assert>
         <assert id="EHF-ESPD-CT-C1002"
                 test="normalize-space(cbc:Name) = 'Breaching of obligations in the fields of social law'"
                 flag="fatal">Element 'Name' MUST have value "Breaching of obligations in the fields of social law".</assert>
         <assert id="EHF-ESPD-CT-C1003"
                 test="normalize-space(cbc:Description) = 'Has the economic operator, to its knowledge, breached its obligations in the fields of social law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator, to its knowledge, breached its obligations in the fields of social law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.".</assert>
         <assert id="EHF-ESPD-CT-C1004" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '976b5acb-c00f-46ca-8f83-5ce6abfdfe43'"
                 flag="fatal">Criterion 'a261a395-ed17-4939-9c75-b9ff1109ca6e' MUST have RequirementGroup '976b5acb-c00f-46ca-8f83-5ce6abfdfe43' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a261a395-ed17-4939-9c75-b9ff1109ca6e']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1011"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1012"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1013"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1014"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1015"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a34b70d6-c43d-4726-9a88-8e2b438424bf']]">
         <assert id="EHF-ESPD-CT-C1101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.SOCIAL.LABOUR_LAW'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.SOCIAL.LABOUR_LAW".</assert>
         <assert id="EHF-ESPD-CT-C1102"
                 test="normalize-space(cbc:Name) = 'Breaching of obligations in the fields of labour law'"
                 flag="fatal">Element 'Name' MUST have value "Breaching of obligations in the fields of labour law".</assert>
         <assert id="EHF-ESPD-CT-C1103"
                 test="normalize-space(cbc:Description) = 'Has the economic operator, to its knowledge, breached its obligations in the fields of labour law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator, to its knowledge, breached its obligations in the fields of labour law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU.".</assert>
         <assert id="EHF-ESPD-CT-C1104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '976b5acb-c00f-46ca-8f83-5ce6abfdfe43'"
                 flag="fatal">Criterion 'a34b70d6-c43d-4726-9a88-8e2b438424bf' MUST have RequirementGroup '976b5acb-c00f-46ca-8f83-5ce6abfdfe43' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a34b70d6-c43d-4726-9a88-8e2b438424bf']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1112"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1114"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd3732c09-7d62-4edc-a172-241da6636e7c']]">
         <assert id="EHF-ESPD-CT-C1201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY".</assert>
         <assert id="EHF-ESPD-CT-C1202"
                 test="normalize-space(cbc:Name) = 'Bankruptcy'"
                 flag="fatal">Element 'Name' MUST have value "Bankruptcy".</assert>
         <assert id="EHF-ESPD-CT-C1203"
                 test="normalize-space(cbc:Description) = 'Is the economic operator bankrupt? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator bankrupt? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion 'd3732c09-7d62-4edc-a172-241da6636e7c' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1222"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'd3732c09-7d62-4edc-a172-241da6636e7c' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd3732c09-7d62-4edc-a172-241da6636e7c']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1212"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1214"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '396f288a-e267-4c20-851a-ed4f7498f137']]">
         <assert id="EHF-ESPD-CT-C1301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.INSOLVENCY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.INSOLVENCY".</assert>
         <assert id="EHF-ESPD-CT-C1302"
                 test="normalize-space(cbc:Name) = 'Insolvency'"
                 flag="fatal">Element 'Name' MUST have value "Insolvency".</assert>
         <assert id="EHF-ESPD-CT-C1303"
                 test="normalize-space(cbc:Description) = 'Is the economic operator the subject of insolvency or winding-up? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator the subject of insolvency or winding-up? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion '396f288a-e267-4c20-851a-ed4f7498f137' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1322"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '396f288a-e267-4c20-851a-ed4f7498f137' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '396f288a-e267-4c20-851a-ed4f7498f137']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1312"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1314"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '68918c7a-f5bc-4a1a-a62f-ad8983600d48']]">
         <assert id="EHF-ESPD-CT-C1401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.CREDITORS_ARRANGEMENT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.CREDITORS_ARRANGEMENT".</assert>
         <assert id="EHF-ESPD-CT-C1402"
                 test="normalize-space(cbc:Name) = 'Arrangement with creditors'"
                 flag="fatal">Element 'Name' MUST have value "Arrangement with creditors".</assert>
         <assert id="EHF-ESPD-CT-C1403"
                 test="normalize-space(cbc:Description) = 'Is the economic operator in arrangement with creditors? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator in arrangement with creditors? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion '68918c7a-f5bc-4a1a-a62f-ad8983600d48' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1422"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '68918c7a-f5bc-4a1a-a62f-ad8983600d48' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '68918c7a-f5bc-4a1a-a62f-ad8983600d48']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1412"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1414"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'daffa2a9-9f8f-4568-8be8-7b8bf306d096']]">
         <assert id="EHF-ESPD-CT-C1501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY_ANALOGOUS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.BANKRUPTCY_ANALOGOUS".</assert>
         <assert id="EHF-ESPD-CT-C1502"
                 test="normalize-space(cbc:Name) = 'Analogous situation like bankruptcy under national law'"
                 flag="fatal">Element 'Name' MUST have value "Analogous situation like bankruptcy under national law".</assert>
         <assert id="EHF-ESPD-CT-C1503"
                 test="normalize-space(cbc:Description) = 'Is the economic operator in any analogous situation like bankruptcy arising from a similar procedure under national laws and regulations? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator in any analogous situation like bankruptcy arising from a similar procedure under national laws and regulations? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1504" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion 'daffa2a9-9f8f-4568-8be8-7b8bf306d096' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion 'daffa2a9-9f8f-4568-8be8-7b8bf306d096' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'daffa2a9-9f8f-4568-8be8-7b8bf306d096']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1511"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1512"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1513"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1514"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1515"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '8fda202a-0c37-41bb-9d7d-de3f49edbfcb']]">
         <assert id="EHF-ESPD-CT-C1601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.LIQUIDATOR_ADMINISTERED'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.LIQUIDATOR_ADMINISTERED".</assert>
         <assert id="EHF-ESPD-CT-C1602"
                 test="normalize-space(cbc:Name) = 'Assets being administered by liquidator'"
                 flag="fatal">Element 'Name' MUST have value "Assets being administered by liquidator".</assert>
         <assert id="EHF-ESPD-CT-C1603"
                 test="normalize-space(cbc:Description) = 'Are the assets of the economic operator being administered by a liquidator or by the court? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Are the assets of the economic operator being administered by a liquidator or by the court?  This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1604" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion '8fda202a-0c37-41bb-9d7d-de3f49edbfcb' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1622"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '8fda202a-0c37-41bb-9d7d-de3f49edbfcb' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '8fda202a-0c37-41bb-9d7d-de3f49edbfcb']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1611"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1612"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1613"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1614"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1615"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '166536e2-77f7-455c-b018-70582474e4f6']]">
         <assert id="EHF-ESPD-CT-C1701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.BUSINESS.ACTIVITIES_SUSPENDED'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.BUSINESS.ACTIVITIES_SUSPENDED".</assert>
         <assert id="EHF-ESPD-CT-C1702"
                 test="normalize-space(cbc:Name) = 'Business activities are suspended'"
                 flag="fatal">Element 'Name' MUST have value "Business activities are suspended".</assert>
         <assert id="EHF-ESPD-CT-C1703"
                 test="normalize-space(cbc:Description) = 'Are the business activities of the economic operator suspended? This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.'"
                 flag="fatal">Element 'Description' MUST have value "Are the business activities of the economic operator suspended?  This information needs not be given if exclusion of economic operators in this case has been made mandatory under the applicable national law without any possibility of derogation where the economic operator is nevertheless able to perform the contract.".</assert>
         <assert id="EHF-ESPD-CT-C1704" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555'"
                 flag="fatal">Criterion '166536e2-77f7-455c-b018-70582474e4f6' MUST have RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C1722"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '7458d42a-e581-4640-9283-34ceb3ad4345'"
                 flag="fatal">Criterion '166536e2-77f7-455c-b018-70582474e4f6' MUST have RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '166536e2-77f7-455c-b018-70582474e4f6']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1711"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1712"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1713"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1714"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1715"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '514d3fde-1e3e-4dcd-b02a-9f984d5bbda3']]">
         <assert id="EHF-ESPD-CT-C1801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.MISCONDUCT.MC_PROFESSIONAL'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.MISCONDUCT.MC_PROFESSIONAL".</assert>
         <assert id="EHF-ESPD-CT-C1802"
                 test="normalize-space(cbc:Name) = 'Guilty of grave professional misconduct'"
                 flag="fatal">Element 'Name' MUST have value "Guilty of grave professional misconduct".</assert>
         <assert id="EHF-ESPD-CT-C1803"
                 test="normalize-space(cbc:Description) = 'Is the economic operator  guilty of grave professional misconduct? Where applicable, see definitions in national law, the relevant notice or the procurement documents.'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator  guilty of grave professional misconduct? Where applicable, see definitions in national law, the relevant notice or the procurement documents.".</assert>
         <assert id="EHF-ESPD-CT-C1804" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8'"
                 flag="fatal">Criterion '514d3fde-1e3e-4dcd-b02a-9f984d5bbda3' MUST have RequirementGroup '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '514d3fde-1e3e-4dcd-b02a-9f984d5bbda3']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1811"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1812"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1813"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1814"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1815"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '56d13e3d-76e8-4f23-8af6-13e60a2ee356']]">
         <assert id="EHF-ESPD-CT-C1901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.MISCONDUCT.MARKET_DISTORTION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.MISCONDUCT.MARKET_DISTORTION".</assert>
         <assert id="EHF-ESPD-CT-C1902"
                 test="normalize-space(cbc:Name) = 'Agreements with other economic operators aimed at distorting competition'"
                 flag="fatal">Element 'Name' MUST have value "Agreements with other economic operators aimed at distorting competition".</assert>
         <assert id="EHF-ESPD-CT-C1903"
                 test="normalize-space(cbc:Description) = 'Has the economic operator entered into agreements with other economic operators aimed at distorting competition?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator entered into agreements with other economic operators aimed at distorting competition?".</assert>
         <assert id="EHF-ESPD-CT-C1904" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C1921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8'"
                 flag="fatal">Criterion '56d13e3d-76e8-4f23-8af6-13e60a2ee356' MUST have RequirementGroup '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '56d13e3d-76e8-4f23-8af6-13e60a2ee356']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C1911"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C1912"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C1913"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C1914"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C1915"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b1b5ac18-f393-4280-9659-1367943c1a2e']]">
         <assert id="EHF-ESPD-CT-C2001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PARTICIPATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PARTICIPATION".</assert>
         <assert id="EHF-ESPD-CT-C2002"
                 test="normalize-space(cbc:Name) = 'Conflict of interest due to its participation in the procurement procedure'"
                 flag="fatal">Element 'Name' MUST have value "Conflict of interest due to its participation in the procurement procedure".</assert>
         <assert id="EHF-ESPD-CT-C2003"
                 test="normalize-space(cbc:Description) = 'Is the economic operator aware of any conflict of interest, as indicated in national law, the relevant notice or the procurement documents due to its participation in the procurement procedure?'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator aware of any conflict of interest, as indicated in national law, the relevant notice or the procurement documents due to its participation in the procurement procedure?".</assert>
         <assert id="EHF-ESPD-CT-C2004" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '30450436-f559-4dfa-98ba-f0842ed9d2a0'"
                 flag="fatal">Criterion 'b1b5ac18-f393-4280-9659-1367943c1a2e' MUST have RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b1b5ac18-f393-4280-9659-1367943c1a2e']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2011"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2012"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2013"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2014"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C2015"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '61874050-5130-4f1c-a174-720939c7b483']]">
         <assert id="EHF-ESPD-CT-C2101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PREPARATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.PROCEDURE_PREPARATION".</assert>
         <assert id="EHF-ESPD-CT-C2102"
                 test="normalize-space(cbc:Name) = 'Direct or indirect involvement in the preparation of this procurement procedure'"
                 flag="fatal">Element 'Name' MUST have value "Direct or indirect involvement in the preparation of this procurement procedure".</assert>
         <assert id="EHF-ESPD-CT-C2103"
                 test="normalize-space(cbc:Description) = 'Has the economic operator or an undertaking related to it advised the contracting authority or contracting entity or otherwise been involved in the preparation of the procurement procedure?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator or an undertaking related to it advised the contracting authority or contracting entity or otherwise been involved in the preparation of the procurement procedure?".</assert>
         <assert id="EHF-ESPD-CT-C2104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '77ae3f29-7c5f-4afa-af97-24afec48c5bf'"
                 flag="fatal">Criterion '61874050-5130-4f1c-a174-720939c7b483' MUST have RequirementGroup '77ae3f29-7c5f-4afa-af97-24afec48c5bf' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '61874050-5130-4f1c-a174-720939c7b483']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2112"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2114"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C2115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3293e92b-7f3e-42f1-bee6-a7641bb04251']]">
         <assert id="EHF-ESPD-CT-C2201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.EARLY_TERMINATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.EARLY_TERMINATION".</assert>
         <assert id="EHF-ESPD-CT-C2202"
                 test="normalize-space(cbc:Name) = 'Early termination, damages or other comparable sanctions'"
                 flag="fatal">Element 'Name' MUST have value "Early termination, damages or other comparable sanctions".</assert>
         <assert id="EHF-ESPD-CT-C2203"
                 test="normalize-space(cbc:Description) = 'Has the economic operator experienced that a prior public contract, a prior contract with a contracting entity or a prior concession contract was terminated early, or that damages or other comparable sanctions were imposed in connection with that prior contract?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator experienced that a prior public contract, a prior contract with a contracting entity or a prior concession contract was terminated early, or that damages or other comparable sanctions were imposed in connection with that prior contract?".</assert>
         <assert id="EHF-ESPD-CT-C2204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8'"
                 flag="fatal">Criterion '3293e92b-7f3e-42f1-bee6-a7641bb04251' MUST have RequirementGroup '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3293e92b-7f3e-42f1-bee6-a7641bb04251']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2212"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2214"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C2215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '696a75b2-6107-428f-8b74-82affb67e184']]">
         <assert id="EHF-ESPD-CT-C2301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.MISINTERPRETATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.MISINTERPRETATION".</assert>
         <assert id="EHF-ESPD-CT-C2302"
                 test="normalize-space(cbc:Name) = 'Guilty of misinterpretation, withheld information, unable to provide required documents and obtained confidential information of this procedure'"
                 flag="fatal">Element 'Name' MUST have value "Guilty of misinterpretation, withheld information, unable to provide required documents and obtained confidential information of this procedure".</assert>
         <assert id="EHF-ESPD-CT-C2303"
                 test="normalize-space(cbc:Description) = 'Can the economic operator confirm the four exclusion grounds, that it has not been guilty of serious misrepresentation in supplying the information required for the verification of the absence of grounds for exclusion or the fulfilment of the selection criteria, that it has not withheld such information, it has been able, without delay, to submit the supporting documents required by a contracting authority or contracting entity, and it has not undertaken to unduly influence the decision making process of the contracting authority or contracting entity, to obtain confidential information that may confer upon it undue advantages in the procurement procedure or to negligently provide misleading information that may have a material influence on decisions concerning exclusion, selection or award?'"
                 flag="fatal">Element 'Description' MUST have value "Can the economic operator confirm the four exclusion grounds, that it has not been guilty of serious misrepresentation in supplying the information required for the verification of the absence of grounds for exclusion or the fulfilment of the selection criteria, that it has not withheld such information, it has been able, without delay, to submit the supporting documents required by a contracting authority or contracting entity, and it has not undertaken to unduly influence the decision making process of the contracting authority or contracting entity, to obtain confidential information that may confer upon it undue advantages in the procurement procedure or to negligently provide misleading information that may have a material influence on decisions concerning exclusion, selection or award?".</assert>
         <assert id="EHF-ESPD-CT-C2304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '30450436-f559-4dfa-98ba-f0842ed9d2a0'"
                 flag="fatal">Criterion '696a75b2-6107-428f-8b74-82affb67e184' MUST have RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '696a75b2-6107-428f-8b74-82affb67e184']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2312"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2314"
                 test="normalize-space(ccv-cbc:Article) = '57(4)'"
                 flag="fatal">Element 'Article' MUST have value "57(4)".</assert>
         <assert id="EHF-ESPD-CT-C2315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '6ee55a59-6adb-4c3a-b89f-e62a7ad7be7f']]">
         <assert id="EHF-ESPD-CT-C2401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.SUITABILITY.PROFESSIONAL_REGISTER_ENROLMENT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.SUITABILITY.PROFESSIONAL_REGISTER_ENROLMENT".</assert>
         <assert id="EHF-ESPD-CT-C2402"
                 test="normalize-space(cbc:Name) = 'Enrolment in a relevant professional register'"
                 flag="fatal">Element 'Name' MUST have value "Enrolment in a relevant professional register".</assert>
         <assert id="EHF-ESPD-CT-C2403"
                 test="normalize-space(cbc:Description) = 'It is enrolled in relevant professional registers kept in the Member State of its establishment as described in Annex XI of Directive 2014/24/EU; economic operators from certain Member States may have to comply with other requirements set out in that Annex.'"
                 flag="fatal">Element 'Description' MUST have value "It is enrolled in relevant professional registers kept in the Member State of its establishment as described in Annex XI of Directive 2014/24/EU; economic operators from certain Member States may have to comply with other requirements set out in that Annex.".</assert>
         <assert id="EHF-ESPD-CT-C2404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '1768de86-a6c8-48e4-bd8e-de2f2f7424d0'"
                 flag="fatal">Criterion '6ee55a59-6adb-4c3a-b89f-e62a7ad7be7f' MUST have RequirementGroup '1768de86-a6c8-48e4-bd8e-de2f2f7424d0' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2422"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '6ee55a59-6adb-4c3a-b89f-e62a7ad7be7f' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '6ee55a59-6adb-4c3a-b89f-e62a7ad7be7f']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2412"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2414"
                 test="normalize-space(ccv-cbc:Article) = '58(2)'"
                 flag="fatal">Element 'Article' MUST have value "58(2)".</assert>
         <assert id="EHF-ESPD-CT-C2415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '87b3fa26-3549-4f92-b8e0-3fd8f04bf5c7']]">
         <assert id="EHF-ESPD-CT-C2501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.SUITABILITY.TRADE_REGISTER_ENROLMENT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.SUITABILITY.TRADE_REGISTER_ENROLMENT".</assert>
         <assert id="EHF-ESPD-CT-C2502"
                 test="normalize-space(cbc:Name) = 'Enrolment in a trade register'"
                 flag="fatal">Element 'Name' MUST have value "Enrolment in a trade register".</assert>
         <assert id="EHF-ESPD-CT-C2503"
                 test="normalize-space(cbc:Description) = 'It is enrolled in trade registers kept in the Member State of its establishment as described in Annex XI of Directive 2014/24/EU; economic operators from certain Member States may have to comply with other requirements set out in that Annex.'"
                 flag="fatal">Element 'Description' MUST have value "It is enrolled in trade registers kept in the Member State of its establishment as described in Annex XI of Directive 2014/24/EU; economic operators from certain Member States may have to comply with other requirements set out in that Annex.".</assert>
         <assert id="EHF-ESPD-CT-C2504" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '1768de86-a6c8-48e4-bd8e-de2f2f7424d0'"
                 flag="fatal">Criterion '87b3fa26-3549-4f92-b8e0-3fd8f04bf5c7' MUST have RequirementGroup '1768de86-a6c8-48e4-bd8e-de2f2f7424d0' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '87b3fa26-3549-4f92-b8e0-3fd8f04bf5c7' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '87b3fa26-3549-4f92-b8e0-3fd8f04bf5c7']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2511"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2512"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2513"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2514"
                 test="normalize-space(ccv-cbc:Article) = '58(2)'"
                 flag="fatal">Element 'Article' MUST have value "58(2)".</assert>
         <assert id="EHF-ESPD-CT-C2515"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4']]">
         <assert id="EHF-ESPD-CT-C2601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.SUITABILITY.AUTHORISATION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.SUITABILITY.AUTHORISATION".</assert>
         <assert id="EHF-ESPD-CT-C2602"
                 test="normalize-space(cbc:Name) = 'For service contracts: authorisation of particular organisation needed'"
                 flag="fatal">Element 'Name' MUST have value "For service contracts: authorisation of particular organisation needed".</assert>
         <assert id="EHF-ESPD-CT-C2603"
                 test="normalize-space(cbc:Description) = 'Is a particular authorisation of a particular organisation needed in order to be able to perform the service in question in the country of establishment of the economic operator?'"
                 flag="fatal">Element 'Description' MUST have value "Is a particular authorisation of a particular organisation needed in order to be able to perform the service in question in the country of establishment of the economic operator?".</assert>
         <assert id="EHF-ESPD-CT-C2604" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'a109e144-f65e-469d-bcda-220f1af34b6c'"
                 flag="fatal">Criterion '9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4' MUST have RequirementGroup 'a109e144-f65e-469d-bcda-220f1af34b6c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2622"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9eeb6d5c-0eb8-48e8-a4c5-5087a7c095a4']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2611"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2612"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2613"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2614"
                 test="normalize-space(ccv-cbc:Article) = '58(2)'"
                 flag="fatal">Element 'Article' MUST have value "58(2)".</assert>
         <assert id="EHF-ESPD-CT-C2615"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '73f10e36-ed7a-412e-995c-aa76463e3776']]">
         <assert id="EHF-ESPD-CT-C2701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.SUITABILITY.MEMBERSHIP'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.SUITABILITY.MEMBERSHIP".</assert>
         <assert id="EHF-ESPD-CT-C2702"
                 test="normalize-space(cbc:Name) = 'For service contracts: membership of particular organisation needed'"
                 flag="fatal">Element 'Name' MUST have value "For service contracts: membership of particular organisation needed".</assert>
         <assert id="EHF-ESPD-CT-C2703"
                 test="normalize-space(cbc:Description) = 'Is a particular membership of a particular organisation needed in order to be able to perform the service in question in the country of establishment of the economic operator?'"
                 flag="fatal">Element 'Description' MUST have value "Is a particular membership of a particular organisation needed in order to be able to perform the service in question in the country of establishment of the economic operator?".</assert>
         <assert id="EHF-ESPD-CT-C2704" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'a109e144-f65e-469d-bcda-220f1af34b6c'"
                 flag="fatal">Criterion '73f10e36-ed7a-412e-995c-aa76463e3776' MUST have RequirementGroup 'a109e144-f65e-469d-bcda-220f1af34b6c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2722"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '73f10e36-ed7a-412e-995c-aa76463e3776' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '73f10e36-ed7a-412e-995c-aa76463e3776']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2711"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2712"
                 test="normalize-space(cbc:Description) = 'Directive 2014/24/EU'"
                 flag="fatal">Element 'Description' MUST have value "Directive 2014/24/EU".</assert>
         <assert id="EHF-ESPD-CT-C2713"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2714"
                 test="normalize-space(ccv-cbc:Article) = '58(2)'"
                 flag="fatal">Element 'Article' MUST have value "58(2)".</assert>
         <assert id="EHF-ESPD-CT-C2715"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '499efc97-2ac1-4af2-9e84-323c2ca67747']]">
         <assert id="EHF-ESPD-CT-C2801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.GENERAL_YEARLY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.GENERAL_YEARLY".</assert>
         <assert id="EHF-ESPD-CT-C2802"
                 test="normalize-space(cbc:Name) = 'General yearly turnover'"
                 flag="fatal">Element 'Name' MUST have value "General yearly turnover".</assert>
         <assert id="EHF-ESPD-CT-C2803"
                 test="normalize-space(cbc:Description) = 'Its general yearly turnover for the number of financial years required in the relevant notice, the procurement documents or the ESPD is as follows:'"
                 flag="fatal">Element 'Description' MUST have value "Its general yearly turnover for the number of financial years required in the relevant notice, the procurement documents or the ESPD is as follows:".</assert>
         <assert id="EHF-ESPD-CT-C2804" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2822"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C2823"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C2824"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-C2825"
                 test="ccv:RequirementGroup[5] and normalize-space(ccv:RequirementGroup[5][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 5.</assert>
         <assert id="EHF-ESPD-CT-C2826"
                 test="ccv:RequirementGroup[6] and normalize-space(ccv:RequirementGroup[6][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '499efc97-2ac1-4af2-9e84-323c2ca67747' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 6.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '499efc97-2ac1-4af2-9e84-323c2ca67747']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2811"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2812"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2813"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2814"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C2815"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b16cb9fc-6cb7-4585-9302-9533b415cf48']]">
         <assert id="EHF-ESPD-CT-C2901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.AVERAGE_YEARLY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.AVERAGE_YEARLY".</assert>
         <assert id="EHF-ESPD-CT-C2902"
                 test="normalize-space(cbc:Name) = 'Average yearly turnover'"
                 flag="fatal">Element 'Name' MUST have value "Average yearly turnover".</assert>
         <assert id="EHF-ESPD-CT-C2903"
                 test="normalize-space(cbc:Description) = 'Its average yearly turnover for the number of years required in the relevant notice, the procurement documents or the ESPD is as follows:'"
                 flag="fatal">Element 'Description' MUST have value "Its average yearly turnover for the number of years required in the relevant notice, the procurement documents or the ESPD is as follows:".</assert>
         <assert id="EHF-ESPD-CT-C2904" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C2921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'e1886054-ada4-473c-9afc-2fde82c24cf4'"
                 flag="fatal">Criterion 'b16cb9fc-6cb7-4585-9302-9533b415cf48' MUST have RequirementGroup 'e1886054-ada4-473c-9afc-2fde82c24cf4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C2922"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'b16cb9fc-6cb7-4585-9302-9533b415cf48' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'b16cb9fc-6cb7-4585-9302-9533b415cf48']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C2911"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2912"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C2913"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C2914"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C2915"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '074f6031-55f9-4e99-b9a4-c4363e8bc315']]">
         <assert id="EHF-ESPD-CT-C3001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_YEARLY'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_YEARLY".</assert>
         <assert id="EHF-ESPD-CT-C3002"
                 test="normalize-space(cbc:Name) = 'Specific yearly turnover'"
                 flag="fatal">Element 'Name' MUST have value "Specific yearly turnover".</assert>
         <assert id="EHF-ESPD-CT-C3003"
                 test="normalize-space(cbc:Description) = 'Its specific yearly turnover in the business area covered by the contract for the number of financial years required in the relevant notice, the procurement documents or the ESPD is as follows:'"
                 flag="fatal">Element 'Description' MUST have value "Its specific yearly turnover in the business area covered by the contract for the number of financial years required in the relevant notice, the procurement documents or the ESPD is as follows:".</assert>
         <assert id="EHF-ESPD-CT-C3004" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3022"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C3023"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C3024"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-C3025"
                 test="ccv:RequirementGroup[5] and normalize-space(ccv:RequirementGroup[5][current()]/cbc:ID/text()) = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' as child number 5.</assert>
         <assert id="EHF-ESPD-CT-C3026"
                 test="ccv:RequirementGroup[6] and normalize-space(ccv:RequirementGroup[6][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '074f6031-55f9-4e99-b9a4-c4363e8bc315' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 6.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '074f6031-55f9-4e99-b9a4-c4363e8bc315']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3011"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3012"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3013"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3014"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C3015"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd3dfb714-f558-4512-bbc5-e456fa2339de']]">
         <assert id="EHF-ESPD-CT-C3101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_AVERAGE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SPECIFIC_AVERAGE".</assert>
         <assert id="EHF-ESPD-CT-C3102"
                 test="normalize-space(cbc:Name) = 'Specific average turnover'"
                 flag="fatal">Element 'Name' MUST have value "Specific average turnover".</assert>
         <assert id="EHF-ESPD-CT-C3103"
                 test="normalize-space(cbc:Description) = 'Its specific average yearly turnover in the business area covered by the contract for the number of years required in the relevant notice, the procurement documents or the ESPD is as follows:'"
                 flag="fatal">Element 'Description' MUST have value "Its specific average yearly turnover in the business area covered by the contract for the number of years required in the relevant notice, the procurement documents or the ESPD is as follows:".</assert>
         <assert id="EHF-ESPD-CT-C3104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'e1886054-ada4-473c-9afc-2fde82c24cf4'"
                 flag="fatal">Criterion 'd3dfb714-f558-4512-bbc5-e456fa2339de' MUST have RequirementGroup 'e1886054-ada4-473c-9afc-2fde82c24cf4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3122"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'd3dfb714-f558-4512-bbc5-e456fa2339de' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd3dfb714-f558-4512-bbc5-e456fa2339de']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3112"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3114"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C3115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '77f481ce-ffb6-483f-8e2b-c78db5e68292']]">
         <assert id="EHF-ESPD-CT-C3201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SET_UP'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.TURNOVER.SET_UP".</assert>
         <assert id="EHF-ESPD-CT-C3202"
                 test="normalize-space(cbc:Name) = 'Set up of economic operator'"
                 flag="fatal">Element 'Name' MUST have value "Set up of economic operator".</assert>
         <assert id="EHF-ESPD-CT-C3203"
                 test="normalize-space(cbc:Description) = 'In case the information concerning turnover (general or specific) is not available for the entire period required, please state the date on which the economic operator was set up or started trading:'"
                 flag="fatal">Element 'Description' MUST have value "In case the information concerning turnover (general or specific) is not available for the entire period required, please state the date on which the economic operator was set up or started trading:".</assert>
         <assert id="EHF-ESPD-CT-C3204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'e9aa7763-c167-4352-8060-1a3d7d3e2662'"
                 flag="fatal">Criterion '77f481ce-ffb6-483f-8e2b-c78db5e68292' MUST have RequirementGroup 'e9aa7763-c167-4352-8060-1a3d7d3e2662' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '77f481ce-ffb6-483f-8e2b-c78db5e68292']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3212"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3214"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C3215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46']]">
         <assert id="EHF-ESPD-CT-C3301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.FINANCIAL_RATIO'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.FINANCIAL_RATIO".</assert>
         <assert id="EHF-ESPD-CT-C3302"
                 test="normalize-space(cbc:Name) = 'Financial ratio'"
                 flag="fatal">Element 'Name' MUST have value "Financial ratio".</assert>
         <assert id="EHF-ESPD-CT-C3303"
                 test="normalize-space(cbc:Description) = 'Concerning the financial ratios specified in the relevant notice, the procurement documents or the ESPD, the economic operator declares that the actual values for the required ratios are as follows:'"
                 flag="fatal">Element 'Description' MUST have value "Concerning the financial ratios  specified in the relevant notice, the procurement documents or the ESPD, the economic operator declares that the actual values for the required ratios are as follows:".</assert>
         <assert id="EHF-ESPD-CT-C3304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '096686e1-82ca-4de0-8710-d74d90da0f0c'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3322"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '096686e1-82ca-4de0-8710-d74d90da0f0c'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C3323"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '096686e1-82ca-4de0-8710-d74d90da0f0c'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C3324"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '096686e1-82ca-4de0-8710-d74d90da0f0c'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-C3325"
                 test="ccv:RequirementGroup[5] and normalize-space(ccv:RequirementGroup[5][current()]/cbc:ID/text()) = '096686e1-82ca-4de0-8710-d74d90da0f0c'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' as child number 5.</assert>
         <assert id="EHF-ESPD-CT-C3326"
                 test="ccv:RequirementGroup[6] and normalize-space(ccv:RequirementGroup[6][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 6.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'e4d37adc-08cd-4f4d-a8d8-32b62b0a1f46']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3312"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3314"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C3315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7604bd40-4462-4086-8763-a50da51a869c']]">
         <assert id="EHF-ESPD-CT-C3401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.RISK_INDEMNITY_INSURANCE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.RISK_INDEMNITY_INSURANCE".</assert>
         <assert id="EHF-ESPD-CT-C3402"
                 test="normalize-space(cbc:Name) = 'Professional risk indemnity insurance'"
                 flag="fatal">Element 'Name' MUST have value "Professional risk indemnity insurance".</assert>
         <assert id="EHF-ESPD-CT-C3403"
                 test="normalize-space(cbc:Description) = 'The insured amount in its professional risk indemnity insurance is the following:'"
                 flag="fatal">Element 'Description' MUST have value "The insured amount in its professional risk indemnity insurance is the following:".</assert>
         <assert id="EHF-ESPD-CT-C3404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '42dc8062-974d-4201-91ba-7f2ea90338fd'"
                 flag="fatal">Criterion '7604bd40-4462-4086-8763-a50da51a869c' MUST have RequirementGroup '42dc8062-974d-4201-91ba-7f2ea90338fd' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3422"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '7604bd40-4462-4086-8763-a50da51a869c' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7604bd40-4462-4086-8763-a50da51a869c']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3412"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3414"
                 test="normalize-space(ccv-cbc:Article) = '58(3)'"
                 flag="fatal">Element 'Article' MUST have value "58(3)".</assert>
         <assert id="EHF-ESPD-CT-C3415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'ab0e7f2e-6418-40e2-8870-6713123e41ad']]">
         <assert id="EHF-ESPD-CT-C3501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.OTHER_REQUIREMENTS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.OTHER_REQUIREMENTS".</assert>
         <assert id="EHF-ESPD-CT-C3502"
                 test="normalize-space(cbc:Name) = 'Other economic or financial requirements'"
                 flag="fatal">Element 'Name' MUST have value "Other economic or financial requirements".</assert>
         <assert id="EHF-ESPD-CT-C3503"
                 test="normalize-space(cbc:Description) = 'Concerning the other economic or financial requirements, if any, that may have been specified in the relevant notice or the procurement documents, the economic operator declares that:'"
                 flag="fatal">Element 'Description' MUST have value "Concerning the other economic or financial requirements, if any, that may have been specified in the relevant notice or the procurement documents, the economic operator declares that:".</assert>
         <assert id="EHF-ESPD-CT-C3504"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C3521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion 'ab0e7f2e-6418-40e2-8870-6713123e41ad' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'ab0e7f2e-6418-40e2-8870-6713123e41ad' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'cdd3bb3e-34a5-43d5-b668-2aab86a73822']]">
         <assert id="EHF-ESPD-CT-C3601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.WORKS_PERFORMANCE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.WORKS_PERFORMANCE".</assert>
         <assert id="EHF-ESPD-CT-C3602"
                 test="normalize-space(cbc:Name) = 'For works contracts: performance of works of the specified type'"
                 flag="fatal">Element 'Name' MUST have value "For works contracts: performance of works of the specified type".</assert>
         <assert id="EHF-ESPD-CT-C3603"
                 test="normalize-space(cbc:Description) = 'For public works contracts only: During the reference period, the economic operator has performed the following works of the specified type. Contracting authorities may require up to five years and allow experience dating from more than five years.'"
                 flag="fatal">Element 'Description' MUST have value "For public works contracts only: During the reference period, the economic operator has performed the following works of the specified type. Contracting authorities may require up to five years and allow experience dating from more than five years.".</assert>
         <assert id="EHF-ESPD-CT-C3604" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3622"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C3623"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C3624"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-C3625"
                 test="ccv:RequirementGroup[5] and normalize-space(ccv:RequirementGroup[5][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 5.</assert>
         <assert id="EHF-ESPD-CT-C3626"
                 test="ccv:RequirementGroup[6] and normalize-space(ccv:RequirementGroup[6][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'cdd3bb3e-34a5-43d5-b668-2aab86a73822' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 6.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'cdd3bb3e-34a5-43d5-b668-2aab86a73822']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3611"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3612"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3613"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3614"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C3615"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3a18a175-1863-4b1d-baef-588ce61960ca']]">
         <assert id="EHF-ESPD-CT-C3701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SUPPLIES_DELIVERY_PERFORMANCE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SUPPLIES_DELIVERY_PERFORMANCE".</assert>
         <assert id="EHF-ESPD-CT-C3702"
                 test="normalize-space(cbc:Name) = 'For supply contracts: performance of deliveries of the specified type'"
                 flag="fatal">Element 'Name' MUST have value "For supply contracts: performance of deliveries of the specified type".</assert>
         <assert id="EHF-ESPD-CT-C3703"
                 test="normalize-space(cbc:Description) = 'For public supply contracts only: During the reference period, the economic operator has delivered the following principal deliveries of the type specified. Contracting authorities may require up to three years and allow experience dating from more than three years.'"
                 flag="fatal">Element 'Description' MUST have value "For public supply contracts only: During the reference period, the economic operator has delivered the following principal deliveries of the type specified. Contracting authorities may require up to three years and allow experience dating from more than three years.".</assert>
         <assert id="EHF-ESPD-CT-C3704" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3722"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C3723"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C3724"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-C3725"
                 test="ccv:RequirementGroup[5] and normalize-space(ccv:RequirementGroup[5][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 5.</assert>
         <assert id="EHF-ESPD-CT-C3726"
                 test="ccv:RequirementGroup[6] and normalize-space(ccv:RequirementGroup[6][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '3a18a175-1863-4b1d-baef-588ce61960ca' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 6.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3a18a175-1863-4b1d-baef-588ce61960ca']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3711"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3712"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3713"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3714"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C3715"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '5e506c16-26ab-4e32-bb78-b27f87dc0565']]">
         <assert id="EHF-ESPD-CT-C3801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SERVICES_DELIVERY_PERFORMANCE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.REFERENCES.SERVICES_DELIVERY_PERFORMANCE".</assert>
         <assert id="EHF-ESPD-CT-C3802"
                 test="normalize-space(cbc:Name) = 'For service contracts: performance of services of the specified type'"
                 flag="fatal">Element 'Name' MUST have value "For service contracts: performance of services of the specified type".</assert>
         <assert id="EHF-ESPD-CT-C3803"
                 test="normalize-space(cbc:Description) = 'For public service contracts only: During the reference period, the economic operator has provided the following main services of the type specified. Contracting authorities may require up to three years and allow experience dating from more than three years.'"
                 flag="fatal">Element 'Description' MUST have value "For public service contracts only: During the reference period, the economic operator has provided the following main services of the type specified. Contracting authorities may require up to three years and allow experience dating from more than three years.".</assert>
         <assert id="EHF-ESPD-CT-C3804" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '5e506c16-26ab-4e32-bb78-b27f87dc0565' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3822"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '5e506c16-26ab-4e32-bb78-b27f87dc0565' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C3823"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '96f00020-0a25-402e-b850-2378e83b5695'"
                 flag="fatal">Criterion '5e506c16-26ab-4e32-bb78-b27f87dc0565' MUST have RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C3824"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '5e506c16-26ab-4e32-bb78-b27f87dc0565' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 4.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '5e506c16-26ab-4e32-bb78-b27f87dc0565']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3811"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3812"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3813"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3814"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C3815"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3aaca389-4a7b-406b-a4b9-080845d127e7']]">
         <assert id="EHF-ESPD-CT-C3901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_QUALITY_CONTROL'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_QUALITY_CONTROL".</assert>
         <assert id="EHF-ESPD-CT-C3902"
                 test="normalize-space(cbc:Name) = 'Technicians or technical bodies for quality control'"
                 flag="fatal">Element 'Name' MUST have value "Technicians or technical bodies for quality control".</assert>
         <assert id="EHF-ESPD-CT-C3903"
                 test="normalize-space(cbc:Description) = 'It can call upon the following technicians or technical bodies, especially those responsible for quality control. For technicians or technical bodies not belonging directly to the economic operator''s undertaking but on whose capacities the economic operator relies as set out under Part II, Section C, separate ESPD forms must be filled in.'"
                 flag="fatal">Element 'Description' MUST have value "It can call upon the following technicians or technical bodies, especially those responsible for quality control. For technicians or technical bodies not belonging directly to the economic operator's undertaking but on whose capacities the economic operator relies as set out under Part II, Section C, separate ESPD forms must be filled in.".</assert>
         <assert id="EHF-ESPD-CT-C3904" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C3921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion '3aaca389-4a7b-406b-a4b9-080845d127e7' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C3922"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '3aaca389-4a7b-406b-a4b9-080845d127e7' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '3aaca389-4a7b-406b-a4b9-080845d127e7']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C3911"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3912"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C3913"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C3914"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C3915"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c599c130-b29f-461e-a187-4e16c7d40db7']]">
         <assert id="EHF-ESPD-CT-C4001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_CARRYING_WORKS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.TECHNICIANS_FOR_CARRYING_WORKS".</assert>
         <assert id="EHF-ESPD-CT-C4002"
                 test="normalize-space(cbc:Name) = 'For works contracts: technicians or technical bodies to carry out the work'"
                 flag="fatal">Element 'Name' MUST have value "For works contracts: technicians or technical bodies to carry out the work".</assert>
         <assert id="EHF-ESPD-CT-C4003"
                 test="normalize-space(cbc:Description) = 'In the case of public works contracts, the economic operator will be able to call on the following technicians or technical bodies to carry out the work:'"
                 flag="fatal">Element 'Description' MUST have value "In the case of public works contracts, the economic operator will be able to call on the following technicians or technical bodies to carry out the work:".</assert>
         <assert id="EHF-ESPD-CT-C4004" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion 'c599c130-b29f-461e-a187-4e16c7d40db7' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4022"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'c599c130-b29f-461e-a187-4e16c7d40db7' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c599c130-b29f-461e-a187-4e16c7d40db7']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4011"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4012"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4013"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4014"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4015"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '4bf996d9-439c-40c6-9ab9-980a48cb55a1']]">
         <assert id="EHF-ESPD-CT-C4101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_QUALITY_ENSURING'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_QUALITY_ENSURING".</assert>
         <assert id="EHF-ESPD-CT-C4102"
                 test="normalize-space(cbc:Name) = 'Technical facilities and measures for ensuring quality'"
                 flag="fatal">Element 'Name' MUST have value "Technical facilities and measures for ensuring quality".</assert>
         <assert id="EHF-ESPD-CT-C4103"
                 test="normalize-space(cbc:Description) = 'It uses the following technical facilities and measures for ensuring quality and its study and research facilities are as follows:'"
                 flag="fatal">Element 'Description' MUST have value "It uses the following technical facilities and measures for ensuring quality and its study and research facilities are as follows:".</assert>
         <assert id="EHF-ESPD-CT-C4104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion '4bf996d9-439c-40c6-9ab9-980a48cb55a1' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4122"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '4bf996d9-439c-40c6-9ab9-980a48cb55a1' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '4bf996d9-439c-40c6-9ab9-980a48cb55a1']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4112"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4114"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '90a2e100-44cc-45d3-9970-69d6714f1596']]">
         <assert id="EHF-ESPD-CT-C4201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_STUDY_RESEARCH'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.FACILITIES_FOR_STUDY_RESEARCH".</assert>
         <assert id="EHF-ESPD-CT-C4202"
                 test="normalize-space(cbc:Name) = 'Study and research facilities'"
                 flag="fatal">Element 'Name' MUST have value "Study and research facilities".</assert>
         <assert id="EHF-ESPD-CT-C4203"
                 test="normalize-space(cbc:Description) = 'It uses the following study and research facilities are as follows:'"
                 flag="fatal">Element 'Description' MUST have value "It uses the following study and research facilities are as follows:".</assert>
         <assert id="EHF-ESPD-CT-C4204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion '90a2e100-44cc-45d3-9970-69d6714f1596' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4222"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '90a2e100-44cc-45d3-9970-69d6714f1596' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '90a2e100-44cc-45d3-9970-69d6714f1596']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4212"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4214"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'dc12a151-7fdf-4733-a8f0-30f667292e66']]">
         <assert id="EHF-ESPD-CT-C4301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.SUPPLY_CHAIN_MANAGEMENT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.SUPPLY_CHAIN_MANAGEMENT".</assert>
         <assert id="EHF-ESPD-CT-C4302"
                 test="normalize-space(cbc:Name) = 'Supply chain management'"
                 flag="fatal">Element 'Name' MUST have value "Supply chain management".</assert>
         <assert id="EHF-ESPD-CT-C4303"
                 test="normalize-space(cbc:Description) = 'It will be able to apply the following supply chain management and tracking systems when performing the contract:'"
                 flag="fatal">Element 'Description' MUST have value "It will be able to apply the following supply chain management and tracking systems when performing the contract:".</assert>
         <assert id="EHF-ESPD-CT-C4304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion 'dc12a151-7fdf-4733-a8f0-30f667292e66' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4322"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'dc12a151-7fdf-4733-a8f0-30f667292e66' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'dc12a151-7fdf-4733-a8f0-30f667292e66']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4312"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4314"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c8809aa1-29b6-4f27-ae2f-27e612e394db']]">
         <assert id="EHF-ESPD-CT-C4401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.CHECKS.ALLOWANCE_OF_CHECKS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.CHECKS.ALLOWANCE_OF_CHECKS".</assert>
         <assert id="EHF-ESPD-CT-C4402"
                 test="normalize-space(cbc:Name) = 'Allowance of checks'"
                 flag="fatal">Element 'Name' MUST have value "Allowance of checks".</assert>
         <assert id="EHF-ESPD-CT-C4403"
                 test="normalize-space(cbc:Description) = 'For complex products or services to be supplied or, exceptionally, for products or services which are required for a special purpose: The economic operator will allow checks to be conducted on the production capacities or the technical capacity of the economic operator and, where necessary, on the means of study and research which are available to it and on the quality control measures? The check is to be performed by the contracting authority or, in case the latter consents to this, on its behalf by a competent official body of the country in which the supplier or service provider is established.'"
                 flag="fatal">Element 'Description' MUST have value "For complex products or services to be supplied or, exceptionally, for products or services which are required for a special purpose: The economic operator will allow checks  to be conducted on the production capacities or the technical capacity of the economic operator and, where necessary, on the means of study and research which are available to it and on the quality control measures? The check is to be performed by the contracting authority or, in case the latter consents to this, on its behalf by a competent official body of the country in which the supplier or service provider is established.".</assert>
         <assert id="EHF-ESPD-CT-C4404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd7721546-9106-43a7-8d31-2fe08a862b00'"
                 flag="fatal">Criterion 'c8809aa1-29b6-4f27-ae2f-27e612e394db' MUST have RequirementGroup 'd7721546-9106-43a7-8d31-2fe08a862b00' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'c8809aa1-29b6-4f27-ae2f-27e612e394db']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4412"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4414"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '07301031-2270-41af-8e7e-66fe0c777107']]">
         <assert id="EHF-ESPD-CT-C4501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.PROFESSIONAL_QUALIFICATIONS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.PROFESSIONAL_QUALIFICATIONS".</assert>
         <assert id="EHF-ESPD-CT-C4502"
                 test="normalize-space(cbc:Name) = 'Educational and professional qualifications'"
                 flag="fatal">Element 'Name' MUST have value "Educational and professional qualifications".</assert>
         <assert id="EHF-ESPD-CT-C4503"
                 test="normalize-space(cbc:Description) = 'The following educational and professional qualifications are held by the service provider or the contractor itself, and/or (depending on the requirements set out in the relevant notice or the procurement documents by its managerial staff.'"
                 flag="fatal">Element 'Description' MUST have value "The following educational and professional qualifications are held by the service provider or the contractor itself, and/or (depending on the requirements set out in the relevant notice or the procurement documents by its managerial staff.".</assert>
         <assert id="EHF-ESPD-CT-C4504" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion '07301031-2270-41af-8e7e-66fe0c777107' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '07301031-2270-41af-8e7e-66fe0c777107' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '07301031-2270-41af-8e7e-66fe0c777107']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4511"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4512"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4513"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4514"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4515"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9460457e-b43d-48a9-acd1-615de6ddd33e']]">
         <assert id="EHF-ESPD-CT-C4601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.ENVIRONMENTAL_MANAGEMENT_MEASURES'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.ENVIRONMENTAL_MANAGEMENT_MEASURES".</assert>
         <assert id="EHF-ESPD-CT-C4602"
                 test="normalize-space(cbc:Name) = 'Environmental management measures'"
                 flag="fatal">Element 'Name' MUST have value "Environmental management measures".</assert>
         <assert id="EHF-ESPD-CT-C4603"
                 test="normalize-space(cbc:Description) = 'The economic operator will be able to apply the following environmental management measures when performing the contract:'"
                 flag="fatal">Element 'Description' MUST have value "The economic operator will be able to apply the following environmental management measures when performing the contract:".</assert>
         <assert id="EHF-ESPD-CT-C4604" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion '9460457e-b43d-48a9-acd1-615de6ddd33e' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4622"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '9460457e-b43d-48a9-acd1-615de6ddd33e' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9460457e-b43d-48a9-acd1-615de6ddd33e']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4611"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4612"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4613"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4614"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4615"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '6346959b-e097-4ea1-89cd-d1b4c131ea4d']]">
         <assert id="EHF-ESPD-CT-C4701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.MANAGERIAL_STAFF'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.MANAGERIAL_STAFF".</assert>
         <assert id="EHF-ESPD-CT-C4702"
                 test="normalize-space(cbc:Name) = 'Number of managerial staff'"
                 flag="fatal">Element 'Name' MUST have value "Number of managerial staff".</assert>
         <assert id="EHF-ESPD-CT-C4703"
                 test="normalize-space(cbc:Description) = 'The economic operator’s number of managerial staff for the last three years were as follows:'"
                 flag="fatal">Element 'Description' MUST have value "The economic operator’s number of managerial staff for the last three years were as follows:".</assert>
         <assert id="EHF-ESPD-CT-C4704" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '6346959b-e097-4ea1-89cd-d1b4c131ea4d' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4722"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '6346959b-e097-4ea1-89cd-d1b4c131ea4d' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C4723"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '6346959b-e097-4ea1-89cd-d1b4c131ea4d' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C4724"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '6346959b-e097-4ea1-89cd-d1b4c131ea4d' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 4.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '6346959b-e097-4ea1-89cd-d1b4c131ea4d']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4711"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4712"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4713"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4714"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4715"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '1f49b3f0-d50f-43f6-8b30-4bafab108b9b']]">
         <assert id="EHF-ESPD-CT-C4801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.AVERAGE_ANNUAL_MANPOWER'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.AVERAGE_ANNUAL_MANPOWER".</assert>
         <assert id="EHF-ESPD-CT-C4802"
                 test="normalize-space(cbc:Name) = 'Average annual manpower'"
                 flag="fatal">Element 'Name' MUST have value "Average annual manpower".</assert>
         <assert id="EHF-ESPD-CT-C4803"
                 test="normalize-space(cbc:Description) = 'The economic operator’s average annual manpower for the last three years were as follows:'"
                 flag="fatal">Element 'Description' MUST have value "The economic operator’s average annual manpower for the last three years were as follows:".</assert>
         <assert id="EHF-ESPD-CT-C4804" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '1f49b3f0-d50f-43f6-8b30-4bafab108b9b' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4822"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '1f49b3f0-d50f-43f6-8b30-4bafab108b9b' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-C4823"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = '96defecc-7d32-4957-82e9-aad5f3c5b736'"
                 flag="fatal">Criterion '1f49b3f0-d50f-43f6-8b30-4bafab108b9b' MUST have RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-C4824"
                 test="ccv:RequirementGroup[4] and normalize-space(ccv:RequirementGroup[4][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '1f49b3f0-d50f-43f6-8b30-4bafab108b9b' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 4.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '1f49b3f0-d50f-43f6-8b30-4bafab108b9b']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4811"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4812"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4813"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4814"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4815"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'cc18c023-211d-484d-a32e-52f3f970285f']]">
         <assert id="EHF-ESPD-CT-C4901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.EQUIPMENT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.TECHNICAL.EQUIPMENT".</assert>
         <assert id="EHF-ESPD-CT-C4902"
                 test="normalize-space(cbc:Name) = 'Tools, plant or technical equipment'"
                 flag="fatal">Element 'Name' MUST have value "Tools, plant or technical equipment".</assert>
         <assert id="EHF-ESPD-CT-C4903"
                 test="normalize-space(cbc:Description) = 'The following tools, plant or technical equipment will be available to it for performing the contract:'"
                 flag="fatal">Element 'Description' MUST have value "The following tools, plant or technical equipment will be available to it for performing the contract:".</assert>
         <assert id="EHF-ESPD-CT-C4904" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C4921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb'"
                 flag="fatal">Criterion 'cc18c023-211d-484d-a32e-52f3f970285f' MUST have RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C4922"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'cc18c023-211d-484d-a32e-52f3f970285f' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'cc18c023-211d-484d-a32e-52f3f970285f']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C4911"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4912"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C4913"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C4914"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C4915"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '612a1625-118d-4ea4-a6db-413184e7c0a8']]">
         <assert id="EHF-ESPD-CT-C5001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.SUBCONTRACTING_PROPORTION'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.SUBCONTRACTING_PROPORTION".</assert>
         <assert id="EHF-ESPD-CT-C5002"
                 test="normalize-space(cbc:Name) = 'Subcontracting proportion'"
                 flag="fatal">Element 'Name' MUST have value "Subcontracting proportion".</assert>
         <assert id="EHF-ESPD-CT-C5003"
                 test="normalize-space(cbc:Description) = 'The economic operator intends possibly to subcontract the following proportion (i.e. percentage) of the contract. Please note that if the economic operator has decided to subcontract a part of the contract and relies on the subcontractor’s capacities to perform that part, then please fill in a separate ESPD for such subcontractors, see Part II, Section C above.'"
                 flag="fatal">Element 'Description' MUST have value "The economic operator intends possibly to subcontract  the following proportion (i.e. percentage) of the contract. Please note that if the economic operator has decided to subcontract a part of the contract and relies on the subcontractor’s capacities to perform that part, then please fill in a separate ESPD for such subcontractors, see Part II, Section C above.".</assert>
         <assert id="EHF-ESPD-CT-C5004" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '575f7550-8a2d-4bad-b9d8-be07ab570076'"
                 flag="fatal">Criterion '612a1625-118d-4ea4-a6db-413184e7c0a8' MUST have RequirementGroup '575f7550-8a2d-4bad-b9d8-be07ab570076' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '612a1625-118d-4ea4-a6db-413184e7c0a8']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5011"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5012"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5013"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5014"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C5015"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'bdf0601d-2480-4250-b870-658d0ee95be6']]">
         <assert id="EHF-ESPD-CT-C5101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.NO_AUTHENTICATED_ARTEFACTS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.NO_AUTHENTICATED_ARTEFACTS".</assert>
         <assert id="EHF-ESPD-CT-C5102"
                 test="normalize-space(cbc:Name) = 'For supply contracts: samples, descriptions or photographs without certification of authenticity'"
                 flag="fatal">Element 'Name' MUST have value "For supply contracts: samples, descriptions or photographs without certification of authenticity".</assert>
         <assert id="EHF-ESPD-CT-C5103"
                 test="normalize-space(cbc:Description) = 'For public supply contracts: The economic operator will supply the required samples, descriptions or photographs of the products to be supplied, which do not need to be accompanied by certifications of authenticity.'"
                 flag="fatal">Element 'Description' MUST have value "For public supply contracts: The economic operator will supply the required samples, descriptions or photographs of the products to be supplied, which do not need to be accompanied by certifications of authenticity.".</assert>
         <assert id="EHF-ESPD-CT-C5104" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e'"
                 flag="fatal">Criterion 'bdf0601d-2480-4250-b870-658d0ee95be6' MUST have RequirementGroup 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C5122"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'bdf0601d-2480-4250-b870-658d0ee95be6' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'bdf0601d-2480-4250-b870-658d0ee95be6']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5111"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5112"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5113"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5114"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C5115"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7662b7a9-bcb8-4763-a0a7-7505d8e8470d']]">
         <assert id="EHF-ESPD-CT-C5201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.AUTHENTICATED_ARTEFACTS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.MANAGEMENT.ARTEFACTS.AUTHENTICATED_ARTEFACTS".</assert>
         <assert id="EHF-ESPD-CT-C5202"
                 test="normalize-space(cbc:Name) = 'For supply contracts: samples, descriptions or photographs with certification of authenticity'"
                 flag="fatal">Element 'Name' MUST have value "For supply contracts: samples, descriptions or photographs with certification of authenticity".</assert>
         <assert id="EHF-ESPD-CT-C5203"
                 test="normalize-space(cbc:Description) = 'For public supply contracts: The economic operator will supply the required samples, descriptions or photographs of the products to be supplied and will provide certifications of authenticity where applicable.'"
                 flag="fatal">Element 'Description' MUST have value "For public supply contracts: The economic operator will supply the required samples, descriptions or photographs of the products to be supplied and will provide certifications of authenticity where applicable.".</assert>
         <assert id="EHF-ESPD-CT-C5204" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e'"
                 flag="fatal">Criterion '7662b7a9-bcb8-4763-a0a7-7505d8e8470d' MUST have RequirementGroup 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C5222"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '7662b7a9-bcb8-4763-a0a7-7505d8e8470d' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '7662b7a9-bcb8-4763-a0a7-7505d8e8470d']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5211"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5212"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5213"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5214"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C5215"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a7669d7d-9297-43e1-9d10-691a1660187c']]">
         <assert id="EHF-ESPD-CT-C5301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INSTITUTES_CERTIFICATE".</assert>
         <assert id="EHF-ESPD-CT-C5302"
                 test="normalize-space(cbc:Name) = 'For supply contracts: certificates by quality control institutes'"
                 flag="fatal">Element 'Name' MUST have value "For supply contracts: certificates by quality control institutes".</assert>
         <assert id="EHF-ESPD-CT-C5303"
                 test="normalize-space(cbc:Description) = 'Can the economic operator provide the required certificates drawn up by official quality control institutes or agencies of recognised competence attesting the conformity of products clearly identified by references to the technical specifications or standards, which are set out in the relevant notice or the procurement documents?'"
                 flag="fatal">Element 'Description' MUST have value "Can the economic operator provide the required certificates drawn up by official quality control institutes or agencies of recognised competence attesting the conformity of products clearly identified by references to the technical specifications or standards, which are set out in the relevant notice or the procurement documents?".</assert>
         <assert id="EHF-ESPD-CT-C5304" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '4887c3d7-05fc-4e3e-b066-f338910f0c4c'"
                 flag="fatal">Criterion 'a7669d7d-9297-43e1-9d10-691a1660187c' MUST have RequirementGroup '4887c3d7-05fc-4e3e-b066-f338910f0c4c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C5322"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'a7669d7d-9297-43e1-9d10-691a1660187c' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'a7669d7d-9297-43e1-9d10-691a1660187c']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5311"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5312"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5313"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5314"
                 test="normalize-space(ccv-cbc:Article) = '58(4)'"
                 flag="fatal">Element 'Article' MUST have value "58(4)".</assert>
         <assert id="EHF-ESPD-CT-C5315"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd726bac9-e153-4e75-bfca-c5385587766d']]">
         <assert id="EHF-ESPD-CT-C5401"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.QUALITY_ASSURANCE.QA_INDEPENDENT_CERTIFICATE".</assert>
         <assert id="EHF-ESPD-CT-C5402"
                 test="normalize-space(cbc:Name) = 'Certificates by independent bodies about arderquality assurance standards'"
                 flag="fatal">Element 'Name' MUST have value "Certificates by independent bodies about arderquality assurance standards".</assert>
         <assert id="EHF-ESPD-CT-C5403"
                 test="normalize-space(cbc:Description) = 'Will the economic operator be able to produce certificates drawn up by independent bodies attesting that the economic operator complies with the required quality assurance standards, including accessibility for disabled persons?'"
                 flag="fatal">Element 'Description' MUST have value "Will the economic operator be able to produce certificates drawn up by independent bodies attesting that the economic operator complies with the required quality assurance standards, including accessibility for disabled persons?".</assert>
         <assert id="EHF-ESPD-CT-C5404" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '0e88f63c-5642-4a17-833b-ae5800e1750a'"
                 flag="fatal">Criterion 'd726bac9-e153-4e75-bfca-c5385587766d' MUST have RequirementGroup '0e88f63c-5642-4a17-833b-ae5800e1750a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C5422"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion 'd726bac9-e153-4e75-bfca-c5385587766d' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'd726bac9-e153-4e75-bfca-c5385587766d']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5411"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5412"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5413"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5414"
                 test="normalize-space(ccv-cbc:Article) = '62(2)'"
                 flag="fatal">Element 'Article' MUST have value "62(2)".</assert>
         <assert id="EHF-ESPD-CT-C5415"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '8ed65e48-fd0d-444f-97bd-4f58da632999']]">
         <assert id="EHF-ESPD-CT-C5501"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.ENVIRONMENTAL_MANAGEMENT.ENV_INDEPENDENT_CERTIFICATE".</assert>
         <assert id="EHF-ESPD-CT-C5502"
                 test="normalize-space(cbc:Name) = 'Certificates by independent bodies about environmental management systems or standards'"
                 flag="fatal">Element 'Name' MUST have value "Certificates by independent bodies about environmental management systems or standards".</assert>
         <assert id="EHF-ESPD-CT-C5503"
                 test="normalize-space(cbc:Description) = 'Will the economic operator be able to produce certificates drawn up by independent bodies attesting that the economic operator complies with the required environmental management systems or standards?'"
                 flag="fatal">Element 'Description' MUST have value "Will the economic operator be able to produce certificates drawn up by independent bodies attesting that the economic operator complies with the required environmental management systems or standards?".</assert>
         <assert id="EHF-ESPD-CT-C5504" test="ccv:LegislationReference" flag="fatal">Element 'LegislationReference' MUST be provided.</assert>
         <assert id="EHF-ESPD-CT-C5521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '82a59ce2-9c59-4075-af08-843ad89a45ec'"
                 flag="fatal">Criterion '8ed65e48-fd0d-444f-97bd-4f58da632999' MUST have RequirementGroup '82a59ce2-9c59-4075-af08-843ad89a45ec' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C5522"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '9026e403-3eb6-4705-a9e9-e21a1efc867d'"
                 flag="fatal">Criterion '8ed65e48-fd0d-444f-97bd-4f58da632999' MUST have RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '8ed65e48-fd0d-444f-97bd-4f58da632999']]/ccv:LegislationReference">
         <assert id="EHF-ESPD-CT-C5511"
                 test="normalize-space(ccv-cbc:Title) = 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Title' MUST have value "DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 on public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5512"
                 test="normalize-space(cbc:Description) = 'On public procurement and repealing Directive 2004/18/EC'"
                 flag="fatal">Element 'Description' MUST have value "On public procurement and repealing Directive 2004/18/EC".</assert>
         <assert id="EHF-ESPD-CT-C5513"
                 test="normalize-space(ccv-cbc:JurisdictionLevelCode) = 'EU_DIRECTIVE'"
                 flag="fatal">Element 'JurisdictionLevelCode' MUST have value "EU_DIRECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C5514"
                 test="normalize-space(ccv-cbc:Article) = '62(2)'"
                 flag="fatal">Element 'Article' MUST have value "62(2)".</assert>
         <assert id="EHF-ESPD-CT-C5515"
                 test="normalize-space(cbc:URI) = 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'"
                 flag="fatal">Element 'URI' MUST have value "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024".</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '2043338f-a38a-490b-b3ec-2607cb25a017']]">
         <assert id="EHF-ESPD-CT-C5601"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP".</assert>
         <assert id="EHF-ESPD-CT-C5602"
                 test="normalize-space(cbc:Name) = 'Procurement reserved'"
                 flag="fatal">Element 'Name' MUST have value "Procurement reserved".</assert>
         <assert id="EHF-ESPD-CT-C5603"
                 test="normalize-space(cbc:Description) = 'Only in case the procurement is reserved: is the economic operator a sheltered workshop, a ''social business'' or will it provide for the performance of the contract in the context of sheltered employment programmes?'"
                 flag="fatal">Element 'Description' MUST have value "Only in case the procurement is reserved: is the economic operator a sheltered workshop, a 'social business' or will it provide for the performance of the contract in the context of sheltered employment programmes?".</assert>
         <assert id="EHF-ESPD-CT-C5604"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C5621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '6febbe4a-e715-427c-a2b1-19cfabadaef0'"
                 flag="fatal">Criterion '2043338f-a38a-490b-b3ec-2607cb25a017' MUST have RequirementGroup '6febbe4a-e715-427c-a2b1-19cfabadaef0' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9b19e869-6c89-4cc4-bd6c-ac9ca8602165']]">
         <assert id="EHF-ESPD-CT-C5701"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST".</assert>
         <assert id="EHF-ESPD-CT-C5702"
                 test="normalize-space(cbc:Name) = 'EO registered'"
                 flag="fatal">Element 'Name' MUST have value "EO registered".</assert>
         <assert id="EHF-ESPD-CT-C5703"
                 test="normalize-space(cbc:Description) = 'If applicable, is the economic operator registered on an official list of approved economic operators or does it have an equivalent certificate (e.g. under a national (pre)qualification system)?'"
                 flag="fatal">Element 'Description' MUST have value "If applicable, is the economic operator registered on an official list of approved economic operators or does it have an equivalent certificate (e.g. under a national (pre)qualification system)?".</assert>
         <assert id="EHF-ESPD-CT-C5704"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C5721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '64162276-7014-408f-a9af-080426bfe1fd'"
                 flag="fatal">Criterion '9b19e869-6c89-4cc4-bd6c-ac9ca8602165' MUST have RequirementGroup '64162276-7014-408f-a9af-080426bfe1fd' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'ee51100f-8e3e-40c9-8f8b-57d5a15be1f2']]">
         <assert id="EHF-ESPD-CT-C5801"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS".</assert>
         <assert id="EHF-ESPD-CT-C5802"
                 test="normalize-space(cbc:Name) = 'EO participating in procurement procedure'"
                 flag="fatal">Element 'Name' MUST have value "EO participating in procurement procedure".</assert>
         <assert id="EHF-ESPD-CT-C5803"
                 test="normalize-space(cbc:Description) = 'Is the economic operator participating in the procurement procedure together with others?'"
                 flag="fatal">Element 'Description' MUST have value "Is the economic operator participating in the procurement procedure together with others?".</assert>
         <assert id="EHF-ESPD-CT-C5804"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C5821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd939f2c6-ba25-4dc4-889c-11d1853add19'"
                 flag="fatal">Criterion 'ee51100f-8e3e-40c9-8f8b-57d5a15be1f2' MUST have RequirementGroup 'd939f2c6-ba25-4dc4-889c-11d1853add19' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '0d62c6ed-f074-4fcf-8e9f-f691351d52ad']]">
         <assert id="EHF-ESPD-CT-C5901"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES".</assert>
         <assert id="EHF-ESPD-CT-C5902"
                 test="normalize-space(cbc:Name) = 'EO relies capacities'"
                 flag="fatal">Element 'Name' MUST have value "EO relies capacities".</assert>
         <assert id="EHF-ESPD-CT-C5903"
                 test="normalize-space(cbc:Description) = 'Does the economic operator rely on the capacities of other entities in order to meet the selection criteria set out under Part IV and the criteria and rules (if any) set out under Part V below?'"
                 flag="fatal">Element 'Description' MUST have value "Does the economic operator rely on the capacities of other entities in order to meet the selection criteria set out under Part IV and the criteria and rules (if any) set out under Part V below?".</assert>
         <assert id="EHF-ESPD-CT-C5904"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C5921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'e688f7d6-dcef-4726-bc61-052e63ead60f'"
                 flag="fatal">Criterion '0d62c6ed-f074-4fcf-8e9f-f691351d52ad' MUST have RequirementGroup 'e688f7d6-dcef-4726-bc61-052e63ead60f' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '72c0c4b1-ca50-4667-9487-461f3eed4ed7']]">
         <assert id="EHF-ESPD-CT-C6001"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES".</assert>
         <assert id="EHF-ESPD-CT-C6002"
                 test="normalize-space(cbc:Name) = 'Subcontracting third parties'"
                 flag="fatal">Element 'Name' MUST have value "Subcontracting third parties".</assert>
         <assert id="EHF-ESPD-CT-C6003"
                 test="normalize-space(cbc:Description) = 'Does the economic operator intend to subcontract any share of the contract to third parties?'"
                 flag="fatal">Element 'Description' MUST have value "Does the economic operator intend to subcontract any share of the contract to third parties?".</assert>
         <assert id="EHF-ESPD-CT-C6004"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C6021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd5fe5a71-7fd3-4910-b6f4-5cd2a4d23524'"
                 flag="fatal">Criterion '72c0c4b1-ca50-4667-9487-461f3eed4ed7' MUST have RequirementGroup 'd5fe5a71-7fd3-4910-b6f4-5cd2a4d23524' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '9c70375e-1264-407e-8b50-b9736bc08901']]">
         <assert id="EHF-ESPD-CT-C6101"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE".</assert>
         <assert id="EHF-ESPD-CT-C6102"
                 test="normalize-space(cbc:Name) = 'It meets the objective and non discriminatory criteria or rules to be applied in order to limit the number of candidates in the following way: In case certain certificates or other forms of documentary evidence are required, please indicate for each whether the economic operator has the required documents:'"
                 flag="fatal">Element 'Name' MUST have value "It meets the objective and non discriminatory criteria or rules to be applied in order to limit the number of candidates in the following way: In case certain certificates or other forms of documentary evidence are required, please indicate for each whether the economic operator has the required documents:".</assert>
         <assert id="EHF-ESPD-CT-C6103"
                 test="normalize-space(cbc:Description) = 'If some of these certificates or forms of documentary evidence are available electronically, please indicate for each:'"
                 flag="fatal">Element 'Description' MUST have value "If some of these certificates or forms of documentary evidence are available electronically, please indicate for each:".</assert>
         <assert id="EHF-ESPD-CT-C6104"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C6121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '3e5c2859-68a7-4312-92e4-01ae79c00cb8'"
                 flag="fatal">Criterion '9c70375e-1264-407e-8b50-b9736bc08901' MUST have RequirementGroup '3e5c2859-68a7-4312-92e4-01ae79c00cb8' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-C6122"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = 'ab335516-73a4-41f7-977b-a98c13a51060'"
                 flag="fatal">Criterion '9c70375e-1264-407e-8b50-b9736bc08901' MUST have RequirementGroup 'ab335516-73a4-41f7-977b-a98c13a51060' as child number 2.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = 'aef56865-3cdc-42a8-87ed-7e5e93088279']]">
         <assert id="EHF-ESPD-CT-C6201"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.NATIONAL.NO.ACCEPTED.FINE'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.NATIONAL.NO.ACCEPTED.FINE".</assert>
         <assert id="EHF-ESPD-CT-C6202"
                 test="normalize-space(cbc:Name) = 'Accepted fine for criminal behaviour'"
                 flag="fatal">Element 'Name' MUST have value "Accepted fine for criminal behaviour".</assert>
         <assert id="EHF-ESPD-CT-C6203"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein accepted a fine for the circumstances listed above, within the meaning of Regulations Regarding Public Procurement FOR-2016-08-12-974 § 24-2 (2) a-f?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein accepted a fine for the circumstances listed above, within the meaning of Regulations Regarding Public Procurement FOR-2016-08-12-974 § 24-2 (2) a-f?".</assert>
         <assert id="EHF-ESPD-CT-C6204"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C6221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '30450436-f559-4dfa-98ba-f0842ed9d2a0'"
                 flag="fatal">Criterion 'aef56865-3cdc-42a8-87ed-7e5e93088279' MUST have RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion[cbc:ID[text() = '588e9cbb-aaaf-48de-b5df-46254e5d2742']]">
         <assert id="EHF-ESPD-CT-C6301"
                 test="normalize-space(cbc:TypeCode) = 'CRITERION.EXCLUSION.NATIONAL.GRAVE.MISCONDUCT'"
                 flag="fatal">Element 'TypeCode' MUST have value "CRITERION.EXCLUSION.NATIONAL.GRAVE.MISCONDUCT".</assert>
         <assert id="EHF-ESPD-CT-C6302"
                 test="normalize-space(cbc:Name) = 'Grave misconduct'"
                 flag="fatal">Element 'Name' MUST have value "Grave misconduct".</assert>
         <assert id="EHF-ESPD-CT-C6303"
                 test="normalize-space(cbc:Description) = 'Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein commited significant breaches of his professonial integrety, within the meaning of Regulations Regarding Public Procurement FOR-2016-08-12-974 § 24-2 (3) i?'"
                 flag="fatal">Element 'Description' MUST have value "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein commited significant breaches of his professonial integrety, within the meaning of Regulations Regarding Public Procurement FOR-2016-08-12-974 § 24-2 (3) i?".</assert>
         <assert id="EHF-ESPD-CT-C6304"
                 test="not(ccv:LegislationReference)"
                 flag="fatal">Element 'LegislationReference' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-C6321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '30450436-f559-4dfa-98ba-f0842ed9d2a0'"
                 flag="fatal">Criterion '588e9cbb-aaaf-48de-b5df-46254e5d2742' MUST have RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' as child number 1.</assert>
      </rule>
      <rule context="ccv:Criterion">
         <assert id="EHF-ESPD-CT-C0001" test="false()" flag="fatal">Invalid criterion.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '7c637c0c-7703-4389-ba52-02997a055bd7']]">
         <assert id="EHF-ESPD-CT-RG0101" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG0102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0103"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'f5276600-a2b6-4ff6-a90e-b31fe19dae41'"
                 flag="fatal">RequirementGroup '7c637c0c-7703-4389-ba52-02997a055bd7' MUST have RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'f5276600-a2b6-4ff6-a90e-b31fe19dae41']]">
         <assert id="EHF-ESPD-CT-RG0201"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG0202"
                 test="count(ccv:Requirement) = 4"
                 flag="fatal">Requirement group MUST contain 4 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG0203"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'ecf40999-7b64-4e10-b960-7f8ff8674cf6'"
                 flag="fatal">RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' MUST have Requirement 'ecf40999-7b64-4e10-b960-7f8ff8674cf6' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0212"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '7d35fb7c-da5b-4830-b598-4f347a04dceb'"
                 flag="fatal">RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' MUST have Requirement '7d35fb7c-da5b-4830-b598-4f347a04dceb' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG0213"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = 'c5012430-14da-454c-9d01-34cedc6a7ded'"
                 flag="fatal">RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' MUST have Requirement 'c5012430-14da-454c-9d01-34cedc6a7ded' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-RG0214"
                 test="ccv:Requirement[4] and normalize-space(ccv:Requirement[4][current()]/cbc:ID/text()) = '9ca9096f-edd2-4f19-b6b1-b55c83a2d5c8'"
                 flag="fatal">RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' MUST have Requirement '9ca9096f-edd2-4f19-b6b1-b55c83a2d5c8' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-RG0221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '5f9f09f7-f701-432c-9fdc-c22c124a74c9'"
                 flag="fatal">RequirementGroup 'f5276600-a2b6-4ff6-a90e-b31fe19dae41' MUST have RequirementGroup '5f9f09f7-f701-432c-9fdc-c22c124a74c9' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '5f9f09f7-f701-432c-9fdc-c22c124a74c9']]">
         <assert id="EHF-ESPD-CT-RG0301" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG0302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0303"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '20c5361b-7599-4ee6-b030-7f8323174d1e'"
                 flag="fatal">RequirementGroup '5f9f09f7-f701-432c-9fdc-c22c124a74c9' MUST have Requirement '20c5361b-7599-4ee6-b030-7f8323174d1e' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '74e6c7b4-757b-4b40-ada6-fad6a997c310'"
                 flag="fatal">RequirementGroup '5f9f09f7-f701-432c-9fdc-c22c124a74c9' MUST have RequirementGroup '74e6c7b4-757b-4b40-ada6-fad6a997c310' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '74e6c7b4-757b-4b40-ada6-fad6a997c310']]">
         <assert id="EHF-ESPD-CT-RG0401"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG0402"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0403"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG0411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7b07904f-e080-401a-a3a1-9a3efeeda54b'"
                 flag="fatal">RequirementGroup '74e6c7b4-757b-4b40-ada6-fad6a997c310' MUST have Requirement '7b07904f-e080-401a-a3a1-9a3efeeda54b' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '7458d42a-e581-4640-9283-34ceb3ad4345']]">
         <assert id="EHF-ESPD-CT-RG0501" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG0502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0503"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'c1347b74-1872-4060-a6db-f4044edcd7c4'"
                 flag="fatal">RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' MUST have Requirement 'c1347b74-1872-4060-a6db-f4044edcd7c4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b'"
                 flag="fatal">RequirementGroup '7458d42a-e581-4640-9283-34ceb3ad4345' MUST have RequirementGroup '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b']]">
         <assert id="EHF-ESPD-CT-RG0601"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG0602"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG0603"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG0611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'f4313bb6-21b6-499e-bdff-debe10e11d2c'"
                 flag="fatal">RequirementGroup '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b' MUST have Requirement 'f4313bb6-21b6-499e-bdff-debe10e11d2c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0612"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '1f1cd18e-3e01-4ca2-af4c-e2981924ba8d'"
                 flag="fatal">RequirementGroup '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b' MUST have Requirement '1f1cd18e-3e01-4ca2-af4c-e2981924ba8d' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG0613"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = 'c3ccfa31-0c5e-4e3a-a3fd-db9fb83d78d4'"
                 flag="fatal">RequirementGroup '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b' MUST have Requirement 'c3ccfa31-0c5e-4e3a-a3fd-db9fb83d78d4' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '098fd3cc-466e-4233-af1a-affe09471bce']]">
         <assert id="EHF-ESPD-CT-RG0701" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG0702"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0703"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '098fd3cc-466e-4233-af1a-affe09471bce' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'f8499787-f9f8-4355-95e2-9784426f4d7b'"
                 flag="fatal">RequirementGroup '098fd3cc-466e-4233-af1a-affe09471bce' MUST have RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'f8499787-f9f8-4355-95e2-9784426f4d7b']]">
         <assert id="EHF-ESPD-CT-RG0801"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG0802"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG0803"
                 test="count(ccv:RequirementGroup) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG0811"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '6c87d3d4-e8eb-4253-b385-6373020ab886'"
                 flag="fatal">RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' MUST have Requirement '6c87d3d4-e8eb-4253-b385-6373020ab886' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0812"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '9052cc59-cfe5-41c6-a314-02a7f378ffe8'"
                 flag="fatal">RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' MUST have Requirement '9052cc59-cfe5-41c6-a314-02a7f378ffe8' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG0821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7c2aec9f-4876-4c33-89e6-2ab6d6cf5d02'"
                 flag="fatal">RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' MUST have RequirementGroup '7c2aec9f-4876-4c33-89e6-2ab6d6cf5d02' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0822"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = 'c882afa4-6971-4b00-8970-0c283eb122cc'"
                 flag="fatal">RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' MUST have RequirementGroup 'c882afa4-6971-4b00-8970-0c283eb122cc' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG0823"
                 test="ccv:RequirementGroup[3] and normalize-space(ccv:RequirementGroup[3][current()]/cbc:ID/text()) = 'fc57e473-d63e-4a04-b589-dcf81cab8052'"
                 flag="fatal">RequirementGroup 'f8499787-f9f8-4355-95e2-9784426f4d7b' MUST have RequirementGroup 'fc57e473-d63e-4a04-b589-dcf81cab8052' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '7c2aec9f-4876-4c33-89e6-2ab6d6cf5d02']]">
         <assert id="EHF-ESPD-CT-RG0901" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG0902"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG0903"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG0911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '9b4497e6-a166-46f9-8581-7fc39ff975c4'"
                 flag="fatal">RequirementGroup '7c2aec9f-4876-4c33-89e6-2ab6d6cf5d02' MUST have Requirement '9b4497e6-a166-46f9-8581-7fc39ff975c4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG0921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '3cb7abf1-662a-4756-b61c-7bc716c1fafc'"
                 flag="fatal">RequirementGroup '7c2aec9f-4876-4c33-89e6-2ab6d6cf5d02' MUST have RequirementGroup '3cb7abf1-662a-4756-b61c-7bc716c1fafc' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '3cb7abf1-662a-4756-b61c-7bc716c1fafc']]">
         <assert id="EHF-ESPD-CT-RG1001"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG1002"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1003"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG1011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '201f11c3-1fa2-4464-acc0-f021266fd881'"
                 flag="fatal">RequirementGroup '3cb7abf1-662a-4756-b61c-7bc716c1fafc' MUST have Requirement '201f11c3-1fa2-4464-acc0-f021266fd881' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'c882afa4-6971-4b00-8970-0c283eb122cc']]">
         <assert id="EHF-ESPD-CT-RG1101" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG1102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1103"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '08b0c984-c5e6-4143-8493-868c39745637'"
                 flag="fatal">RequirementGroup 'c882afa4-6971-4b00-8970-0c283eb122cc' MUST have Requirement '08b0c984-c5e6-4143-8493-868c39745637' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '815422d6-f8a1-418a-8bf0-3524f7c8f721'"
                 flag="fatal">RequirementGroup 'c882afa4-6971-4b00-8970-0c283eb122cc' MUST have RequirementGroup '815422d6-f8a1-418a-8bf0-3524f7c8f721' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '815422d6-f8a1-418a-8bf0-3524f7c8f721']]">
         <assert id="EHF-ESPD-CT-RG1201"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG1202"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG1203"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG1211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'ecf40999-7b64-4e10-b960-7f8ff8674cf6'"
                 flag="fatal">RequirementGroup '815422d6-f8a1-418a-8bf0-3524f7c8f721' MUST have Requirement 'ecf40999-7b64-4e10-b960-7f8ff8674cf6' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1212"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '9ca9096f-edd2-4f19-b6b1-b55c83a2d5c8'"
                 flag="fatal">RequirementGroup '815422d6-f8a1-418a-8bf0-3524f7c8f721' MUST have Requirement '9ca9096f-edd2-4f19-b6b1-b55c83a2d5c8' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'fc57e473-d63e-4a04-b589-dcf81cab8052']]">
         <assert id="EHF-ESPD-CT-RG1301" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG1302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1303"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '70f8697b-8953-411a-a489-4ff62e5250d2'"
                 flag="fatal">RequirementGroup 'fc57e473-d63e-4a04-b589-dcf81cab8052' MUST have Requirement '70f8697b-8953-411a-a489-4ff62e5250d2' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '6c3609e1-9add-4fa9-9409-62ce72ae4548'"
                 flag="fatal">RequirementGroup 'fc57e473-d63e-4a04-b589-dcf81cab8052' MUST have RequirementGroup '6c3609e1-9add-4fa9-9409-62ce72ae4548' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '6c3609e1-9add-4fa9-9409-62ce72ae4548']]">
         <assert id="EHF-ESPD-CT-RG1401"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG1402"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1403"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG1411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '55905dd0-38f0-4f93-8c74-5ae05a21afc5'"
                 flag="fatal">RequirementGroup '6c3609e1-9add-4fa9-9409-62ce72ae4548' MUST have Requirement '55905dd0-38f0-4f93-8c74-5ae05a21afc5' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '976b5acb-c00f-46ca-8f83-5ce6abfdfe43']]">
         <assert id="EHF-ESPD-CT-RG1501" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG1502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1503"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '976b5acb-c00f-46ca-8f83-5ce6abfdfe43' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '64a2102c-4af1-4ecb-97b3-0c41907ec0f6'"
                 flag="fatal">RequirementGroup '976b5acb-c00f-46ca-8f83-5ce6abfdfe43' MUST have RequirementGroup '64a2102c-4af1-4ecb-97b3-0c41907ec0f6' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '64a2102c-4af1-4ecb-97b3-0c41907ec0f6']]">
         <assert id="EHF-ESPD-CT-RG1601"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG1602"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1603"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'e098da8e-4717-4500-965f-f882d5b4e1ad'"
                 flag="fatal">RequirementGroup '64a2102c-4af1-4ecb-97b3-0c41907ec0f6' MUST have Requirement 'e098da8e-4717-4500-965f-f882d5b4e1ad' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '5f9f09f7-f701-432c-9fdc-c22c124a74c9'"
                 flag="fatal">RequirementGroup '64a2102c-4af1-4ecb-97b3-0c41907ec0f6' MUST have RequirementGroup '5f9f09f7-f701-432c-9fdc-c22c124a74c9' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'd91c11a1-f19e-4b83-8ade-c4be2bf00555']]">
         <assert id="EHF-ESPD-CT-RG1701" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG1702"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1703"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'aeef523b-c8fc-4dba-9c34-03e34812567b'"
                 flag="fatal">RequirementGroup 'd91c11a1-f19e-4b83-8ade-c4be2bf00555' MUST have RequirementGroup 'aeef523b-c8fc-4dba-9c34-03e34812567b' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'aeef523b-c8fc-4dba-9c34-03e34812567b']]">
         <assert id="EHF-ESPD-CT-RG1801"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG1802"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG1803"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG1811"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'e098da8e-4717-4500-965f-f882d5b4e1ad'"
                 flag="fatal">RequirementGroup 'aeef523b-c8fc-4dba-9c34-03e34812567b' MUST have Requirement 'e098da8e-4717-4500-965f-f882d5b4e1ad' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1812"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '4e3f468a-86c4-4c99-bd15-c8b221229348'"
                 flag="fatal">RequirementGroup 'aeef523b-c8fc-4dba-9c34-03e34812567b' MUST have Requirement '4e3f468a-86c4-4c99-bd15-c8b221229348' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8']]">
         <assert id="EHF-ESPD-CT-RG1901" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG1902"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG1903"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG1911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG1921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '2cbcf978-765c-40aa-996b-b1d082485cef'"
                 flag="fatal">RequirementGroup '67362ec7-cec3-4cb8-a38e-5d7a2a31e6d8' MUST have RequirementGroup '2cbcf978-765c-40aa-996b-b1d082485cef' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '2cbcf978-765c-40aa-996b-b1d082485cef']]">
         <assert id="EHF-ESPD-CT-RG2001"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG2002"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2003"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG2011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'e098da8e-4717-4500-965f-f882d5b4e1ad'"
                 flag="fatal">RequirementGroup '2cbcf978-765c-40aa-996b-b1d082485cef' MUST have Requirement 'e098da8e-4717-4500-965f-f882d5b4e1ad' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '5f9f09f7-f701-432c-9fdc-c22c124a74c9'"
                 flag="fatal">RequirementGroup '2cbcf978-765c-40aa-996b-b1d082485cef' MUST have RequirementGroup '5f9f09f7-f701-432c-9fdc-c22c124a74c9' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '30450436-f559-4dfa-98ba-f0842ed9d2a0']]">
         <assert id="EHF-ESPD-CT-RG2101" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2103"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG2111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2121"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '73f0fe4c-4ed9-4343-8096-d898cf200146'"
                 flag="fatal">RequirementGroup '30450436-f559-4dfa-98ba-f0842ed9d2a0' MUST have RequirementGroup '73f0fe4c-4ed9-4343-8096-d898cf200146' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '73f0fe4c-4ed9-4343-8096-d898cf200146']]">
         <assert id="EHF-ESPD-CT-RG2201"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG2202"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2203"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG2211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'e098da8e-4717-4500-965f-f882d5b4e1ad'"
                 flag="fatal">RequirementGroup '73f0fe4c-4ed9-4343-8096-d898cf200146' MUST have Requirement 'e098da8e-4717-4500-965f-f882d5b4e1ad' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '77ae3f29-7c5f-4afa-af97-24afec48c5bf']]">
         <assert id="EHF-ESPD-CT-RG2301" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2303"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG2311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a'"
                 flag="fatal">RequirementGroup '77ae3f29-7c5f-4afa-af97-24afec48c5bf' MUST have Requirement '974c8196-9d1c-419c-9ca9-45bb9f5fd59a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '73f0fe4c-4ed9-4343-8096-d898cf200146'"
                 flag="fatal">RequirementGroup '77ae3f29-7c5f-4afa-af97-24afec48c5bf' MUST have RequirementGroup '73f0fe4c-4ed9-4343-8096-d898cf200146' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '1768de86-a6c8-48e4-bd8e-de2f2f7424d0']]">
         <assert id="EHF-ESPD-CT-RG2401" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2402"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2403"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG2411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup '1768de86-a6c8-48e4-bd8e-de2f2f7424d0' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '9026e403-3eb6-4705-a9e9-e21a1efc867d']]">
         <assert id="EHF-ESPD-CT-RG2501" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2503"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG2511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '9dae5670-cb75-4c97-901b-96ddac5a633a'"
                 flag="fatal">RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' MUST have Requirement '9dae5670-cb75-4c97-901b-96ddac5a633a' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2521"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48'"
                 flag="fatal">RequirementGroup '9026e403-3eb6-4705-a9e9-e21a1efc867d' MUST have RequirementGroup '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48']]">
         <assert id="EHF-ESPD-CT-RG2601"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG2602"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG2603"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG2611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '03bb1954-13ae-47d8-8ef8-b7fe0f22d700'"
                 flag="fatal">RequirementGroup '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48' MUST have Requirement '03bb1954-13ae-47d8-8ef8-b7fe0f22d700' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2612"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = 'e2d863a0-60cb-4e58-8c14-4c1595af48b7'"
                 flag="fatal">RequirementGroup '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48' MUST have Requirement 'e2d863a0-60cb-4e58-8c14-4c1595af48b7' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG2613"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = '5cbf74d9-a1e2-4233-921d-8b298842ee7d'"
                 flag="fatal">RequirementGroup '0a166f0a-0c5f-42b0-81e9-0fc9fa598a48' MUST have Requirement '5cbf74d9-a1e2-4233-921d-8b298842ee7d' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'a109e144-f65e-469d-bcda-220f1af34b6c']]">
         <assert id="EHF-ESPD-CT-RG2701" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2702"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2703"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG2711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup 'a109e144-f65e-469d-bcda-220f1af34b6c' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2721"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '7696fb3f-9722-43b8-9b91-ad59bb4b8ad2'"
                 flag="fatal">RequirementGroup 'a109e144-f65e-469d-bcda-220f1af34b6c' MUST have RequirementGroup '7696fb3f-9722-43b8-9b91-ad59bb4b8ad2' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '7696fb3f-9722-43b8-9b91-ad59bb4b8ad2']]">
         <assert id="EHF-ESPD-CT-RG2801"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG2802"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG2803"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG2811"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '51391308-0bf6-423c-95e2-d5a54aa31fb8'"
                 flag="fatal">RequirementGroup '7696fb3f-9722-43b8-9b91-ad59bb4b8ad2' MUST have Requirement '51391308-0bf6-423c-95e2-d5a54aa31fb8' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16']]">
         <assert id="EHF-ESPD-CT-RG2901" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG2902"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG2903"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG2911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '42ec8116-31a7-4118-8612-5b04f5c8bde7'"
                 flag="fatal">RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' MUST have Requirement '42ec8116-31a7-4118-8612-5b04f5c8bde7' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG2912"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '3641b897-f9f0-4d90-909a-b6d4c4b1d645'"
                 flag="fatal">RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' MUST have Requirement '3641b897-f9f0-4d90-909a-b6d4c4b1d645' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG2913"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = '42db0eaa-d2dd-48cb-83ac-38d73cab9b50'"
                 flag="fatal">RequirementGroup 'c0cd9c1c-e90a-4ff9-bce3-ac0fe31abf16' MUST have Requirement '42db0eaa-d2dd-48cb-83ac-38d73cab9b50' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'e1886054-ada4-473c-9afc-2fde82c24cf4']]">
         <assert id="EHF-ESPD-CT-RG3001" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3002"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG3003"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'b98ffd05-6572-4b07-a521-693a1754ed46'"
                 flag="fatal">RequirementGroup 'e1886054-ada4-473c-9afc-2fde82c24cf4' MUST have Requirement 'b98ffd05-6572-4b07-a521-693a1754ed46' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG3012"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '217637ba-6bdb-4c73-a38f-27fe0e71d9be'"
                 flag="fatal">RequirementGroup 'e1886054-ada4-473c-9afc-2fde82c24cf4' MUST have Requirement '217637ba-6bdb-4c73-a38f-27fe0e71d9be' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'e9aa7763-c167-4352-8060-1a3d7d3e2662']]">
         <assert id="EHF-ESPD-CT-RG3101" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3103"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'a18b2c98-8552-45ca-9751-d4c94c05847a'"
                 flag="fatal">RequirementGroup 'e9aa7763-c167-4352-8060-1a3d7d3e2662' MUST have Requirement 'a18b2c98-8552-45ca-9751-d4c94c05847a' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '096686e1-82ca-4de0-8710-d74d90da0f0c']]">
         <assert id="EHF-ESPD-CT-RG3201" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3202"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG3203"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'ab05ff3b-f3e1-4441-9b43-ee9912e29e92'"
                 flag="fatal">RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' MUST have Requirement 'ab05ff3b-f3e1-4441-9b43-ee9912e29e92' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG3212"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '5461b973-7067-457e-93cc-8338da2c3eef'"
                 flag="fatal">RequirementGroup '096686e1-82ca-4de0-8710-d74d90da0f0c' MUST have Requirement '5461b973-7067-457e-93cc-8338da2c3eef' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '42dc8062-974d-4201-91ba-7f2ea90338fd']]">
         <assert id="EHF-ESPD-CT-RG3301" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3303"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '42db0eaa-d2dd-48cb-83ac-38d73cab9b50'"
                 flag="fatal">RequirementGroup '42dc8062-974d-4201-91ba-7f2ea90338fd' MUST have Requirement '42db0eaa-d2dd-48cb-83ac-38d73cab9b50' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '96f00020-0a25-402e-b850-2378e83b5695']]">
         <assert id="EHF-ESPD-CT-RG3401" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3402"
                 test="count(ccv:Requirement) = 5"
                 flag="fatal">Requirement group MUST contain 5 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG3403"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'ab05ff3b-f3e1-4441-9b43-ee9912e29e92'"
                 flag="fatal">RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' MUST have Requirement 'ab05ff3b-f3e1-4441-9b43-ee9912e29e92' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG3412"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '42db0eaa-d2dd-48cb-83ac-38d73cab9b50'"
                 flag="fatal">RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' MUST have Requirement '42db0eaa-d2dd-48cb-83ac-38d73cab9b50' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG3413"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = '42ec8116-31a7-4118-8612-5b04f5c8bde7'"
                 flag="fatal">RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' MUST have Requirement '42ec8116-31a7-4118-8612-5b04f5c8bde7' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-RG3414"
                 test="ccv:Requirement[4] and normalize-space(ccv:Requirement[4][current()]/cbc:ID/text()) = '3641b897-f9f0-4d90-909a-b6d4c4b1d645'"
                 flag="fatal">RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' MUST have Requirement '3641b897-f9f0-4d90-909a-b6d4c4b1d645' as child number 4.</assert>
         <assert id="EHF-ESPD-CT-RG3415"
                 test="ccv:Requirement[5] and normalize-space(ccv:Requirement[5][current()]/cbc:ID/text()) = 'a92536ab-6783-40bb-a037-5d31f421fd85'"
                 flag="fatal">RequirementGroup '96f00020-0a25-402e-b850-2378e83b5695' MUST have Requirement 'a92536ab-6783-40bb-a037-5d31f421fd85' as child number 5.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '488ca189-bcdb-4bf4-80c7-3ad507fd89fb']]">
         <assert id="EHF-ESPD-CT-RG3501" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3503"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '51391308-0bf6-423c-95e2-d5a54aa31fb8'"
                 flag="fatal">RequirementGroup '488ca189-bcdb-4bf4-80c7-3ad507fd89fb' MUST have Requirement '51391308-0bf6-423c-95e2-d5a54aa31fb8' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'd7721546-9106-43a7-8d31-2fe08a862b00']]">
         <assert id="EHF-ESPD-CT-RG3601" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3602"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3603"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '23a27c0e-c4f7-42cd-b0fd-a7cedfbf77a7'"
                 flag="fatal">RequirementGroup 'd7721546-9106-43a7-8d31-2fe08a862b00' MUST have Requirement '23a27c0e-c4f7-42cd-b0fd-a7cedfbf77a7' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '96defecc-7d32-4957-82e9-aad5f3c5b736']]">
         <assert id="EHF-ESPD-CT-RG3701" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3702"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG3703"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '5aacceb3-280e-42f1-b2da-3d8ac7877fe9'"
                 flag="fatal">RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' MUST have Requirement '5aacceb3-280e-42f1-b2da-3d8ac7877fe9' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG3712"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '42037f41-53af-44df-b6b8-2395cee98087'"
                 flag="fatal">RequirementGroup '96defecc-7d32-4957-82e9-aad5f3c5b736' MUST have Requirement '42037f41-53af-44df-b6b8-2395cee98087' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '575f7550-8a2d-4bad-b9d8-be07ab570076']]">
         <assert id="EHF-ESPD-CT-RG3801" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3802"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3803"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3811"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15778db8-0d84-42ba-931b-774c1b3d3f9f'"
                 flag="fatal">RequirementGroup '575f7550-8a2d-4bad-b9d8-be07ab570076' MUST have Requirement '15778db8-0d84-42ba-931b-774c1b3d3f9f' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e']]">
         <assert id="EHF-ESPD-CT-RG3901" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG3902"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG3903"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG3911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup 'cb73544d-e8bb-4cc6-819b-b8e04f1e240e' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '4887c3d7-05fc-4e3e-b066-f338910f0c4c']]">
         <assert id="EHF-ESPD-CT-RG4001" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG4002"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4003"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG4011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup '4887c3d7-05fc-4e3e-b066-f338910f0c4c' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '83f2f79e-0455-4918-89ff-d7829e8bf758'"
                 flag="fatal">RequirementGroup '4887c3d7-05fc-4e3e-b066-f338910f0c4c' MUST have RequirementGroup '83f2f79e-0455-4918-89ff-d7829e8bf758' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '83f2f79e-0455-4918-89ff-d7829e8bf758']]">
         <assert id="EHF-ESPD-CT-RG4101"
                 test="@pi = 'GROUP_FULFILLED.ON_FALSE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_FALSE".</assert>
         <assert id="EHF-ESPD-CT-RG4102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4103"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG4111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'b9dec4cb-2f6f-47d7-a023-e9099b19b338'"
                 flag="fatal">RequirementGroup '83f2f79e-0455-4918-89ff-d7829e8bf758' MUST have Requirement 'b9dec4cb-2f6f-47d7-a023-e9099b19b338' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '0e88f63c-5642-4a17-833b-ae5800e1750a']]">
         <assert id="EHF-ESPD-CT-RG4201" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG4202"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4203"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG4211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup '0e88f63c-5642-4a17-833b-ae5800e1750a' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4221"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '76c7cf31-be3a-4a7e-8c35-a65ae60cd674'"
                 flag="fatal">RequirementGroup '0e88f63c-5642-4a17-833b-ae5800e1750a' MUST have RequirementGroup '76c7cf31-be3a-4a7e-8c35-a65ae60cd674' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '76c7cf31-be3a-4a7e-8c35-a65ae60cd674']]">
         <assert id="EHF-ESPD-CT-RG4301"
                 test="@pi = 'GROUP_FULFILLED.ON_FALSE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_FALSE".</assert>
         <assert id="EHF-ESPD-CT-RG4302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4303"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG4311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '8c5d1e13-54f7-4895-a65c-b8e09253130c'"
                 flag="fatal">RequirementGroup '76c7cf31-be3a-4a7e-8c35-a65ae60cd674' MUST have Requirement '8c5d1e13-54f7-4895-a65c-b8e09253130c' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '82a59ce2-9c59-4075-af08-843ad89a45ec']]">
         <assert id="EHF-ESPD-CT-RG4401" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG4402"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4403"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG4411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '15335c12-ad77-4728-b5ad-3c06a60d65a4'"
                 flag="fatal">RequirementGroup '82a59ce2-9c59-4075-af08-843ad89a45ec' MUST have Requirement '15335c12-ad77-4728-b5ad-3c06a60d65a4' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4421"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'd664788a-df88-49a0-9dfa-2dd217355724'"
                 flag="fatal">RequirementGroup '82a59ce2-9c59-4075-af08-843ad89a45ec' MUST have RequirementGroup 'd664788a-df88-49a0-9dfa-2dd217355724' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'd664788a-df88-49a0-9dfa-2dd217355724']]">
         <assert id="EHF-ESPD-CT-RG4501"
                 test="@pi = 'GROUP_FULFILLED.ON_FALSE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_FALSE".</assert>
         <assert id="EHF-ESPD-CT-RG4502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4503"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG4511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'b0aace10-fd73-46d1-ae78-289ee5cd42ca'"
                 flag="fatal">RequirementGroup 'd664788a-df88-49a0-9dfa-2dd217355724' MUST have Requirement 'b0aace10-fd73-46d1-ae78-289ee5cd42ca' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '6febbe4a-e715-427c-a2b1-19cfabadaef0']]">
         <assert id="EHF-ESPD-CT-RG4601" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG4602"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4603"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG4611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup '6febbe4a-e715-427c-a2b1-19cfabadaef0' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'a5e33369-e2b5-45f7-9969-ddb1c3ae17c8'"
                 flag="fatal">RequirementGroup '6febbe4a-e715-427c-a2b1-19cfabadaef0' MUST have RequirementGroup 'a5e33369-e2b5-45f7-9969-ddb1c3ae17c8' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'a5e33369-e2b5-45f7-9969-ddb1c3ae17c8']]">
         <assert id="EHF-ESPD-CT-RG4701"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG4702"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG4703"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG4711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '4e552658-d532-4770-943b-b90efcc9788d'"
                 flag="fatal">RequirementGroup 'a5e33369-e2b5-45f7-9969-ddb1c3ae17c8' MUST have Requirement '4e552658-d532-4770-943b-b90efcc9788d' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4712"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = 'e01d0929-c7a9-455a-aaf9-e1f7cd966336'"
                 flag="fatal">RequirementGroup 'a5e33369-e2b5-45f7-9969-ddb1c3ae17c8' MUST have Requirement 'e01d0929-c7a9-455a-aaf9-e1f7cd966336' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '64162276-7014-408f-a9af-080426bfe1fd']]">
         <assert id="EHF-ESPD-CT-RG4801" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG4802"
                 test="count(ccv:Requirement) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG4803"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG4821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'ecb5127b-9018-4fb8-8327-a6a7a2c73195'"
                 flag="fatal">RequirementGroup '64162276-7014-408f-a9af-080426bfe1fd' MUST have RequirementGroup 'ecb5127b-9018-4fb8-8327-a6a7a2c73195' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'ecb5127b-9018-4fb8-8327-a6a7a2c73195']]">
         <assert id="EHF-ESPD-CT-RG4901"
                 test="@pi = 'GROUP_FULFILLED.ON_FALSE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_FALSE".</assert>
         <assert id="EHF-ESPD-CT-RG4902"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG4903"
                 test="count(ccv:RequirementGroup) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG4911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup 'ecb5127b-9018-4fb8-8327-a6a7a2c73195' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4921"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'dc4acf0c-c761-40d0-b031-4ee1f224be5c'"
                 flag="fatal">RequirementGroup 'ecb5127b-9018-4fb8-8327-a6a7a2c73195' MUST have RequirementGroup 'dc4acf0c-c761-40d0-b031-4ee1f224be5c' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG4922"
                 test="ccv:RequirementGroup[2] and normalize-space(ccv:RequirementGroup[2][current()]/cbc:ID/text()) = '59e6f3ef-15cd-4e21-82ac-ea497ccd44e2'"
                 flag="fatal">RequirementGroup 'ecb5127b-9018-4fb8-8327-a6a7a2c73195' MUST have RequirementGroup '59e6f3ef-15cd-4e21-82ac-ea497ccd44e2' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'dc4acf0c-c761-40d0-b031-4ee1f224be5c']]">
         <assert id="EHF-ESPD-CT-RG5001"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG5002"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG5003"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG5011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '30064ad3-fc11-4579-8528-fdd0b9a5ba75'"
                 flag="fatal">RequirementGroup 'dc4acf0c-c761-40d0-b031-4ee1f224be5c' MUST have Requirement '30064ad3-fc11-4579-8528-fdd0b9a5ba75' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5012"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = 'b3403349-cbc0-4d84-879e-fc0f2d90ecbd'"
                 flag="fatal">RequirementGroup 'dc4acf0c-c761-40d0-b031-4ee1f224be5c' MUST have Requirement 'b3403349-cbc0-4d84-879e-fc0f2d90ecbd' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG5013"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = '792ff522-6f3f-4a62-ab6e-a8b272bc290e'"
                 flag="fatal">RequirementGroup 'dc4acf0c-c761-40d0-b031-4ee1f224be5c' MUST have Requirement '792ff522-6f3f-4a62-ab6e-a8b272bc290e' as child number 3.</assert>
         <assert id="EHF-ESPD-CT-RG5021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '92e44d3b-af8e-4a29-91a8-24d27aa27fee'"
                 flag="fatal">RequirementGroup 'dc4acf0c-c761-40d0-b031-4ee1f224be5c' MUST have RequirementGroup '92e44d3b-af8e-4a29-91a8-24d27aa27fee' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '92e44d3b-af8e-4a29-91a8-24d27aa27fee']]">
         <assert id="EHF-ESPD-CT-RG5101" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG5102"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5103"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'd9996ef5-49f9-4cf8-a2f5-31c9f4efd894'"
                 flag="fatal">RequirementGroup '92e44d3b-af8e-4a29-91a8-24d27aa27fee' MUST have Requirement 'd9996ef5-49f9-4cf8-a2f5-31c9f4efd894' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '59e6f3ef-15cd-4e21-82ac-ea497ccd44e2']]">
         <assert id="EHF-ESPD-CT-RG5201"
                 test="@pi = 'GROUP_FULFILLED.ON_FALSE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_FALSE".</assert>
         <assert id="EHF-ESPD-CT-RG5202"
                 test="count(ccv:Requirement) = 2"
                 flag="fatal">Requirement group MUST contain 2 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG5203"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5211"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '0e71abd3-198e-49c5-8128-5708617bb191'"
                 flag="fatal">RequirementGroup '59e6f3ef-15cd-4e21-82ac-ea497ccd44e2' MUST have Requirement '0e71abd3-198e-49c5-8128-5708617bb191' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5212"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = 'caa72cea-5443-49fb-84ba-ab6c64427f77'"
                 flag="fatal">RequirementGroup '59e6f3ef-15cd-4e21-82ac-ea497ccd44e2' MUST have Requirement 'caa72cea-5443-49fb-84ba-ab6c64427f77' as child number 2.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'd939f2c6-ba25-4dc4-889c-11d1853add19']]">
         <assert id="EHF-ESPD-CT-RG5301" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG5302"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5303"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG5311"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup 'd939f2c6-ba25-4dc4-889c-11d1853add19' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5321"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'f5663c5a-d311-4ae4-be14-1575754be5f2'"
                 flag="fatal">RequirementGroup 'd939f2c6-ba25-4dc4-889c-11d1853add19' MUST have RequirementGroup 'f5663c5a-d311-4ae4-be14-1575754be5f2' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'f5663c5a-d311-4ae4-be14-1575754be5f2']]">
         <assert id="EHF-ESPD-CT-RG5401"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG5402"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG5403"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5411"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '907fd62b-02f1-452c-81a8-785bedb0c536'"
                 flag="fatal">RequirementGroup 'f5663c5a-d311-4ae4-be14-1575754be5f2' MUST have Requirement '907fd62b-02f1-452c-81a8-785bedb0c536' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5412"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '7c267f95-a3a7-49ef-abd9-e121dcd641a9'"
                 flag="fatal">RequirementGroup 'f5663c5a-d311-4ae4-be14-1575754be5f2' MUST have Requirement '7c267f95-a3a7-49ef-abd9-e121dcd641a9' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG5413"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = '96f38793-4469-4153-aba6-c613282cdbdc'"
                 flag="fatal">RequirementGroup 'f5663c5a-d311-4ae4-be14-1575754be5f2' MUST have Requirement '96f38793-4469-4153-aba6-c613282cdbdc' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'e688f7d6-dcef-4726-bc61-052e63ead60f']]">
         <assert id="EHF-ESPD-CT-RG5501" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG5502"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5503"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5511"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup 'e688f7d6-dcef-4726-bc61-052e63ead60f' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'd5fe5a71-7fd3-4910-b6f4-5cd2a4d23524']]">
         <assert id="EHF-ESPD-CT-RG5601" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG5602"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5603"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG5611"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup 'd5fe5a71-7fd3-4910-b6f4-5cd2a4d23524' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5621"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = 'b638edf6-4f00-4e24-92c4-cf96846f2c17'"
                 flag="fatal">RequirementGroup 'd5fe5a71-7fd3-4910-b6f4-5cd2a4d23524' MUST have RequirementGroup 'b638edf6-4f00-4e24-92c4-cf96846f2c17' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'b638edf6-4f00-4e24-92c4-cf96846f2c17']]">
         <assert id="EHF-ESPD-CT-RG5701"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG5702"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5703"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5711"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '999c7fe2-61cd-4e86-b76f-e280304dc8c9'"
                 flag="fatal">RequirementGroup 'b638edf6-4f00-4e24-92c4-cf96846f2c17' MUST have Requirement '999c7fe2-61cd-4e86-b76f-e280304dc8c9' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '3e5c2859-68a7-4312-92e4-01ae79c00cb8']]">
         <assert id="EHF-ESPD-CT-RG5801" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG5802"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5803"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG5811"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '7f18c64e-ae09-4646-9400-f3666d50af51'"
                 flag="fatal">RequirementGroup '3e5c2859-68a7-4312-92e4-01ae79c00cb8' MUST have Requirement '7f18c64e-ae09-4646-9400-f3666d50af51' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG5821"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '6066950e-3049-4b4e-86e7-2454f1fb3780'"
                 flag="fatal">RequirementGroup '3e5c2859-68a7-4312-92e4-01ae79c00cb8' MUST have RequirementGroup '6066950e-3049-4b4e-86e7-2454f1fb3780' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '6066950e-3049-4b4e-86e7-2454f1fb3780']]">
         <assert id="EHF-ESPD-CT-RG5901"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG5902"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG5903"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG5911"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '323f19b5-3308-4873-b2d1-767963cc81e9'"
                 flag="fatal">RequirementGroup '6066950e-3049-4b4e-86e7-2454f1fb3780' MUST have Requirement '323f19b5-3308-4873-b2d1-767963cc81e9' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = 'ab335516-73a4-41f7-977b-a98c13a51060']]">
         <assert id="EHF-ESPD-CT-RG6001" test="not(@pi)" flag="fatal">Attribute 'pi' MUST NOT be provided.</assert>
         <assert id="EHF-ESPD-CT-RG6002"
                 test="count(ccv:Requirement) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement.</assert>
         <assert id="EHF-ESPD-CT-RG6003"
                 test="count(ccv:RequirementGroup) = 1"
                 flag="fatal">Requirement group MUST contain 1 requirement group.</assert>
         <assert id="EHF-ESPD-CT-RG6011"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = '0622bbd1-7378-45e1-8fb9-25429740ac22'"
                 flag="fatal">RequirementGroup 'ab335516-73a4-41f7-977b-a98c13a51060' MUST have Requirement '0622bbd1-7378-45e1-8fb9-25429740ac22' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG6021"
                 test="ccv:RequirementGroup[1] and normalize-space(ccv:RequirementGroup[1][current()]/cbc:ID/text()) = '8e7e890c-d117-44c8-aa48-cc236d26b475'"
                 flag="fatal">RequirementGroup 'ab335516-73a4-41f7-977b-a98c13a51060' MUST have RequirementGroup '8e7e890c-d117-44c8-aa48-cc236d26b475' as child number 1.</assert>
      </rule>
      <rule context="ccv:RequirementGroup[cbc:ID[text() = '8e7e890c-d117-44c8-aa48-cc236d26b475']]">
         <assert id="EHF-ESPD-CT-RG6101"
                 test="@pi = 'GROUP_FULFILLED.ON_TRUE'"
                 flag="fatal">Attribute 'pi' MUST have value "GROUP_FULFILLED.ON_TRUE".</assert>
         <assert id="EHF-ESPD-CT-RG6102"
                 test="count(ccv:Requirement) = 3"
                 flag="fatal">Requirement group MUST contain 3 requirements.</assert>
         <assert id="EHF-ESPD-CT-RG6103"
                 test="count(ccv:RequirementGroup) = 0"
                 flag="fatal">Requirement group MUST contain 0 requirement groups.</assert>
         <assert id="EHF-ESPD-CT-RG6111"
                 test="ccv:Requirement[1] and normalize-space(ccv:Requirement[1][current()]/cbc:ID/text()) = 'ee1ee1cd-3791-4855-8b8b-28d4f4c5c007'"
                 flag="fatal">RequirementGroup '8e7e890c-d117-44c8-aa48-cc236d26b475' MUST have Requirement 'ee1ee1cd-3791-4855-8b8b-28d4f4c5c007' as child number 1.</assert>
         <assert id="EHF-ESPD-CT-RG6112"
                 test="ccv:Requirement[2] and normalize-space(ccv:Requirement[2][current()]/cbc:ID/text()) = '1e55ff14-c643-4abc-91d7-2f4dfcdf2409'"
                 flag="fatal">RequirementGroup '8e7e890c-d117-44c8-aa48-cc236d26b475' MUST have Requirement '1e55ff14-c643-4abc-91d7-2f4dfcdf2409' as child number 2.</assert>
         <assert id="EHF-ESPD-CT-RG6113"
                 test="ccv:Requirement[3] and normalize-space(ccv:Requirement[3][current()]/cbc:ID/text()) = 'd8e1e818-d67b-4bb9-9aeb-4c10943a8342'"
                 flag="fatal">RequirementGroup '8e7e890c-d117-44c8-aa48-cc236d26b475' MUST have Requirement 'd8e1e818-d67b-4bb9-9aeb-4c10943a8342' as child number 3.</assert>
      </rule>
      <rule context="ccv:RequirementGroup">
         <assert id="EHF-ESPD-CT-RG0001" test="false()" flag="fatal">Invalid requirement group.</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '974c8196-9d1c-419c-9ca9-45bb9f5fd59a']]">
         <assert id="EHF-ESPD-CT-R0101"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R0102"
                 test="normalize-space(cbc:Description) = 'Your answer?'"
                 flag="fatal">Description MUST be "Your answer?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'ecf40999-7b64-4e10-b960-7f8ff8674cf6']]">
         <assert id="EHF-ESPD-CT-R0201"
                 test="@responseDataType = 'DATE'"
                 flag="fatal">Response data type MUST be "DATE".</assert>
         <assert id="EHF-ESPD-CT-R0202"
                 test="normalize-space(cbc:Description) = 'Date of conviction'"
                 flag="fatal">Description MUST be "Date of conviction".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '7d35fb7c-da5b-4830-b598-4f347a04dceb']]">
         <assert id="EHF-ESPD-CT-R0301"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R0302"
                 test="normalize-space(cbc:Description) = 'Reason'"
                 flag="fatal">Description MUST be "Reason".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'c5012430-14da-454c-9d01-34cedc6a7ded']]">
         <assert id="EHF-ESPD-CT-R0401"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R0402"
                 test="normalize-space(cbc:Description) = 'Who has been convicted'"
                 flag="fatal">Description MUST be "Who has been convicted".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '9ca9096f-edd2-4f19-b6b1-b55c83a2d5c8']]">
         <assert id="EHF-ESPD-CT-R0501"
                 test="@responseDataType = 'PERIOD'"
                 flag="fatal">Response data type MUST be "PERIOD".</assert>
         <assert id="EHF-ESPD-CT-R0502"
                 test="normalize-space(cbc:Description) = 'Length of the period of exclusion'"
                 flag="fatal">Description MUST be "Length of the period of exclusion".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '20c5361b-7599-4ee6-b030-7f8323174d1e']]">
         <assert id="EHF-ESPD-CT-R0601"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R0602"
                 test="normalize-space(cbc:Description) = 'Have you taken measures to demonstrate your reliability (&#34;Self-Cleaning&#34;)?'"
                 flag="fatal">Description MUST be "Have you taken measures to demonstrate your reliability ("Self-Cleaning")?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '7b07904f-e080-401a-a3a1-9a3efeeda54b']]">
         <assert id="EHF-ESPD-CT-R0701"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R0702"
                 test="normalize-space(cbc:Description) = 'Please describe them'"
                 flag="fatal">Description MUST be "Please describe them".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'c1347b74-1872-4060-a6db-f4044edcd7c4']]">
         <assert id="EHF-ESPD-CT-R0801"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R0802"
                 test="normalize-space(cbc:Description) = 'Is this information available electronically?'"
                 flag="fatal">Description MUST be "Is this information available electronically?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'f4313bb6-21b6-499e-bdff-debe10e11d2c']]">
         <assert id="EHF-ESPD-CT-R0901"
                 test="@responseDataType = 'EVIDENCE_URL'"
                 flag="fatal">Response data type MUST be "EVIDENCE_URL".</assert>
         <assert id="EHF-ESPD-CT-R0902"
                 test="normalize-space(cbc:Description) = 'URL'"
                 flag="fatal">Description MUST be "URL".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '1f1cd18e-3e01-4ca2-af4c-e2981924ba8d']]">
         <assert id="EHF-ESPD-CT-R1001"
                 test="@responseDataType = 'CODE'"
                 flag="fatal">Response data type MUST be "CODE".</assert>
         <assert id="EHF-ESPD-CT-R1002"
                 test="normalize-space(cbc:Description) = 'Code'"
                 flag="fatal">Description MUST be "Code".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'c3ccfa31-0c5e-4e3a-a3fd-db9fb83d78d4']]">
         <assert id="EHF-ESPD-CT-R1101"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R1102"
                 test="normalize-space(cbc:Description) = 'Issuer'"
                 flag="fatal">Description MUST be "Issuer".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '6c87d3d4-e8eb-4253-b385-6373020ab886']]">
         <assert id="EHF-ESPD-CT-R1201"
                 test="@responseDataType = 'CODE_COUNTRY'"
                 flag="fatal">Response data type MUST be "CODE_COUNTRY".</assert>
         <assert id="EHF-ESPD-CT-R1202"
                 test="normalize-space(cbc:Description) = 'Country or member state concerned'"
                 flag="fatal">Description MUST be "Country or member state concerned".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '9052cc59-cfe5-41c6-a314-02a7f378ffe8']]">
         <assert id="EHF-ESPD-CT-R1301"
                 test="@responseDataType = 'AMOUNT'"
                 flag="fatal">Response data type MUST be "AMOUNT".</assert>
         <assert id="EHF-ESPD-CT-R1302"
                 test="normalize-space(cbc:Description) = 'Amount concerned'"
                 flag="fatal">Description MUST be "Amount concerned".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '9b4497e6-a166-46f9-8581-7fc39ff975c4']]">
         <assert id="EHF-ESPD-CT-R1401"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R1402"
                 test="normalize-space(cbc:Description) = 'Has this breach of obligations been established by means other than a judicial or administrative decision?'"
                 flag="fatal">Description MUST be "Has this breach of obligations been established by means other than a judicial or administrative decision?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '201f11c3-1fa2-4464-acc0-f021266fd881']]">
         <assert id="EHF-ESPD-CT-R1501"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R1502"
                 test="normalize-space(cbc:Description) = 'Please describe which means were used'"
                 flag="fatal">Description MUST be "Please describe which means were used".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '08b0c984-c5e6-4143-8493-868c39745637']]">
         <assert id="EHF-ESPD-CT-R1601"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R1602"
                 test="normalize-space(cbc:Description) = 'If this breach of obligations was established through a judicial or administrative decision, was this decision final and binding?'"
                 flag="fatal">Description MUST be "If this breach of obligations was established through a judicial or administrative decision, was this decision final and binding?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '70f8697b-8953-411a-a489-4ff62e5250d2']]">
         <assert id="EHF-ESPD-CT-R1701"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R1702"
                 test="normalize-space(cbc:Description) = 'Has the economic operator fulfilled its obligations by paying or entering into a binding arrangement with a view to paying the taxes or social security contributions due, including, where applicable, any interest accrued or fines?'"
                 flag="fatal">Description MUST be "Has the economic operator fulfilled its obligations by paying or entering into a binding arrangement with a view to paying the taxes or social security contributions due, including, where applicable, any interest accrued or fines?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '55905dd0-38f0-4f93-8c74-5ae05a21afc5']]">
         <assert id="EHF-ESPD-CT-R1801"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R1802"
                 test="normalize-space(cbc:Description) = 'Please describe them'"
                 flag="fatal">Description MUST be "Please describe them".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'e098da8e-4717-4500-965f-f882d5b4e1ad']]">
         <assert id="EHF-ESPD-CT-R1901"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R1902"
                 test="normalize-space(cbc:Description) = 'Please describe them'"
                 flag="fatal">Description MUST be "Please describe them".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '4e3f468a-86c4-4c99-bd15-c8b221229348']]">
         <assert id="EHF-ESPD-CT-R2001"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R2002"
                 test="normalize-space(cbc:Description) = 'Indicate reasons for being nevertheless to perform the contract'"
                 flag="fatal">Description MUST be "Indicate reasons for being nevertheless to perform the contract".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '15335c12-ad77-4728-b5ad-3c06a60d65a4']]">
         <assert id="EHF-ESPD-CT-R2101"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R2102"
                 test="normalize-space(cbc:Description) = 'Your answer?'"
                 flag="fatal">Description MUST be "Your answer?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '9dae5670-cb75-4c97-901b-96ddac5a633a']]">
         <assert id="EHF-ESPD-CT-R2201"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R2202"
                 test="normalize-space(cbc:Description) = 'Is this information available electronically?'"
                 flag="fatal">Description MUST be "Is this information available electronically?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '03bb1954-13ae-47d8-8ef8-b7fe0f22d700']]">
         <assert id="EHF-ESPD-CT-R2301"
                 test="@responseDataType = 'EVIDENCE_URL'"
                 flag="fatal">Response data type MUST be "EVIDENCE_URL".</assert>
         <assert id="EHF-ESPD-CT-R2302"
                 test="normalize-space(cbc:Description) = 'URL'"
                 flag="fatal">Description MUST be "URL".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'e2d863a0-60cb-4e58-8c14-4c1595af48b7']]">
         <assert id="EHF-ESPD-CT-R2401"
                 test="@responseDataType = 'CODE'"
                 flag="fatal">Response data type MUST be "CODE".</assert>
         <assert id="EHF-ESPD-CT-R2402"
                 test="normalize-space(cbc:Description) = 'Code'"
                 flag="fatal">Description MUST be "Code".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '5cbf74d9-a1e2-4233-921d-8b298842ee7d']]">
         <assert id="EHF-ESPD-CT-R2501"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R2502"
                 test="normalize-space(cbc:Description) = 'Issuer'"
                 flag="fatal">Description MUST be "Issuer".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '51391308-0bf6-423c-95e2-d5a54aa31fb8']]">
         <assert id="EHF-ESPD-CT-R2601"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R2602"
                 test="normalize-space(cbc:Description) = 'Please describe them'"
                 flag="fatal">Description MUST be "Please describe them".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '42ec8116-31a7-4118-8612-5b04f5c8bde7']]">
         <assert id="EHF-ESPD-CT-R2701"
                 test="@responseDataType = 'DATE'"
                 flag="fatal">Response data type MUST be "DATE".</assert>
         <assert id="EHF-ESPD-CT-R2702"
                 test="normalize-space(cbc:Description) = 'Start Date'"
                 flag="fatal">Description MUST be "Start Date".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '3641b897-f9f0-4d90-909a-b6d4c4b1d645']]">
         <assert id="EHF-ESPD-CT-R2801"
                 test="@responseDataType = 'DATE'"
                 flag="fatal">Response data type MUST be "DATE".</assert>
         <assert id="EHF-ESPD-CT-R2802"
                 test="normalize-space(cbc:Description) = 'End Date'"
                 flag="fatal">Description MUST be "End Date".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '42db0eaa-d2dd-48cb-83ac-38d73cab9b50']]">
         <assert id="EHF-ESPD-CT-R2901"
                 test="@responseDataType = 'AMOUNT'"
                 flag="fatal">Response data type MUST be "AMOUNT".</assert>
         <assert id="EHF-ESPD-CT-R2902"
                 test="normalize-space(cbc:Description) = 'Amount'"
                 flag="fatal">Description MUST be "Amount".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'b98ffd05-6572-4b07-a521-693a1754ed46']]">
         <assert id="EHF-ESPD-CT-R3001"
                 test="@responseDataType = 'QUANTITY_INTEGER'"
                 flag="fatal">Response data type MUST be "QUANTITY_INTEGER".</assert>
         <assert id="EHF-ESPD-CT-R3002"
                 test="normalize-space(cbc:Description) = 'Number of years'"
                 flag="fatal">Description MUST be "Number of years".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '217637ba-6bdb-4c73-a38f-27fe0e71d9be']]">
         <assert id="EHF-ESPD-CT-R3101"
                 test="@responseDataType = 'AMOUNT'"
                 flag="fatal">Response data type MUST be "AMOUNT".</assert>
         <assert id="EHF-ESPD-CT-R3102"
                 test="normalize-space(cbc:Description) = 'Average turnover'"
                 flag="fatal">Description MUST be "Average turnover".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'a18b2c98-8552-45ca-9751-d4c94c05847a']]">
         <assert id="EHF-ESPD-CT-R3201"
                 test="@responseDataType = 'QUANTITY_YEAR'"
                 flag="fatal">Response data type MUST be "QUANTITY_YEAR".</assert>
         <assert id="EHF-ESPD-CT-R3202"
                 test="normalize-space(cbc:Description) = 'Please specify'"
                 flag="fatal">Description MUST be "Please specify".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'ab05ff3b-f3e1-4441-9b43-ee9912e29e92']]">
         <assert id="EHF-ESPD-CT-R3301"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R3302"
                 test="normalize-space(cbc:Description) = 'Description'"
                 flag="fatal">Description MUST be "Description".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '5461b973-7067-457e-93cc-8338da2c3eef']]">
         <assert id="EHF-ESPD-CT-R3401"
                 test="@responseDataType = 'QUANTITY'"
                 flag="fatal">Response data type MUST be "QUANTITY".</assert>
         <assert id="EHF-ESPD-CT-R3402"
                 test="normalize-space(cbc:Description) = 'Ratio'"
                 flag="fatal">Description MUST be "Ratio".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'a92536ab-6783-40bb-a037-5d31f421fd85']]">
         <assert id="EHF-ESPD-CT-R3501"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R3502"
                 test="normalize-space(cbc:Description) = 'Recipients'"
                 flag="fatal">Description MUST be "Recipients".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '23a27c0e-c4f7-42cd-b0fd-a7cedfbf77a7']]">
         <assert id="EHF-ESPD-CT-R3601"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R3602"
                 test="normalize-space(cbc:Description) = 'Do you allow checks?'"
                 flag="fatal">Description MUST be "Do you allow checks?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '5aacceb3-280e-42f1-b2da-3d8ac7877fe9']]">
         <assert id="EHF-ESPD-CT-R3701"
                 test="@responseDataType = 'QUANTITY_YEAR'"
                 flag="fatal">Response data type MUST be "QUANTITY_YEAR".</assert>
         <assert id="EHF-ESPD-CT-R3702"
                 test="normalize-space(cbc:Description) = 'Year'"
                 flag="fatal">Description MUST be "Year".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '42037f41-53af-44df-b6b8-2395cee98087']]">
         <assert id="EHF-ESPD-CT-R3801"
                 test="@responseDataType = 'QUANTITY_INTEGER'"
                 flag="fatal">Response data type MUST be "QUANTITY_INTEGER".</assert>
         <assert id="EHF-ESPD-CT-R3802"
                 test="normalize-space(cbc:Description) = 'Number'"
                 flag="fatal">Description MUST be "Number".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '15778db8-0d84-42ba-931b-774c1b3d3f9f']]">
         <assert id="EHF-ESPD-CT-R3901"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R3902"
                 test="normalize-space(cbc:Description) = 'Please specify'"
                 flag="fatal">Description MUST be "Please specify".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'b9dec4cb-2f6f-47d7-a023-e9099b19b338']]">
         <assert id="EHF-ESPD-CT-R4001"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4002"
                 test="normalize-space(cbc:Description) = 'If not, please explain why and state which other means of proof can be provided:'"
                 flag="fatal">Description MUST be "If not, please explain why and state which other means of proof can be provided:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '8c5d1e13-54f7-4895-a65c-b8e09253130c']]">
         <assert id="EHF-ESPD-CT-R4101"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4102"
                 test="normalize-space(cbc:Description) = 'If not, please explain why and specify which other means of proof concerning the quality assurance scheme can be provided:'"
                 flag="fatal">Description MUST be "If not, please explain why and specify which other means of proof concerning the quality assurance scheme can be provided:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'b0aace10-fd73-46d1-ae78-289ee5cd42ca']]">
         <assert id="EHF-ESPD-CT-R4201"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4202"
                 test="normalize-space(cbc:Description) = 'If not, please explain why and specify which other means of proof concerning the environmental management systems or standards can be provided:'"
                 flag="fatal">Description MUST be "If not, please explain why and specify which other means of proof concerning the environmental management systems or standards can be provided:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '7f18c64e-ae09-4646-9400-f3666d50af51']]">
         <assert id="EHF-ESPD-CT-R4301"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R4302"
                 test="normalize-space(cbc:Description) = 'Your answer'"
                 flag="fatal">Description MUST be "Your answer".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '4e552658-d532-4770-943b-b90efcc9788d']]">
         <assert id="EHF-ESPD-CT-R4401"
                 test="@responseDataType = 'PERCENTAGE'"
                 flag="fatal">Response data type MUST be "PERCENTAGE".</assert>
         <assert id="EHF-ESPD-CT-R4402"
                 test="normalize-space(cbc:Description) = 'What is the corresponding percentage of disabled or disadvantaged workers?'"
                 flag="fatal">Description MUST be "What is the corresponding percentage of disabled or disadvantaged workers?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'e01d0929-c7a9-455a-aaf9-e1f7cd966336']]">
         <assert id="EHF-ESPD-CT-R4501"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4502"
                 test="normalize-space(cbc:Description) = 'If required, please provide details on whether the employees concerned belong to a specific category of disabled or disadvantaged workers?'"
                 flag="fatal">Description MUST be "If required, please provide details on whether the employees concerned belong to a specific category of disabled or disadvantaged workers?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '30064ad3-fc11-4579-8528-fdd0b9a5ba75']]">
         <assert id="EHF-ESPD-CT-R4601"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4602"
                 test="normalize-space(cbc:Description) = 'a) Please provide the relevant registration or certification number, if applicable:'"
                 flag="fatal">Description MUST be "a) Please provide the relevant registration or certification number, if applicable:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'b3403349-cbc0-4d84-879e-fc0f2d90ecbd']]">
         <assert id="EHF-ESPD-CT-R4701"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4702"
                 test="normalize-space(cbc:Description) = 'b) If the certificate of registration or certification is available electronically, please state:'"
                 flag="fatal">Description MUST be "b) If the certificate of registration or certification is available electronically, please state:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '792ff522-6f3f-4a62-ab6e-a8b272bc290e']]">
         <assert id="EHF-ESPD-CT-R4801"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R4802"
                 test="normalize-space(cbc:Description) = 'c) Please state the references on which the registration or certification is based, and, where applicable, the classification obtained in the official list:'"
                 flag="fatal">Description MUST be "c) Please state the references on which the registration or certification is based, and, where applicable, the classification obtained in the official list:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'd9996ef5-49f9-4cf8-a2f5-31c9f4efd894']]">
         <assert id="EHF-ESPD-CT-R4901"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R4902"
                 test="normalize-space(cbc:Description) = 'd) Does the registration or certification cover all of the required selection criteria?'"
                 flag="fatal">Description MUST be "d) Does the registration or certification cover all of the required selection criteria?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '0e71abd3-198e-49c5-8128-5708617bb191']]">
         <assert id="EHF-ESPD-CT-R5001"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R5002"
                 test="normalize-space(cbc:Description) = 'e) Will the economic operator be able to provide a certificate with regard to the payment of social security contributions and taxes or provide information enabling the contracting authority or contracting entity to obtaining it directly by accessing a national database in any Member State that is available free of charge?'"
                 flag="fatal">Description MUST be "e) Will the economic operator be able to provide a certificate with regard to the payment of social security contributions and taxes or provide information enabling the contracting authority or contracting entity to obtaining it directly by accessing a national database in any Member State that is available free of charge?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'caa72cea-5443-49fb-84ba-ab6c64427f77']]">
         <assert id="EHF-ESPD-CT-R5101"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5102"
                 test="normalize-space(cbc:Description) = 'If the relevant documentation is available electronically, please indicate:'"
                 flag="fatal">Description MUST be "If the relevant documentation is available electronically, please indicate:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '907fd62b-02f1-452c-81a8-785bedb0c536']]">
         <assert id="EHF-ESPD-CT-R5201"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5202"
                 test="normalize-space(cbc:Description) = 'a) Please indicate the role of the economic operator in the group (leader, responsible for specific tasks...):'"
                 flag="fatal">Description MUST be "a) Please indicate the role of the economic operator in the group (leader, responsible for specific tasks...):".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '7c267f95-a3a7-49ef-abd9-e121dcd641a9']]">
         <assert id="EHF-ESPD-CT-R5301"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5302"
                 test="normalize-space(cbc:Description) = 'b) Please identify the other economic operators participating in the procurement procedure together:'"
                 flag="fatal">Description MUST be "b) Please identify the other economic operators participating in the procurement procedure together:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '96f38793-4469-4153-aba6-c613282cdbdc']]">
         <assert id="EHF-ESPD-CT-R5401"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5402"
                 test="normalize-space(cbc:Description) = 'c) Where applicable, name of the participating group:'"
                 flag="fatal">Description MUST be "c) Where applicable, name of the participating group:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '999c7fe2-61cd-4e86-b76f-e280304dc8c9']]">
         <assert id="EHF-ESPD-CT-R5501"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5502"
                 test="normalize-space(cbc:Description) = 'If yes and in so far as known, please list the proposed subcontractors:'"
                 flag="fatal">Description MUST be "If yes and in so far as known, please list the proposed subcontractors:".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '323f19b5-3308-4873-b2d1-767963cc81e9']]">
         <assert id="EHF-ESPD-CT-R5601"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R5602"
                 test="normalize-space(cbc:Description) = 'Please describe them'"
                 flag="fatal">Description MUST be "Please describe them".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '0622bbd1-7378-45e1-8fb9-25429740ac22']]">
         <assert id="EHF-ESPD-CT-R5701"
                 test="@responseDataType = 'INDICATOR'"
                 flag="fatal">Response data type MUST be "INDICATOR".</assert>
         <assert id="EHF-ESPD-CT-R5702"
                 test="normalize-space(cbc:Description) = 'Is this information available electronically?'"
                 flag="fatal">Description MUST be "Is this information available electronically?".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'ee1ee1cd-3791-4855-8b8b-28d4f4c5c007']]">
         <assert id="EHF-ESPD-CT-R5801"
                 test="@responseDataType = 'EVIDENCE_URL'"
                 flag="fatal">Response data type MUST be "EVIDENCE_URL".</assert>
         <assert id="EHF-ESPD-CT-R5802"
                 test="normalize-space(cbc:Description) = 'URL'"
                 flag="fatal">Description MUST be "URL".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = '1e55ff14-c643-4abc-91d7-2f4dfcdf2409']]">
         <assert id="EHF-ESPD-CT-R5901"
                 test="@responseDataType = 'CODE'"
                 flag="fatal">Response data type MUST be "CODE".</assert>
         <assert id="EHF-ESPD-CT-R5902"
                 test="normalize-space(cbc:Description) = 'Code'"
                 flag="fatal">Description MUST be "Code".</assert>
      </rule>
      <rule context="ccv:Requirement[cbc:ID[text() = 'd8e1e818-d67b-4bb9-9aeb-4c10943a8342']]">
         <assert id="EHF-ESPD-CT-R6001"
                 test="@responseDataType = 'DESCRIPTION'"
                 flag="fatal">Response data type MUST be "DESCRIPTION".</assert>
         <assert id="EHF-ESPD-CT-R6002"
                 test="normalize-space(cbc:Description) = 'Issuer'"
                 flag="fatal">Description MUST be "Issuer".</assert>
      </rule>
      <rule context="ccv:Requirement">
         <assert id="EHF-ESPD-CT-R0001" test="false()" flag="fatal">Invalid requirement.</assert>
      </rule>
   </pattern>

</schema>
