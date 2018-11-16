///
/// Copyright 2016-2018 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {Injectable} from '@angular/core';
import {ApicallService} from './apicall.service';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {FormGroup, NgForm} from '@angular/forms';
import {Cadetails} from '../model/caDetails.model';
import {ESPDRequest} from '../model/ESPDRequest.model';
import {FullCriterion} from '../model/fullCriterion.model';
// import {saveAs} from 'file-saver/FileSaver';
import {saveAs} from 'file-saver/FileSaver';
import {EoDetails} from '../model/eoDetails.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {ESPDResponse} from '../model/ESPDResponse.model';
import * as moment from 'moment';
import {PostalAddress} from '../model/postalAddress.model';
import {ContactingDetails} from '../model/contactingDetails.model';
import {MatSnackBar} from '@angular/material';
import {FormUtilService} from './form-util.service';
import {UtilitiesService} from './utilities.service';
import {TranslateService} from '@ngx-translate/core';
import {Language} from '../model/language.model';
import {ExportType} from '../export/export-type.enum';
import {CaRelatedCriterion} from '../model/caRelatedCriterion.model';
import {DocumentDetails} from '../model/documentDetails.model';
import {Amount} from '../model/amount.model';
import {CodeList} from '../model/codeList.model';

import _ from 'lodash';

@Injectable()
export class DataService {

  /* ================================= Criterion Filtering Regex ===============================*/
  EXCLUSION_REGEXP: RegExp = /^CRITERION.EXCLUSION.+/;
  EXCLUSION_CONVICTION_REGEXP: RegExp = /^CRITERION.EXCLUSION.CONVICTIONS.+/;
  EXCLUSION_CONTRIBUTION_REGEXP: RegExp = /^CRITERION.EXCLUSION.CONTRIBUTIONS.+/;
  EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP: RegExp = /(^CRITERION.EXCLUSION.SOCIAL.+)|(^CRITERION.EXCLUSION.BUSINESS.+)|(^CRITERION.EXCLUSION.MISCONDUCT.+)|(^CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.+)/;
  EXCLUSION_NATIONAL_REGEXP: RegExp = /^CRITERION.EXCLUSION.NATIONAL.+/;

  SELECTION_REGEXP: RegExp = /^CRITERION.SELECTION.+/;
  SELECTION_SUITABILITY_REGEXP: RegExp = /^CRITERION.SELECTION.SUITABILITY.+/;
  SELECTION_ECONOMIC_REGEXP: RegExp = /^CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.+/;
  SELECTION_TECHNICAL_REGEXP: RegExp = /(?!.*CERTIFICATES*)^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.+/;
  SELECTION_CERTIFICATES_REGEXP: RegExp = /^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.+/;

  EO_RELATED_REGEXP: RegExp = /(?!.*MEETS_THE_OBJECTIVE*)^CRITERION.OTHER.EO_DATA.+/;
  EO_RELATED_A_REGEXP: RegExp = /(^CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST*)|(^CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP*)|(^CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS*)|(^CRITERION.OTHER.EO_DATA.CONTRIBUTIONS_CERTIFICATES*)/;
  EO_RELATED_C_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES*/;
  EO_RELATED_D_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES*/;
  REDUCTION_OF_CANDIDATES_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE*/;

  OTHER_CA_REGEXP: RegExp = /^CRITERION.OTHER.CA_DATA.+/;
  EO_LOT_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.LOTS_TENDERED/;


  exclusionACriteria: ExclusionCriteria[] = null;
  exclusionBCriteria: ExclusionCriteria[] = null;
  exclusionCCriteria: ExclusionCriteria[] = null;
  exclusionDCriteria: ExclusionCriteria[] = null;
  selectionACriteria: SelectionCriteria[] = null;
  selectionBCriteria: SelectionCriteria[] = null;
  selectionCCriteria: SelectionCriteria[] = null;
  selectionDCriteria: SelectionCriteria[] = null;
  selectionALLCriteria: SelectionCriteria[] = null;
  fullCriterionList: FullCriterion[] = null;
  caRelatedCriteria: CaRelatedCriterion[] = null;
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  eoRelatedACriteria: EoRelatedCriterion[] = null;
  eoRelatedCCriteria: EoRelatedCriterion[] = null;
  eoRelatedDCriteria: EoRelatedCriterion[] = null;
  eoLotCriterion: EoRelatedCriterion[] = null;
  reductionCriteria: ReductionCriterion[] = null;
  language: Language[] = null;
  selectedLanguage: string = null;
  langTemplate = [];
  langNames = [];
  langISOCodes = [];
  projectLots = [];
  // evidenceList: Evidence[] = [];

  notDefCriteria: EoRelatedCriterion[] = null;

  blob = null;
  blobV2 = null;

  isSatisfiedALLeo: boolean;
  toggleIndicator: boolean;

  CADetails: Cadetails = new Cadetails();
  EODetails: EoDetails = new EoDetails();
  PostalAddress: PostalAddress = new PostalAddress();
  ContactingDetails: ContactingDetails = new ContactingDetails();
  generalTurnover: Amount = new Amount();
  documentDetails: DocumentDetails = new DocumentDetails();
  espdRequest: ESPDRequest;
  espdResponse: ESPDResponse;
  version: string;
  receivedNoticeNumber: string;
  selectedCountry = '';
  selectedEOCountry = '';
  public EOForm: FormGroup;


  /* =========================================== FORMS =============================================== */

  public eoRelatedACriteriaForm: FormGroup = null;
  public eoRelatedCCriteriaForm: FormGroup = null;
  public eoRelatedDCriteriaForm: FormGroup = null;
  public eoLotCriterionForm: FormGroup = null;

  public caRelatedCriteriaForm: FormGroup = null;

  public exclusionACriteriaForm: FormGroup = null;
  public exclusionBCriteriaForm: FormGroup = null;
  public exclusionCCriteriaForm: FormGroup = null;
  public exclusionDCriteriaForm: FormGroup = null;

  public selectionACriteriaForm: FormGroup = null;
  public selectionBCriteriaForm: FormGroup = null;
  public selectionCCriteriaForm: FormGroup = null;
  public selectionDCriteriaForm: FormGroup = null;

  public selectionALLCriteriaForm: FormGroup = null;

  public reductionCriteriaForm: FormGroup = null;


  constructor(private APIService: ApicallService,
              public formUtil: FormUtilService,
              public utilities: UtilitiesService,
              public translate: TranslateService) {

    this.AddLanguages();
    translate.setDefaultLang('ESPD_en');
    console.log(this.translate.getLangs());
  }


  /* ================= Merge criterions into one fullcriterion list ================*/

  /* ============================= Filtering Criteria ============================*/


  filterExclusionCriteria(regex: RegExp, criteriaList: FullCriterion[]): ExclusionCriteria[] {
    const filteredList: FullCriterion[] = [];
    for (const fullCriterion of criteriaList) {
      if (regex.test(fullCriterion.typeCode)) {
        filteredList.push(fullCriterion);
      }
    }
    return filteredList;
  }

