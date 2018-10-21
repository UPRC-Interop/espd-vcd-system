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
import {Country} from '../model/country.model';
import {ProcedureType} from '../model/procedureType.model';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {FormGroup, NgForm} from '@angular/forms';
import {Cadetails} from '../model/caDetails.model';
import {ESPDRequest} from '../model/ESPDRequest.model';
import {FullCriterion} from '../model/fullCriterion.model';
import {saveAs} from 'file-saver/FileSaver';
import {EoDetails} from '../model/eoDetails.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {Currency} from '../model/currency.model';
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
import {ExportType} from "../export/export-type.enum";

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
  EO_RELATED_A_REGEXP: RegExp = /(^CRITERION.OTHER.EO_DATA.REGISTERED_IN_OFFICIAL_LIST*)|(^CRITERION.OTHER.EO_DATA.SHELTERED_WORKSHOP*)|(^CRITERION.OTHER.EO_DATA.TOGETHER_WITH_OTHERS*)/;
  EO_RELATED_C_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.RELIES_ON_OTHER_CAPACITIES*/;
  EO_RELATED_D_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.SUBCONTRACTS_WITH_THIRD_PARTIES*/;
  REDUCTION_OF_CANDIDATES_REGEXP: RegExp = /^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE*/;


  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  currency: Currency[] = null;
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
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  eoRelatedACriteria: EoRelatedCriterion[] = null;
  eoRelatedCCriteria: EoRelatedCriterion[] = null;
  eoRelatedDCriteria: EoRelatedCriterion[] = null;
  reductionCriteria: ReductionCriterion[] = null;
  language: Language[] = null;
  langTemplate = [];
  langNames = [];
  langISOCodes = [];
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
  espdRequest: ESPDRequest;
  espdResponse: ESPDResponse;
  version: string;
  receivedNoticeNumber: string;
  selectedCountry: string = '';
  selectedEOCountry: string = '';
  public EOForm: FormGroup;


  /* =========================================== FORMS =============================================== */

  public eoRelatedACriteriaForm: FormGroup = null;
  public eoRelatedCCriteriaForm: FormGroup = null;
  public eoRelatedDCriteriaForm: FormGroup = null;

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
              public snackBar: MatSnackBar,
              public formUtil: FormUtilService,
              public utilities: UtilitiesService,
              public translate: TranslateService) {

    this.AddLanguages();
    translate.setDefaultLang('ESPD_en');
    console.log(this.translate.getLangs());

  }


  /* ============================ snackbar ===================================== */
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action);
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

  makeFullCriterionListCA(exclusionACriteria: ExclusionCriteria[],
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
        var combineJsonArray = [...exclusionACriteria,
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
        var combineJsonArray = [
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


  makeFullCriterionListEO(exclusionACriteria: ExclusionCriteria[],
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
        var combineJsonArray = [...exclusionACriteria,
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
        var combineJsonArray = [
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


  /* ================================= create ESPDRequest Object =======================*/

  createESPDRequest(): ESPDRequest {

    this.espdRequest = new ESPDRequest(this.CADetails, this.fullCriterionList);
    console.log(this.espdRequest);
    return this.espdRequest;

  }


  createESPDResponse(): ESPDResponse {
    this.espdResponse = new ESPDResponse(this.CADetails, this.EODetails, this.fullCriterionList, this.formUtil.evidenceList);

    if (typeof this.espdResponse.eodetails.naturalPersons[0].birthDate !== 'string'
      && this.espdResponse.eodetails.naturalPersons[0].birthDate !== null) {
      let utcDate = this.utilities.toUTCDate(this.espdResponse.eodetails.naturalPersons[0].birthDate);
      this.espdResponse.eodetails.naturalPersons[0].birthDate = moment(utcDate);
    }

    console.log(this.espdResponse);
    return this.espdResponse;
  }


  /* ============================= step submit actions =================================*/

  exclusionSubmit(exclusionCriteriaA: ExclusionCriteria[],
                  exclusionCriteriaB: ExclusionCriteria[],
                  exclusionCriteriaC: ExclusionCriteria[],
                  exclusionCriteriaD: ExclusionCriteria[]) {
    this.exclusionACriteria = exclusionCriteriaA;
    this.exclusionBCriteria = exclusionCriteriaB;
    this.exclusionCCriteria = exclusionCriteriaC;
    this.exclusionDCriteria = exclusionCriteriaD;

  }

  selectionSubmit(selectionCriteriaA: SelectionCriteria[],
                  selectionCriteriaB: SelectionCriteria[],
                  selectionCriteriaC: SelectionCriteria[],
                  selectionCriteriaD: SelectionCriteria[],
                  isSatisfiedALL: boolean) {
    this.selectionACriteria = selectionCriteriaA;
    this.selectionBCriteria = selectionCriteriaB;
    this.selectionCCriteria = selectionCriteriaC;
    this.selectionDCriteria = selectionCriteriaD;

    // create ESPDRequest
    // console.dir(JSON.stringify(this.createESPDRequest(isSatisfiedALL)));
    // console.log(this.reductionCriteria);
    this.fullCriterionList = this.makeFullCriterionListCA(this.exclusionACriteria,
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
    this.APIService.getXMLRequest(JSON.stringify(this.createESPDRequest()))
      .then(res => {
        this.createXmlFile(res);
        this.saveFile(this.blob, '.xml');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }

  createRequestHtmlFile() {
    this.APIService.getHTMLRequest(JSON.stringify(this.createESPDRequest()))
      .then(res => {
        this.createHtmlFile(res);
        this.saveFile(this.blob, '.html');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }

  createRequestPdfFile() {
    this.APIService.getPDFRequest(JSON.stringify(this.createESPDRequest()))
      .then(res => {
        this.createPdfFile(res);
        this.saveFile(this.blob, '.pdf');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }


  procedureEOSubmit(eoRelatedCriteriaA: EoRelatedCriterion[],
                    eoRelatedCriteriaC: EoRelatedCriterion[],
                    eoRelatedCriteriaD: EoRelatedCriterion[]) {
    this.eoRelatedACriteria = eoRelatedCriteriaA;
    this.eoRelatedCCriteria = eoRelatedCriteriaC;
    this.eoRelatedDCriteria = eoRelatedCriteriaD;
  }

  selectionEOSubmit(selectionCriteriaA: SelectionCriteria[],
                    selectionCriteriaB: SelectionCriteria[],
                    selectionCriteriaC: SelectionCriteria[],
                    selectionCriteriaD: SelectionCriteria[],
                    isSatisfiedALL: boolean) {
    this.selectionACriteria = selectionCriteriaA;
    this.selectionBCriteria = selectionCriteriaB;
    this.selectionCCriteria = selectionCriteriaC;
    this.selectionDCriteria = selectionCriteriaD;

    console.log(this.selectionALLCriteria);
    this.isSatisfiedALLeo = isSatisfiedALL;

  }

  finishEOSubmit(reductionCriteria: ReductionCriterion[], exportType: ExportType) {
    this.reductionCriteria = reductionCriteria;

    // make full criterion list
    this.fullCriterionList = this.makeFullCriterionListEO(this.exclusionACriteria,
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
    this.APIService.getPDFResponse(JSON.stringify(this.createESPDResponse()))
      .then(res => {
        this.createPdfFile(res);
        this.saveFile(this.blob, '.pdf');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }

  createHtml() {
    this.APIService.getHTMLResponse(JSON.stringify(this.createESPDResponse()))
      .then(res => {
        this.createHtmlFile(res);
        this.saveFile(this.blob, '.html');
      })
      .catch(err => {
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }

  createXml() {
    this.APIService.getXMLResponse(JSON.stringify(this.createESPDResponse()))
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
        this.openSnackBar(message, action);
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

  isVersionTwo() : boolean {
    return this.APIService.version === 'v2'
  }

  saveFile(blob, fileSuffix: String) {
    if (this.utilities.isCA && this.APIService.version === 'v1') {
      var filename = 'espd-request-v1' + fileSuffix;
    } else if (this.utilities.isEO && this.APIService.version === 'v1') {
      var filename = 'espd-response-v1' + fileSuffix;
    } else if (this.utilities.isCA && this.APIService.version === 'v2') {
      var filename = 'espd-request-v2.xml' + fileSuffix;
    } else if (this.utilities.isEO && this.APIService.version === 'v2') {
      var filename = 'espd-response-v2.xml' + fileSuffix;
    }

    saveAs(blob, filename);
  }


  /* ================================= CA REUSE ESPD REQUEST ====================== */

  ReuseESPD(filesToUpload: File[], form: NgForm, role: string) {
    if (filesToUpload.length > 0 && role === 'CA') {
      this.APIService.postFile(filesToUpload)
        .then(res => {
          console.log('REUSE EPSD');
          this.APIService.version = res.documentDetails.version.toLowerCase();
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

          console.log(res.fullCriterionList);

          this.exclusionACriteria = this.filterExclusionCriteria(this.EXCLUSION_CONVICTION_REGEXP, res.fullCriterionList);
          this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_CONTRIBUTION_REGEXP, res.fullCriterionList);
          this.exclusionCCriteria = this.filterExclusionCriteria(this.EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP, res.fullCriterionList);
          this.exclusionDCriteria = this.filterExclusionCriteria(this.EXCLUSION_NATIONAL_REGEXP, res.fullCriterionList);

          console.log(this.exclusionDCriteria);

          this.selectionACriteria = this.filterSelectionCriteria(this.SELECTION_SUITABILITY_REGEXP, res.fullCriterionList);
          this.selectionBCriteria = this.filterSelectionCriteria(this.SELECTION_ECONOMIC_REGEXP, res.fullCriterionList);
          this.selectionCCriteria = this.filterSelectionCriteria(this.SELECTION_TECHNICAL_REGEXP, res.fullCriterionList);
          this.selectionDCriteria = this.filterSelectionCriteria(this.SELECTION_CERTIFICATES_REGEXP, res.fullCriterionList);
          this.selectionALLCriteria = this.filterSelectionCriteria(this.SELECTION_REGEXP, res.fullCriterionList);

          this.eoRelatedCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_REGEXP, res.fullCriterionList);
          this.reductionCriteria = this.filterEoRelatedCriteria(this.REDUCTION_OF_CANDIDATES_REGEXP, res.fullCriterionList);
          console.log(res);


        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });
    } else if (filesToUpload.length > 0 && role === 'EO') {
      this.APIService.postFileResponse(filesToUpload)
        .then(res => {
          console.log(res);
          this.APIService.version = res.documentDetails.version.toLowerCase();
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


          console.log(res);


        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });
    }

    if (form.value.chooseRole == 'CA') {
      this.utilities.isCA = true;
      this.receivedNoticeNumber = form.value.noticeNumber;
    }

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

  startESPD(form: NgForm) {
    // console.log(form);
    // console.log(form.value);
    console.log('START ESPD');

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

    if (form.value.chooseRole == 'EO' && form.value.eoOptions == 'importESPD') {
      this.utilities.isImportESPD = true;
      this.utilities.isCreateResponse = false;
    } else if (form.value.chooseRole == 'EO' && form.value.eoOptions == 'createResponse') {
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
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getEoRelatedCCriteria()
        .then(res => {
          this.eoRelatedCCriteria = res;
          this.eoRelatedCCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedCCriteria);

          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.eoRelatedCCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getEoRelatedDCriteria()
        .then(res => {
          this.eoRelatedDCriteria = res;
          this.eoRelatedDCriteriaForm = this.formUtil.createEORelatedCriterionForm(this.eoRelatedDCriteria);

          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.eoRelatedDCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });


      /* ========================= predefined exclusion criteria response ============= */
      this.getExclusionACriteria()
        .then(res => {
          this.exclusionACriteria = res;
          this.exclusionACriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionACriteria);
          console.log(this.exclusionACriteriaForm);

          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.exclusionACriteria);

        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionBCriteria()
        .then(res => {
          this.exclusionBCriteria = res;
          this.exclusionBCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionBCriteria);
          console.log(this.exclusionBCriteriaForm);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.exclusionBCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionCCriteria()
        .then(res => {
          this.exclusionCCriteria = res;
          this.exclusionCCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionCCriteria);
          // console.log(res);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.exclusionCCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionDCriteria()
        .then(res => {
          this.exclusionDCriteria = res;
          this.exclusionDCriteriaForm = this.formUtil.createExclusionCriterionForm(this.exclusionDCriteria);
          // console.log(res);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.exclusionDCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      /* ======================== predefined selection criteria ============================ */
      this.getSelectionALLCriteria()
        .then(res => {
          this.selectionALLCriteria = res;
          this.selectionALLCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionALLCriteria);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.selectionALLCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });


      this.getSelectionACriteria()
        .then(res => {
          this.selectionACriteria = res;
          this.selectionACriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionACriteria);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.selectionACriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });


      this.getSelectionBCriteria()
        .then(res => {
          this.selectionBCriteria = res;
          this.selectionBCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionBCriteria);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.selectionBCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getSelectionCCriteria()
        .then(res => {
          this.selectionCCriteria = res;
          this.selectionCCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionCCriteria);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.selectionCCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getSelectionDCriteria()
        .then(res => {
          this.selectionDCriteria = res;
          this.selectionDCriteriaForm = this.formUtil.createSelectionCriterionForm(this.selectionDCriteria);
          /* [cardinalities]: create template requirementGroup */
          this.formUtil.createTemplateReqGroups(this.selectionDCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
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

        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

    }

    // get predefined criteria (ca)
    if (this.utilities.isCreateNewESPD) {

      /* =========================== predefined reduction criteria ================================= */
      this.getReductionCriteria()
        .then(res => {
          this.reductionCriteria = res;
          // console.log(this.reductionCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      /* ========================= predefined other.eo criteria ====================================*/
      this.getEoRelatedCriteria()
        .then(res => {
          this.eoRelatedCriteria = res;
          // console.log(this.eoRelatedCriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      /* ======================== predefined exclusion criteria ================================== */

      this.getExclusionACriteria()
        .then(res => {
          this.exclusionACriteria = res;
          // console.log("This is exclusionACriteria: ");
          // console.log(this.exclusionACriteria);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionBCriteria()
        .then(res => {
          this.exclusionBCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionCCriteria()
        .then(res => {
          this.exclusionCCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getExclusionDCriteria()
        .then(res => {
          this.exclusionDCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });


      /* ============================= predefined selection criteria =========================== */
      this.getSelectionALLCriteria()
        .then(res => {
          this.selectionALLCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getSelectionACriteria()
        .then(res => {
          this.selectionACriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });


      this.getSelectionBCriteria()
        .then(res => {
          this.selectionBCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getSelectionCCriteria()
        .then(res => {
          this.selectionCCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

      this.getSelectionDCriteria()
        .then(res => {
          this.selectionDCriteria = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
        });

    }

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
      // console.log('LANG TEMPLATE: ');
      // console.log(this.langTemplate);
      // console.log('LANG_ISO: ');
      // console.log(this.langISOCodes);
      // console.log('RESPONSE: ');
      // console.log(res);


    })
      .catch(err => {
        console.log(err);
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });
  }

  switchLanguage(language: string) {
    const lang = 'ESPD_' + this.langTemplate[language].toLowerCase();
    console.log(lang);
    this.translate.use(lang);
    // this.AddLanguages();
  }

  /* =================================  Get from Codelists ===========================*/

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
          this.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


  getCountries(): Promise<Country[]> {
    if (this.countries != null) {
      return Promise.resolve(this.countries);
    } else {
      return this.APIService.getCountryList()
        .then(res => {
          this.countries = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getProcedureTypes(): Promise<ProcedureType[]> {
    if (this.procedureTypes != null) {
      return Promise.resolve(this.procedureTypes);
    } else {
      return this.APIService.getProcedureType()
        .then(res => {
          this.procedureTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getCurrency(): Promise<Currency[]> {
    if (this.currency != null) {
      return Promise.resolve(this.currency);
    } else {
      return this.APIService.getCurr()
        .then(res => {
          this.currency = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
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
          this.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


}