  makeFullCriterionListCA(caRelatedCriteria: CaRelatedCriterion[],
                          exclusionACriteria: ExclusionCriteria[],
                          exclusionBCriteria: ExclusionCriteria[],
                          exclusionCCriteria: ExclusionCriteria[],
                          exclusionDCriteria: ExclusionCriteria[],
                          isSatisfiedALL: boolean,
                          selectionALLCriteria?: SelectionCriteria[],
                          selectionACriteria?: SelectionCriteria[],
                          selectionBCriteria?: SelectionCriteria[],
                          selectionCCriteria?: SelectionCriteria[],
                          selectionDCriteria?: SelectionCriteria[],
                          eoRelatedCriteria?: EoRelatedCriterion[],
                          reductionCriteria?: ReductionCriterion[]
  ): FullCriterion[] {

    if (this.utilities.isCA) {
      if (isSatisfiedALL) {
        console.log(reductionCriteria);
        var combineJsonArray = [...caRelatedCriteria,
          ...exclusionACriteria,
          ...exclusionBCriteria,
          ...exclusionCCriteria,
          ...exclusionDCriteria,
          ...selectionALLCriteria,
          ...eoRelatedCriteria,
          ...reductionCriteria
        ];
        // console.log(combineJsonArray);
        return combineJsonArray;

      } else {
        console.log(reductionCriteria);
        var combineJsonArray = [...caRelatedCriteria,
          ...exclusionACriteria,
          ...exclusionBCriteria,
          ...exclusionCCriteria,
          ...exclusionDCriteria,
          ...selectionACriteria,
          ...selectionBCriteria,
          ...selectionCCriteria,
          ...selectionDCriteria,
          ...eoRelatedCriteria,
          ...reductionCriteria];
        // console.dir(combineJsonArray);
        return combineJsonArray;
      }
    }
  }


  makeFullCriterionListEO(caRelatedCriteria: CaRelatedCriterion[],
                          exclusionACriteria: ExclusionCriteria[],
                          exclusionBCriteria: ExclusionCriteria[],
                          exclusionCCriteria: ExclusionCriteria[],
                          exclusionDCriteria: ExclusionCriteria[],
                          isSatisfiedALL: boolean,
                          selectionALLCriteria?: SelectionCriteria[],
                          selectionACriteria?: SelectionCriteria[],
                          selectionBCriteria?: SelectionCriteria[],
                          selectionCCriteria?: SelectionCriteria[],
                          selectionDCriteria?: SelectionCriteria[],
                          eoRelatedACriteria?: EoRelatedCriterion[],
                          eoRelatedCCriteria?: EoRelatedCriterion[],
                          eoRelatedDCriteria?: EoRelatedCriterion[],
                          reductionCriteria?: ReductionCriterion[]
  ): FullCriterion[] {
    if (this.utilities.isEO) {
      if (isSatisfiedALL) {
        console.log(reductionCriteria);
        console.log(eoRelatedACriteria);
        console.log(eoRelatedCCriteria);
        console.log(eoRelatedDCriteria);
        var combineJsonArray = [...caRelatedCriteria,
          ...exclusionACriteria,
          ...exclusionBCriteria,
          ...exclusionCCriteria,
          ...exclusionDCriteria,
          ...selectionALLCriteria,
          ...eoRelatedACriteria,
          ...eoRelatedCCriteria,
          ...eoRelatedDCriteria,
          ...reductionCriteria];
        // console.dir(combineJsonArray);
        return combineJsonArray;

      } else {
        var combineJsonArray = [...caRelatedCriteria,
          ...exclusionACriteria,
          ...exclusionBCriteria,
          ...exclusionCCriteria,
          ...exclusionDCriteria,
          ...selectionACriteria,
          ...selectionBCriteria,
          ...selectionCCriteria,
          ...selectionDCriteria,
          ...eoRelatedACriteria,
          ...eoRelatedCCriteria,
          ...eoRelatedDCriteria,
          ...reductionCriteria];
        // console.dir(combineJsonArray);
        return combineJsonArray;
      }
    }
  }


  filterSelectionCriteria(regex: RegExp, criteriaList: FullCriterion[]): SelectionCriteria[] {
    const filteredList: FullCriterion[] = [];
    for (const fullCriterion of criteriaList) {
      if (regex.test(fullCriterion.typeCode)) {
        filteredList.push(fullCriterion);
      }
    }
    return filteredList;
  }

  filterEoRelatedCriteria(regex: RegExp, criteriaList: FullCriterion[]): EoRelatedCriterion[] {
    const filteredList: FullCriterion[] = [];
    for (const fullCriterion of criteriaList) {
      if (regex.test(fullCriterion.typeCode)) {
        filteredList.push(fullCriterion);
      }
    }
    return filteredList;
  }

  filterReductionCriteria(regex: RegExp, criteriaList: FullCriterion[]): ReductionCriterion[] {
    const filteredList: FullCriterion[] = [];
    for (const fullCriterion of criteriaList) {
      if (regex.test(fullCriterion.typeCode)) {
        filteredList.push(fullCriterion);
      }
    }
    return filteredList;
  }

  filterCARelatedCriteria(regex: RegExp, criteriaList: FullCriterion[]): CaRelatedCriterion[] {
    const filteredList: FullCriterion[] = [];
    for (const fullCriterion of criteriaList) {
      if (regex.test(fullCriterion.typeCode)) {
        filteredList.push(fullCriterion);
      }
    }
    return filteredList;
  }


  /* ================================= create ESPDRequest Object =======================*/

  createESPDRequest(): ESPDRequest {
    /* SET DOCUMENT DETAILS */
    this.documentDetails.qualificationApplicationType = this.utilities.qualificationApplicationType.toUpperCase();
    this.documentDetails.type = this.utilities.type.toUpperCase();
    this.documentDetails.version = this.APIService.version.toUpperCase();
    /* CREATE ESPD REQUEST */
    this.espdRequest = new ESPDRequest(this.CADetails, this.fullCriterionList, this.documentDetails);
    console.log(this.espdRequest);
    return this.espdRequest;

  }


  createESPDResponse(): ESPDResponse {

    /* SET DOCUMENT DETAILS */
    this.documentDetails.qualificationApplicationType = this.utilities.qualificationApplicationType.toUpperCase();
    this.documentDetails.type = this.utilities.type.toUpperCase();
    this.documentDetails.version = this.APIService.version.toUpperCase();

    /* CREATE ESPD RESPONSE */
    this.espdResponse = new ESPDResponse(
      this.CADetails,
      this.EODetails,
      this.fullCriterionList,
      this.formUtil.evidenceList,
      this.documentDetails);

    if (typeof this.espdResponse.eodetails.naturalPersons[0].birthDate !== 'string'
      && this.espdResponse.eodetails.naturalPersons[0].birthDate !== null) {
      let utcDate = this.utilities.toUTCDate(this.espdResponse.eodetails.naturalPersons[0].birthDate);
      this.espdResponse.eodetails.naturalPersons[0].birthDate = moment(utcDate);
    }

    console.log(this.espdResponse);
    return this.espdResponse;
  }


  /* ============================= step submit actions =================================*/

  selectionSubmit(isSatisfiedALL: boolean) {
    // console.log('THIS IS SELECTION ISSUE: ');
    // console.log(isSatisfiedALL);
    // console.log(this.selectionALLCriteria);
    // console.log(this.caRelatedCriteria);
    // console.log(this.selectionACriteria);
    // console.log(this.selectionBCriteria);
    // console.log(this.selectionCCriteria);
    // console.log(this.selectionDCriteria);
    // console.log(this.utilities.qualificationApplicationType);

    /* WORKAROUND-FIX: satisfiesALL Criteria null issue when it's self-contained */
    if (this.utilities.qualificationApplicationType === 'selfcontained') {
      this.selectionALLCriteria = [];
    }
    console.log(this.selectionALLCriteria);

    /* extract caRelated criteria */
    if (this.utilities.qualificationApplicationType === 'selfcontained') {
      this.formUtil.extractFormValuesFromCriteria(this.caRelatedCriteria, this.caRelatedCriteriaForm, this.formUtil.evidenceList);
    }
    /* extract exclusion criteria */
    this.formUtil.extractFormValuesFromCriteria(this.exclusionACriteria, this.exclusionACriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionBCriteria, this.exclusionBCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionCCriteria, this.exclusionCCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionDCriteria, this.exclusionDCriteriaForm, this.formUtil.evidenceList);
    /* extract selection criteria */
    this.formUtil.extractFormValuesFromCriteria(this.selectionACriteria, this.selectionACriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionBCriteria, this.selectionBCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionCCriteria, this.selectionCCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionDCriteria, this.selectionDCriteriaForm, this.formUtil.evidenceList);

    // create ESPDRequest
    // console.dir(JSON.stringify(this.createESPDRequest(isSatisfiedALL)));
    // console.log(this.reductionCriteria);
    this.fullCriterionList = this.makeFullCriterionListCA(this.caRelatedCriteria,
      this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria,
      isSatisfiedALL,
      this.selectionALLCriteria,
      this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.eoRelatedCriteria,
      this.reductionCriteria);

    console.log(this.fullCriterionList);
    // apicall service post


  }

  finishSubmit(exportType: ExportType) {

    switch (exportType) {
      case ExportType.XML:
        this.createRequestXmlFile();
        break;
      case ExportType.HTML:
        this.createRequestHtmlFile();
        break;
      case ExportType.PDF:
        this.createRequestPdfFile();
    }
  }

  createRequestXmlFile() {
    this.APIService.getXMLRequest(JSON.stringify(this.createESPDRequest()), this.selectedLanguage)
      .then(res => {
        this.createXmlFile(res);
        this.saveFile(this.blob, '.xml');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  createRequestHtmlFile() {
    this.APIService.getHTMLRequest(JSON.stringify(this.createESPDRequest()), this.selectedLanguage)
      .then(res => {
        this.createHtmlFile(res);
        this.saveFile(this.blob, '.html');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  createRequestPdfFile() {
    this.APIService.getPDFRequest(JSON.stringify(this.createESPDRequest()), this.selectedLanguage)
      .then(res => {
        this.createPdfFile(res);
        this.saveFile(this.blob, '.pdf');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  selectionEOSubmit(isSatisfiedALL: boolean) {
    this.isSatisfiedALLeo = isSatisfiedALL;

  }

  finishEOSubmit(exportType: ExportType) {

    /* extract caRelated criteria */
    if (this.utilities.qualificationApplicationType === 'selfcontained') {
      /* WORKAROUND-FIX: satisfiesALL Criteria null issue when it's self-contained */
      this.selectionALLCriteria = [];
      this.formUtil.extractFormValuesFromCriteria(this.caRelatedCriteria, this.caRelatedCriteriaForm, this.formUtil.evidenceList);
    }

    /* extract eoRelated criteria */
    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedACriteria, this.eoRelatedACriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedCCriteria, this.eoRelatedCCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedDCriteria, this.eoRelatedDCriteriaForm, this.formUtil.evidenceList);

    /* extract exclusion criteria */
    this.formUtil.extractFormValuesFromCriteria(this.exclusionACriteria, this.exclusionACriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionBCriteria, this.exclusionBCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionCCriteria, this.exclusionCCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionDCriteria, this.exclusionDCriteriaForm, this.formUtil.evidenceList);

    /* extract selection criteria */
    this.formUtil.extractFormValuesFromCriteria(this.selectionACriteria, this.selectionACriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionBCriteria, this.selectionBCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionCCriteria, this.selectionCCriteriaForm, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionDCriteria, this.selectionDCriteriaForm, this.formUtil.evidenceList);

    if (this.isSatisfiedALLeo) {
      this.formUtil.extractFormValuesFromCriteria(this.selectionALLCriteria, this.selectionALLCriteriaForm, this.formUtil.evidenceList);
    }

    /* extract reduction criteria */
    this.formUtil.extractFormValuesFromCriteria(this.reductionCriteria, this.reductionCriteriaForm, this.formUtil.evidenceList);

    // make full criterion list
    this.fullCriterionList = this.makeFullCriterionListEO(this.caRelatedCriteria,
      this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria,
      this.isSatisfiedALLeo,
      this.selectionALLCriteria,
      this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.eoRelatedACriteria,
      this.eoRelatedCCriteria,
      this.eoRelatedDCriteria,
      this.reductionCriteria
    );

    console.log(this.fullCriterionList);

    switch (exportType) {
      case ExportType.XML:
        this.createXml();
        break;
      case ExportType.HTML:
        this.createHtml();
        break;
      case ExportType.PDF:
        this.createPdf();
    }

  }

  createPdf() {
    this.APIService.getPDFResponse(JSON.stringify(this.createESPDResponse()), this.selectedLanguage)
      .then(res => {
        this.createPdfFile(res);
        this.saveFile(this.blob, '.pdf');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  createHtml() {
    this.APIService.getHTMLResponse(JSON.stringify(this.createESPDResponse()), this.selectedLanguage)
      .then(res => {
        this.createHtmlFile(res);
        this.saveFile(this.blob, '.html');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  createXml() {
    this.APIService.getXMLResponse(JSON.stringify(this.createESPDResponse()), this.selectedLanguage)
      .then(res => {
        console.log(res);
        this.createXmlFile(res);
        this.saveFile(this.blob, '.xml');
      })
      .catch(err => {
        console.log(err);
        console.log(err.error);
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }


  /* ================================== EXPORT FILES ============================= */

  createHtmlFile(response) {
    this.createFile(response, 'application/html');
  }

  createPdfFile(response) {
    this.createFile(response, 'application/pdf');
  }

  createXmlFile(response) {
    this.createFile(response, 'application/xml');
  }

  private createFile(response, type: string) {
// const filename:string = "espd-request";
    this.blob = new Blob([response.body], {type: type});
  }

  isVersionTwo(): boolean {
    return this.APIService.version === 'v2';
  }

  saveFile(blob, fileSuffix: String) {
    if (this.utilities.isCA && this.APIService.version === 'v1') {
      var filename = 'espd-request-v1' + fileSuffix;
    } else if (this.utilities.isEO && this.APIService.version === 'v1') {
      var filename = 'espd-response-v1' + fileSuffix;
    } else if (this.utilities.isCA && this.APIService.version === 'v2' && this.utilities.qualificationApplicationType === 'regulated') {
      var filename = 'espd-request-v2' + fileSuffix;
    } else if (this.utilities.isEO && this.APIService.version === 'v2' && this.utilities.qualificationApplicationType === 'regulated') {
      var filename = 'espd-response-v2' + fileSuffix;
    } else if (this.utilities.isCA && this.utilities.qualificationApplicationType === 'selfcontained') {
      var filename = 'espd-self-contained-request' + fileSuffix;
    } else if (this.utilities.isEO && this.utilities.qualificationApplicationType === 'selfcontained') {
      var filename = 'espd-self-contained-response' + fileSuffix;
    }
    saveAs(blob, filename);
  }


  /* ================================= CA REUSE ESPD REQUEST ====================== */

  ReuseESPD(filesToUpload: File[], form: NgForm, role: string): Promise<void> {
    const promise = new Promise<void>((resolve, reject) => {

      if (filesToUpload.length > 0 && role === 'CA') {
        this.APIService.postFile(filesToUpload)
          .then(res => {
            /* DUMMY ESPD for testing */
            // res = this.utilities.makeDummyESPDRequest();
            // console.log(res);
            console.log('REUSE EPSD');
            this.APIService.version = res.documentDetails.version.toLowerCase();
            /* SELF-CONTAINED: if a self-contained artifact is imported then the version is v2 */
            this.utilities.qualificationApplicationType = res.documentDetails.qualificationApplicationType.toLowerCase();
            if (res.documentDetails.qualificationApplicationType === 'SELFCONTAINED') {
              this.APIService.version = 'v2';
              // Create the lots here:
              this.utilities.projectLots = _.range(res.cadetails.procurementProjectLots).map(i => `Lot${i + 1}`);
            }

            // res.cadetails=this.CADetails;
            // console.log(res.fullCriterionList);
            console.log(res.cadetails);
            this.CADetails = res.cadetails;
            this.receivedNoticeNumber = res.cadetails.receivedNoticeNumber;
            this.PostalAddress = res.cadetails.postalAddress;
            this.ContactingDetails = res.cadetails.contactingDetails;
            // console.log(res.cadetails.postalAddress);
            console.log(this.CADetails.postalAddress.addressLine1);
            console.log(this.CADetails.postalAddress.city);
            console.log(this.CADetails.postalAddress.postCode);
            this.selectedCountry = this.CADetails.cacountry;

            if (this.utilities.qualificationApplicationType === 'selfcontained') {
              this.CADetails.classificationCodes = res.cadetails.classificationCodes;
              this.CADetails.weightScoringMethodologyNote = res.cadetails.weightScoringMethodologyNote;
              this.CADetails.weightingType = res.cadetails.weightingType;
              this.utilities.isAtoD = true;
              this.utilities.isSatisfiedALL = false;

              this.caRelatedCriteria = this.filterCARelatedCriteria(this.OTHER_CA_REGEXP, res.fullCriterionList);
              this.caRelatedCriteriaForm = this.formUtil.createCARelatedCriterionForm(this.caRelatedCriteria);
              this.eoLotCriterion = this.filterCARelatedCriteria(this.EO_LOT_REGEXP, res.fullCriterionList);
            }
            console.log(res.fullCriterionList);
            this.exclusionACriteria = this.filterExclusionCriteria(this.EXCLUSION_CONVICTION_REGEXP, res.fullCriterionList);
            this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_CONTRIBUTION_REGEXP, res.fullCriterionList);
            this.exclusionCCriteria = this.filterExclusionCriteria(this.EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP, res.fullCriterionList);
            this.exclusionDCriteria = this.filterExclusionCriteria(this.EXCLUSION_NATIONAL_REGEXP, res.fullCriterionList);

            this.exclusionACriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionACriteria);
            // console.log(this.exclusionACriteriaForm);
            this.exclusionBCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionBCriteria);
            // console.log(this.exclusionBCriteriaForm);
            this.exclusionCCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionCCriteria);
            // console.log(this.exclusionCCriteriaForm);
            this.exclusionDCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionDCriteria);
            // console.log(this.exclusionDCriteriaForm);

            // console.log(this.exclusionDCriteria);

            this.selectionACriteria = this.filterSelectionCriteria(this.SELECTION_SUITABILITY_REGEXP, res.fullCriterionList);
            this.selectionBCriteria = this.filterSelectionCriteria(this.SELECTION_ECONOMIC_REGEXP, res.fullCriterionList);
            this.selectionCCriteria = this.filterSelectionCriteria(this.SELECTION_TECHNICAL_REGEXP, res.fullCriterionList);
            this.selectionDCriteria = this.filterSelectionCriteria(this.SELECTION_CERTIFICATES_REGEXP, res.fullCriterionList);
            this.selectionALLCriteria = this.filterSelectionCriteria(this.SELECTION_REGEXP, res.fullCriterionList);

            this.selectionACriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionACriteria);
            // console.log(this.selectionACriteriaForm);
            this.selectionBCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionBCriteria);
            // console.log(this.selectionBCriteriaForm);
            this.selectionCCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionCCriteria);
            // console.log(this.selectionCCriteriaForm);
            this.selectionDCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionDCriteria);
            // console.log(this.selectionDCriteriaForm);
            this.selectionALLCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionALLCriteria);

            this.eoRelatedCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_REGEXP, res.fullCriterionList);
            // this.eoLotCriterionForm = this.formUtil.createEORelatedCriterionForm(this.eoLotCriterion);
            this.reductionCriteria = this.filterEoRelatedCriteria(this.REDUCTION_OF_CANDIDATES_REGEXP, res.fullCriterionList);

            // create requirementGroup template objects required for multiple instances (cardinalities) function
            this.formUtil.createTemplateReqGroups(res.fullCriterionList);
            console.log(res);
            console.log(this.CADetails);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });
      } else if (filesToUpload.length > 0 && role === 'EO') {
        this.APIService.postFileResponse(filesToUpload)
          .then(res => {
            console.log(res);
            this.APIService.version = res.documentDetails.version.toLowerCase();
            this.utilities.qualificationApplicationType = res.documentDetails.qualificationApplicationType.toLowerCase();
            /* SELF-CONTAINED: if a self-cointained artifact is imported then the version is v2 */
            if (res.documentDetails.qualificationApplicationType === 'SELFCONTAINED') {
              this.APIService.version = 'v2';
              // Create the lots here:
              this.utilities.projectLots = _.range(res.cadetails.procurementProjectLots).map(i => `Lot${i + 1}`);
            }
            // res.cadetails=this.CADetails;
            // console.log(res.fullCriterionList);
            // console.log(res.cadetails);
            this.CADetails = res.cadetails;
            this.PostalAddress = res.cadetails.postalAddress;
            this.ContactingDetails = res.cadetails.contactingDetails;
            this.receivedNoticeNumber = res.cadetails.receivedNoticeNumber;
            this.selectedCountry = this.CADetails.cacountry;
            this.EODetails = res.eodetails;
            console.log(this.EODetails);
            console.log(this.EODetails.naturalPersons);
            // console.log(this.EODetails.naturalPersons['birthDate']);
            this.selectedEOCountry = this.EODetails.postalAddress.countryCode;
            if (this.utilities.qualificationApplicationType === 'selfcontained') {
              this.CADetails.classificationCodes = res.cadetails.classificationCodes;
              this.CADetails.weightScoringMethodologyNote = res.cadetails.weightScoringMethodologyNote;
              this.CADetails.weightingType = res.cadetails.weightingType;

              this.utilities.isAtoD = true;
              this.utilities.isSatisfiedALL = false;

              if (res.eodetails.generalTurnover !== null || res.eodetails.generalTurnover !== undefined) {
                this.generalTurnover = res.eodetails.generalTurnover;
              } else if (res.eodetails.generalTurnover === null) {
                this.generalTurnover = new Amount();
                // this.generalTurnover.amount = 0;
                // this.generalTurnover.currency = '';

                this.EODetails.generalTurnover = this.generalTurnover;
              }
            }

            // get evidence list only in v2
            if (this.APIService.version === 'v2') {
              this.formUtil.evidenceList = res.evidenceList;
              this.formUtil.evidenceList = res.evidenceList;
              console.log(this.formUtil.evidenceList);
            }


            // Fill in EoDetails Form

            this.eoDetailsFormUpdate();
            this.caDetailsFormUpdate();
            // console.log(this.EOForm.value);

            console.log(res.fullCriterionList);

            if (this.utilities.qualificationApplicationType === 'selfcontained') {
              this.caRelatedCriteria = this.filterCARelatedCriteria(this.OTHER_CA_REGEXP, res.fullCriterionList);
              console.log('FILTERED CA RELATED CRITERIA');
              console.log(this.caRelatedCriteria);

              this.caRelatedCriteriaForm = this.formUtil.createCARelatedCriterionForm(this.caRelatedCriteria);
              console.log('FILTERED CA RELATED CRITERIA FORM');
              console.log(this.caRelatedCriteriaForm);

              this.eoLotCriterion = this.filterCARelatedCriteria(this.EO_LOT_REGEXP, res.fullCriterionList);
              console.log('FILTERED EO LOT RELATED CRITERIA');
              console.log(this.eoLotCriterion);
              this.eoLotCriterionForm = this.formUtil.createEORelatedCriterionForm(this.eoLotCriterion);
            }


            this.eoRelatedACriteria = this.filterEoRelatedCriteria(this.EO_RELATED_A_REGEXP, res.fullCriterionList);
            // console.log(this.eoRelatedACriteria);
            this.eoRelatedCCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_C_REGEXP, res.fullCriterionList);
            // console.log(this.eoRelatedCCriteria);
            this.eoRelatedDCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_D_REGEXP, res.fullCriterionList);
            // console.log(this.eoRelatedDCriteria);
            this.eoRelatedACriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedACriteria);
            // console.log(this.eoRelatedACriteriaForm);
            this.eoRelatedCCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedCCriteria);
            // console.log(this.eoRelatedCCriteriaForm);
            this.eoRelatedDCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedDCriteria);
            // console.log(this.eoRelatedDCriteriaForm);


            this.exclusionACriteria = this.filterExclusionCriteria(this.EXCLUSION_CONVICTION_REGEXP, res.fullCriterionList);
            // console.log(this.exclusionACriteria);
            this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_CONTRIBUTION_REGEXP, res.fullCriterionList);
            // console.log(this.exclusionBCriteria);
            this.exclusionCCriteria = this.filterExclusionCriteria(this.EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP, res.fullCriterionList);
            // console.log(this.exclusionCCriteria);
            this.exclusionDCriteria = this.filterExclusionCriteria(this.EXCLUSION_NATIONAL_REGEXP, res.fullCriterionList);
            // console.log(this.exclusionDCriteria);

            this.exclusionACriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionACriteria);
            console.log(this.exclusionACriteriaForm);
            this.exclusionBCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionBCriteria);
            console.log(this.exclusionBCriteriaForm);
            this.exclusionCCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionCCriteria);
            // console.log(this.exclusionCCriteriaForm);
            this.exclusionDCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionDCriteria);
            // console.log(this.exclusionDCriteriaForm);


            this.selectionACriteria = this.filterSelectionCriteria(this.SELECTION_SUITABILITY_REGEXP, res.fullCriterionList);
            // console.log(this.selectionACriteria);
            this.selectionBCriteria = this.filterSelectionCriteria(this.SELECTION_ECONOMIC_REGEXP, res.fullCriterionList);
            // console.log(this.selectionBCriteria);
            this.selectionCCriteria = this.filterSelectionCriteria(this.SELECTION_TECHNICAL_REGEXP, res.fullCriterionList);
            // console.log(this.selectionCCriteria);
            this.selectionDCriteria = this.filterSelectionCriteria(this.SELECTION_CERTIFICATES_REGEXP, res.fullCriterionList);
            // console.log(this.selectionDCriteria);
            this.selectionALLCriteria = this.filterSelectionCriteria(this.SELECTION_REGEXP, res.fullCriterionList);
            console.log(this.selectionALLCriteria);


            this.selectionACriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionACriteria);
            // console.log(this.selectionACriteriaForm);
            this.selectionBCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionBCriteria);
            // console.log(this.selectionBCriteriaForm);
            this.selectionCCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionCCriteria);
            // console.log(this.selectionCCriteriaForm);
            this.selectionDCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionDCriteria);
            // console.log(this.selectionDCriteriaForm);
            this.selectionALLCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionALLCriteria);


            this.reductionCriteria = this.filterReductionCriteria(this.REDUCTION_OF_CANDIDATES_REGEXP, res.fullCriterionList);
            if (!this.reductionCriteria) {

            }
            this.reductionCriteriaForm = this.formUtil.createReductionCriterionForm(this.reductionCriteria);

            // REVIEW ESPD: make forms non editable if user selected Review ESPD
            if (this.isReadOnly()) {
              this.exclusionACriteriaForm.disable();
              this.exclusionBCriteriaForm.disable();
              this.exclusionCCriteriaForm.disable();
              this.exclusionDCriteriaForm.disable();
              this.selectionALLCriteriaForm.disable();
              this.selectionACriteriaForm.disable();
              this.selectionBCriteriaForm.disable();
              this.selectionCCriteriaForm.disable();
              this.selectionDCriteriaForm.disable();
              this.eoRelatedACriteriaForm.disable();
              this.eoRelatedCCriteriaForm.disable();
              this.eoRelatedDCriteriaForm.disable();
              this.reductionCriteriaForm.disable();
            }

            // create requirementGroup template objects required for multiple instances (cardinalities) function
            this.formUtil.createTemplateReqGroups(res.fullCriterionList);

            /* find if CRITERION.SELECTION.ALL_SATISFIED exists */
            this.utilities.satisfiedALLCriterionExists = this.utilities
              .findCriterion(this.selectionALLCriteria, '7e7db838-eeac-46d9-ab39-42927486f22d');

            if (this.utilities.satisfiedALLCriterionExists) {
              this.utilities.isSatisfiedALL = true;
              this.utilities.isAtoD = false;
            } else {
              this.utilities.isSatisfiedALL = false;
              this.utilities.isAtoD = true;
            }
            console.log(res);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });
      }
    });
    return promise;
  }

  isReadOnly(): boolean {
    if (this.utilities.isReviewESPD) {
      // console.log('It is REVIEWESPD');
      return true;
    } else {
      // console.log('It is NOT review espd');
      return false;
    }
  }

  eoDetailsFormUpdate() {

    /* ====================== Date Manipulation =====================================*/
    if (this.EODetails.naturalPersons[0]['birthDate']) {
      console.log(this.EODetails.naturalPersons[0]['birthDate']);
      // this.EODetails.naturalPersons[0]['birthDate'] = this.EODetails.naturalPersons[0]['birthDate'].utc();
    }


    this.EOForm.patchValue({
      'name': this.EODetails.name,
      'smeIndicator': this.EODetails.smeIndicator,
      'eoRole': this.EODetails.eoRole,
      'employeeQuantity': this.EODetails.employeeQuantity,
      'postalAddress': {
        'addressLine1': this.EODetails.postalAddress.addressLine1,
        'postCode': this.EODetails.postalAddress.postCode,
        'city': this.EODetails.postalAddress.city,
        'countryCode': this.selectedEOCountry,
      },
      'id': this.EODetails.id,
      'webSiteURI': this.EODetails.webSiteURI,
      'procurementProjectLot': this.EODetails.procurementProjectLot
    });

    if (this.EODetails.generalTurnover !== null && this.EODetails.generalTurnover !== undefined) {
      this.EOForm.patchValue({
        'generalTurnover': {
          'amount': this.EODetails.generalTurnover.amount,
          'currency': this.EODetails.generalTurnover.currency
        }
      });
    }

    if (this.EODetails.contactingDetails !== null) {
      this.EOForm.patchValue({
        'contactingDetails': {
          'contactPointName': this.EODetails.contactingDetails.contactPointName,
          'emailAddress': this.EODetails.contactingDetails.emailAddress,
          'telephoneNumber': this.EODetails.contactingDetails.telephoneNumber,
          'faxNumber': this.EODetails.contactingDetails.faxNumber,
        }
      });
    }

    if (this.EODetails.naturalPersons !== null || this.EODetails.naturalPersons !== undefined) {
      // TODO find better way to handle null fields in naturalperson's objects
      if (this.EODetails.naturalPersons[0].firstName !== null) {
        this.EOForm.patchValue({
          'naturalPersons': this.EODetails.naturalPersons
        });
      }
    }

    /* ====================== FORM RESET in case of creating new ESPD ================================ */
    if (this.utilities.isReset && (this.utilities.isCreateResponse || this.utilities.isCreateNewESPD)) {
      this.EOForm.reset('');
    }
  }

  caDetailsFormUpdate() {
    if (this.utilities.isReset && (this.utilities.isCreateResponse || this.utilities.isCreateNewESPD)) {
      this.utilities.setAllFields(this.CADetails.postalAddress, '');
      this.utilities.setAllFields(this.CADetails.contactingDetails, '');
      this.utilities.setAllFields(this.CADetails, '');
    }
  }

  startESPD(form: NgForm): Promise<void> {

    const promise = new Promise<void>((resolve, reject) => {
      // console.log(form);
      // console.log(form.value);
      console.log('START ESPD');
      console.log(form);

      // form reset
      if (!this.utilities.isEmpty(this.CADetails) || !this.utilities.isEmpty(this.EODetails)) {
        this.eoRelatedACriteriaForm = JSON.parse(JSON.stringify(null));
        this.eoRelatedCCriteriaForm = JSON.parse(JSON.stringify(null));
        this.eoRelatedDCriteriaForm = JSON.parse(JSON.stringify(null));
        this.exclusionACriteriaForm = JSON.parse(JSON.stringify(null));
        this.exclusionBCriteriaForm = JSON.parse(JSON.stringify(null));
        this.exclusionCCriteriaForm = JSON.parse(JSON.stringify(null));
        this.exclusionDCriteriaForm = JSON.parse(JSON.stringify(null));
        this.selectionALLCriteriaForm = JSON.parse(JSON.stringify(null));
        this.selectionACriteriaForm = JSON.parse(JSON.stringify(null));
        this.selectionBCriteriaForm = JSON.parse(JSON.stringify(null));
        this.selectionCCriteriaForm = JSON.parse(JSON.stringify(null));
        this.selectionDCriteriaForm = JSON.parse(JSON.stringify(null));
        this.reductionCriteriaForm = JSON.parse(JSON.stringify(null));
        this.eoRelatedACriteria = JSON.parse(JSON.stringify(null));
        this.eoRelatedCCriteria = JSON.parse(JSON.stringify(null));
        this.eoRelatedDCriteria = JSON.parse(JSON.stringify(null));
        this.exclusionACriteria = JSON.parse(JSON.stringify(null));
        this.exclusionBCriteria = JSON.parse(JSON.stringify(null));
        this.exclusionCCriteria = JSON.parse(JSON.stringify(null));
        this.exclusionDCriteria = JSON.parse(JSON.stringify(null));
        this.selectionALLCriteria = JSON.parse(JSON.stringify(null));
        this.selectionACriteria = JSON.parse(JSON.stringify(null));
        this.selectionBCriteria = JSON.parse(JSON.stringify(null));
        this.selectionCCriteria = JSON.parse(JSON.stringify(null));
        this.selectionDCriteria = JSON.parse(JSON.stringify(null));
        this.reductionCriteria = JSON.parse(JSON.stringify(null));
        this.utilities.setAllFields(this.PostalAddress, '');
        this.utilities.setAllFields(this.ContactingDetails, '');
        this.utilities.setAllFields(this.CADetails, '');
        this.utilities.setAllFields(this.EODetails, '');
        this.formUtil.evidenceList = [];
        this.utilities.isReset = true;
      }

      if (form.value.chooseRole === 'CA') {
        this.utilities.isCA = true;
        this.utilities.isEO = false;
        this.receivedNoticeNumber = form.value.noticeNumber;
        if (form.value.CACountry !== '') {
          this.selectedCountry = form.value.CACountry;
        }
      }

      if (form.value.chooseRole === 'EO') {
        this.utilities.isEO = true;
        this.utilities.isCA = false;
        if (form.value.EOCountry !== '') {
          this.selectedEOCountry = form.value.EOCountry;
        }
      }

      if (form.value.chooseRole === 'EO' && form.value.eoOptions === 'importESPD') {
        this.utilities.isImportESPD = true;
        this.utilities.isCreateResponse = false;
      } else if (form.value.chooseRole === 'EO' && form.value.eoOptions === 'createResponse') {
        this.utilities.isImportESPD = false;
        this.utilities.isCreateResponse = true;
      }

      /* ===================== create forms in case of predefined criteria ================== */
      if (this.utilities.isCreateResponse) {
        this.getEoRelatedACriteria()
          .then(res => {

            if (this.utilities.isCreateResponse) {
              this.eoRelatedACriteria = res;

              /* [cardinalities]: create template requirementGroup */
              this.formUtil.createTemplateReqGroups(this.eoRelatedACriteria);

              console.log('This is create response');
            }
            this.eoRelatedACriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedACriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getEoRelatedCCriteria()
          .then(res => {
            this.eoRelatedCCriteria = res;
            this.eoRelatedCCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedCCriteria);

            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.eoRelatedCCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getEoRelatedDCriteria()
          .then(res => {
            this.eoRelatedDCriteria = res;
            this.eoRelatedDCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedDCriteria);

            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.eoRelatedDCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        /* =================== SELF-CONTAINED: predefined ca related criterion ============ */

        if (this.utilities.qualificationApplicationType === 'selfcontained') {
          this.getCaRelatedCriteria()
            .then(res => {
              this.caRelatedCriteria = res;
              this.caRelatedCriteriaForm = this.formUtil.createCARelatedCriterionForm(this.caRelatedCriteria);
              // console.log(this.caRelatedCriteria);
              resolve();
            })
            .catch(err => {
              console.log(err);
              const message: string = err.error +
                ' ' + err.message;
              const action = 'close';
              this.utilities.openSnackBar(message, action);
              reject();
            });

          this.getEOLotCriterion()
            .then(res => {
              this.eoLotCriterion = res;
              this.eoLotCriterionForm = this.formUtil.createEORelatedCriterionForm(this.eoLotCriterion);
              // console.log(this.caRelatedCriteria);
              resolve();
            })
            .catch(err => {
              console.log(err);
              const message: string = err.error +
                ' ' + err.message;
              const action = 'close';
              this.utilities.openSnackBar(message, action);
              reject();
            });
        } else {
          this.caRelatedCriteria = [];
          this.eoLotCriterion = [];
        }


        /* ========================= predefined exclusion criteria response ============= */
        this.getExclusionACriteria()
          .then(res => {
            this.exclusionACriteria = res;
            this.exclusionACriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionACriteria);
            console.log(this.exclusionACriteriaForm);

            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionACriteria);
            resolve();

          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionBCriteria()
          .then(res => {
            this.exclusionBCriteria = res;
            this.exclusionBCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionBCriteria);
            console.log(this.exclusionBCriteriaForm);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionBCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionCCriteria()
          .then(res => {
            this.exclusionCCriteria = res;
            this.exclusionCCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionCCriteria);
            // console.log(res);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionCCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionDCriteria()
          .then(res => {
            this.exclusionDCriteria = res;
            this.exclusionDCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionDCriteria);
            // console.log(res);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionDCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        /* ======================== predefined selection criteria ============================ */
        this.getSelectionALLCriteria()
          .then(res => {
            this.selectionALLCriteria = res;
            this.selectionALLCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionALLCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionALLCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });


        this.getSelectionACriteria()
          .then(res => {
            this.selectionACriteria = res;
            this.selectionACriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionACriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionACriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });


        this.getSelectionBCriteria()
          .then(res => {
            this.selectionBCriteria = res;
            this.selectionBCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionBCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionBCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getSelectionCCriteria()
          .then(res => {
            this.selectionCCriteria = res;
            this.selectionCCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionCCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionCCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getSelectionDCriteria()
          .then(res => {
            this.selectionDCriteria = res;
            this.selectionDCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionDCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionDCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        /* =========================== predefined reduction criteria ================================= */
        this.getReductionCriteria()
          .then(res => {
            this.reductionCriteria = res;
            this.reductionCriteriaForm = this.formUtil.createReductionCriterionForm(this.reductionCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.reductionCriteria);
            console.log('TEMPLATE ARRAY: ');
            console.log(this.formUtil.template);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

      }
      // get predefined criteria (ca)
      if (this.utilities.isCreateNewESPD) {

        /* =========================== predefined reduction criteria ================================= */
        this.getReductionCriteria()
          .then(res => {
            this.reductionCriteria = res;
            // console.log(this.reductionCriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.reductionCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        /* ========================= predefined other.eo criteria ====================================*/
        this.getEoRelatedCriteria()
          .then(res => {
            this.eoRelatedCriteria = res;
            // console.log(this.eoRelatedCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        /* ========================= SELF-CONTAINED: predefined other.ca criteria ============================= */

        if (this.utilities.qualificationApplicationType === 'selfcontained') {
          this.getCaRelatedCriteria()
            .then(res => {
              this.caRelatedCriteria = res;
              this.caRelatedCriteriaForm = this.formUtil.createCARelatedCriterionForm(this.caRelatedCriteria);
              /* [cardinalities]: create template requirementGroup */
              this.formUtil.createTemplateReqGroups(this.caRelatedCriteria);
              // console.log(this.caRelatedCriteria);
              resolve();
            })
            .catch(err => {
              console.log(err);
              const message: string = err.error +
                ' ' + err.message;
              const action = 'close';
              this.utilities.openSnackBar(message, action);
              reject();
            });


          /* SELF-CONTAINED: OTHER_EO LOT TENDERED CRITERION */
          this.getEOLotCriterion()
            .then(res => {
              this.eoLotCriterion = res;
              this.eoLotCriterionForm = this.formUtil.createEORelatedCriterionForm(this.eoLotCriterion);
              resolve();
            })
            .catch(err => {
              console.log(err);
              const message: string = err.error +
                ' ' + err.message;
              const action = 'close';
              this.utilities.openSnackBar(message, action);
              reject();
            });
        } else {
          this.caRelatedCriteria = [];
          this.eoLotCriterion = [];
        }

        /* ======================== predefined exclusion criteria ================================== */

        this.getExclusionACriteria()
          .then(res => {
            this.exclusionACriteria = res;
            this.exclusionACriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionACriteria);
            // console.log("This is exclusionACriteria: ");
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionACriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionBCriteria()
          .then(res => {
            this.exclusionBCriteria = res;
            this.exclusionBCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionBCriteria);
            // console.log(res);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionBCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionCCriteria()
          .then(res => {
            this.exclusionCCriteria = res;
            this.exclusionCCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionCCriteria);
            // console.log(res);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionCCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getExclusionDCriteria()
          .then(res => {
            this.exclusionDCriteria = res;
            this.exclusionDCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionDCriteria);
            // console.log(res);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.exclusionDCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });


        /* ============================= predefined selection criteria =========================== */
        this.getSelectionALLCriteria()
          .then(res => {
            this.selectionALLCriteria = res;
            this.selectionALLCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionALLCriteria);
            // console.log(res);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getSelectionACriteria()
          .then(res => {
            this.selectionACriteria = res;
            this.selectionACriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionACriteria);
            /* [cardinalities]: create template requirementGroup */
            this.formUtil.createTemplateReqGroups(this.selectionACriteria);
            // console.log(res);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });


        this.getSelectionBCriteria()
          .then(res => {
            this.selectionBCriteria = res;
            this.selectionBCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionBCriteria);
            // console.log(res);
            this.formUtil.createTemplateReqGroups(this.selectionBCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getSelectionCCriteria()
          .then(res => {
            this.selectionCCriteria = res;
            this.selectionCCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionCCriteria);
            // console.log('SELECTION C CRITERIA FORM:  ========================================================= ');
            // console.log(this.selectionCCriteriaForm);
            // console.log(res);
            this.formUtil.createTemplateReqGroups(this.selectionCCriteria);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });

        this.getSelectionDCriteria()
          .then(res => {
            this.selectionDCriteria = res;
            this.selectionDCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionDCriteria);
            this.formUtil.createTemplateReqGroups(this.selectionDCriteria);
            // console.log(res);
            resolve();
          })
          .catch(err => {
            console.log(err);
            const message: string = err.error +
              ' ' + err.message;
            const action = 'close';
            this.utilities.openSnackBar(message, action);
            reject();
          });
      }
    });
    return promise;
  }

  AddLanguages() {
    this.getLanguages().then(res => {
      let langs = [];
      console.log(res);
      res.forEach(lang => {
          // langs.push('ESPD_' + lang.code.toLowerCase());
          langs.push(lang.name);
          this.langNames.push(lang.name);
          /* create associative template array in order to retrieve the code using the language name from the dropdown ui element */
          this.langTemplate[lang.name] = lang.code;
          // this.langISOCodes[lang.name] = lang.code;
          // console.log(langs);
        }
      );

      // this.langISOCodes = res;
      this.translate.addLangs(langs);
      // MAP to ISO 3166-1-alpha-2 code for css flag library to work properly
      res.map(x => {
        x.code = x.code
          .replace('EL', 'GR')
          .replace('EN', 'GB')
          .replace('ET', 'EE')
          .replace('GA', 'IE')
          .replace('SL', 'SI')
          .replace('SV', 'SE')
          .replace('DA', 'DK')
          .replace('CS', 'CZ');
        // console.log(x.code);
        this.langISOCodes[x.name] = x.code;
        return x;
      });


    })
      .catch(err => {
        console.log(err);
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.utilities.openSnackBar(message, action);
      });
  }

  switchLanguage(language: string) {
    this.selectedLanguage = this.langTemplate[language].toLowerCase();
    this.utilities.selectedLang = this.selectedLanguage;
    const lang = 'ESPD_' + this.selectedLanguage;
    console.log(lang);
    this.translate.use(lang);
    // this.AddLanguages();
  }

  /* =================================  Get Language ===========================*/

  getLanguages(): Promise<Language[]> {
    if (this.language != null) {
      return Promise.resolve(this.language);
    } else {
      return this.APIService.getLangs()
        .then(res => {
          this.language = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


  /* ======================================== EO related Criteria ========================= */

  getEoRelatedCriteria(): Promise<EoRelatedCriterion[]> {
    if (this.eoRelatedCriteria != null) {
      return Promise.resolve(this.eoRelatedCriteria);
    } else {
      return this.APIService.getEO_RelatedCriteria()
        .then(
          res => {
            this.eoRelatedCriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEoRelatedACriteria(): Promise<EoRelatedCriterion[]> {
    if (this.eoRelatedACriteria != null) {
      return Promise.resolve(this.eoRelatedACriteria);
    } else {
      return this.APIService.getEO_RelatedACriteria()
        .then(
          res => {
            this.eoRelatedACriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEoRelatedCCriteria(): Promise<EoRelatedCriterion[]> {
    if (this.eoRelatedCCriteria != null) {
      return Promise.resolve(this.eoRelatedCCriteria);
    } else {
      return this.APIService.getEO_RelatedCCriteria()
        .then(
          res => {
            this.eoRelatedCCriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEoRelatedDCriteria(): Promise<EoRelatedCriterion[]> {
    if (this.eoRelatedDCriteria != null) {
      return Promise.resolve(this.eoRelatedDCriteria);
    } else {
      return this.APIService.getEO_RelatedDCriteria()
        .then(
          res => {
            this.eoRelatedDCriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEOLotCriterion(): Promise<EoRelatedCriterion[]> {
    if (this.eoLotCriterion != null) {
      return Promise.resolve(this.eoLotCriterion);
    } else {
      return this.APIService.getEO_LotCriterion()
        .then(
          res => {
            this.eoLotCriterion = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  /* =============================== SELF-CONTAINED: CA Related Criteria ======================== */
  getCaRelatedCriteria(): Promise<CaRelatedCriterion[]> {
    if (this.caRelatedCriteria != null) {
      return Promise.resolve(this.caRelatedCriteria);
    } else {
      return this.APIService.getCA_RelatedCriteria()
        .then(
          res => {
            this.caRelatedCriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  /* ================================= Reduction of Candidates Criteria ======================= */
  getReductionCriteria(): Promise<ReductionCriterion[]> {
    if (this.reductionCriteria != null) {
      return Promise.resolve(this.reductionCriteria);
    } else {
      return this.APIService.get_ReductionCriteria()
        .then(
          res => {
            this.reductionCriteria = res;
            return Promise.resolve(res);
          }
        ).catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  /* ======================================= Exclusion Criteria ========================== */

  getExclusionACriteria(): Promise<ExclusionCriteria[]> {
    if (this.exclusionACriteria != null) {
      return Promise.resolve(this.exclusionACriteria);
    } else {
      return this.APIService.getExclusionCriteria_A()
        .then(res => {
          this.exclusionACriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getExclusionBCriteria(): Promise<ExclusionCriteria[]> {
    if (this.exclusionBCriteria != null) {
      return Promise.resolve(this.exclusionBCriteria);
    } else {
      return this.APIService.getExclusionCriteria_B()
        .then(res => {
          this.exclusionBCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getExclusionCCriteria(): Promise<ExclusionCriteria[]> {
    if (this.exclusionCCriteria != null) {
      return Promise.resolve(this.exclusionCCriteria);
    } else {
      return this.APIService.getExclusionCriteria_C()
        .then(res => {
          this.exclusionCCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getExclusionDCriteria(): Promise<ExclusionCriteria[]> {
    if (this.exclusionDCriteria != null) {
      return Promise.resolve(this.exclusionDCriteria);
    } else {
      return this.APIService.getExclusionCriteria_D()
        .then(res => {
          this.exclusionDCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  /* ============================== Selection Criteria ======================================= */

  getSelectionALLCriteria(): Promise<SelectionCriteria[]> {
    if (this.selectionALLCriteria != null) {
      return Promise.resolve(this.selectionALLCriteria);
    } else {
      return this.APIService.getSelectionCriteria()
        .then(res => {
          this.selectionALLCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


  getSelectionACriteria(): Promise<SelectionCriteria[]> {
    if (this.selectionACriteria != null) {
      return Promise.resolve(this.selectionACriteria);
    } else {
      return this.APIService.getSelectionCriteria_A()
        .then(res => {
          this.selectionACriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getSelectionBCriteria(): Promise<SelectionCriteria[]> {
    if (this.selectionBCriteria != null) {
      return Promise.resolve(this.selectionBCriteria);
    } else {
      return this.APIService.getSelectionCriteria_B()
        .then(res => {
          this.selectionBCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getSelectionCCriteria(): Promise<SelectionCriteria[]> {
    if (this.selectionCCriteria != null) {
      return Promise.resolve(this.selectionCCriteria);
    } else {
      return this.APIService.getSelectionCriteria_C()
        .then(res => {
          this.selectionCCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getSelectionDCriteria(): Promise<SelectionCriteria[]> {
    if (this.selectionDCriteria != null) {
      return Promise.resolve(this.selectionDCriteria);
    } else {
      return this.APIService.getSelectionCriteria_D()
        .then(res => {
          this.selectionDCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


}
