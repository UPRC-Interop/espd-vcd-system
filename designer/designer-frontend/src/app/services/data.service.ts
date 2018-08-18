import {Injectable} from '@angular/core';
import {ApicallService} from '../services/apicall.service';
import {Country} from '../model/country.model';
import {ProcedureType} from '../model/procedureType.model';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {Cadetails} from '../model/caDetails.model';
import {ESPDRequest} from '../model/ESPDRequest.model';
import {FullCriterion} from '../model/fullCriterion.model';
import {saveAs} from 'file-saver/FileSaver';
import {EoDetails} from '../model/eoDetails.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';
import {Currency} from '../model/currency.model';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {ESPDResponse} from '../model/ESPDResponse.model';

import * as moment from 'moment';
import {Moment} from 'moment';
import {PostalAddress} from '../model/postalAddress.model';
import {ContactingDetails} from '../model/contactingDetails.model';
import {MatSnackBar} from '@angular/material';
import {Evidence} from '../model/evidence.model';

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
  evidenceList: Evidence[] = [];

  notDefCriteria: EoRelatedCriterion[] = null;

  blob = null;
  blobV2 = null;

  isSatisfiedALLeo: boolean;

  CADetails: Cadetails = new Cadetails();
  EODetails: EoDetails = new EoDetails();
  PostalAddress: PostalAddress = new PostalAddress();
  ContactingDetails: ContactingDetails = new ContactingDetails();
  espdRequest: ESPDRequest;
  espdResponse: ESPDResponse;
  version: string;

  isCA: boolean = false;
  isEO: boolean = false;
  isImportESPD: boolean = false;
  isCreateResponse: boolean = false;
  isCreateNewESPD: boolean = false;
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


  constructor(private APIService: ApicallService, public snackBar: MatSnackBar) {

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
    // let exAString:string= JSON.stringify(exclusionACriteria);
    // let exBString:string= JSON.stringify(exclusionBCriteria);

    if (this.isCA) {
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
    // let exAString:string= JSON.stringify(exclusionACriteria);
    // let exBString:string= JSON.stringify(exclusionBCriteria);

    // if (this.isCA) {
    //   if (isSatisfiedALL) {
    //     console.log(reductionCriteria);
    //     var combineJsonArray = [...exclusionACriteria,
    //       ...exclusionBCriteria,
    //       ...exclusionCCriteria,
    //       ...exclusionDCriteria,
    //       ...selectionALLCriteria,
    //       ...eoRelatedCriteria,
    //       ...reductionCriteria
    //     ];
    //     // console.log(combineJsonArray);
    //     return combineJsonArray;
    //
    //   } else {
    //     console.log(reductionCriteria);
    //     var combineJsonArray = [
    //       ...exclusionACriteria,
    //       ...exclusionBCriteria,
    //       ...exclusionCCriteria,
    //       ...exclusionDCriteria,
    //       ...selectionACriteria,
    //       ...selectionBCriteria,
    //       ...selectionCCriteria,
    //       ...selectionDCriteria,
    //       ...eoRelatedCriteria,
    //       ...reductionCriteria];
    //     // console.dir(combineJsonArray);
    //     return combineJsonArray;
    //   }
    // } else
    if (this.isEO) {
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

  // date handling

  toUTCDate(date: Moment): Moment {
    if (date !== null && date !== undefined) {
      const utcDate = new Date(Date.UTC(date.toDate().getFullYear(),
        date.toDate().getMonth(),
        date.toDate().getDate(),
        date.toDate().getHours(),
        date.toDate().getMinutes()));

      return date = moment(utcDate);
    }

  }


  createESPDResponse(): ESPDResponse {
    this.espdResponse = new ESPDResponse(this.CADetails, this.EODetails, this.fullCriterionList, this.evidenceList);
    console.log(this.espdResponse.eodetails.naturalPersons[0].birthDate);
    console.log(JSON.stringify(this.espdResponse.eodetails.naturalPersons[0].birthDate));
    // let utcDate = new Date(Date.UTC(this.espdResponse.eodetails.naturalPersons[0].birthDate.toDate().getFullYear(),
    //   this.espdResponse.eodetails.naturalPersons[0].birthDate.toDate().getMonth(),
    //   this.espdResponse.eodetails.naturalPersons[0].birthDate.toDate().getDate(),
    //   this.espdResponse.eodetails.naturalPersons[0].birthDate.toDate().getHours(),
    //   this.espdResponse.eodetails.naturalPersons[0].birthDate.toDate().getMinutes()));
    // console.log(utcDate);
    //
    // // let momentUTC = moment.utc(this.espdResponse.eodetails.naturalPersons[0].birthDate).format();
    // // console.log(momentUTC);
    // // console.log(JSON.stringify(momentUTC));
    //
    // this.espdResponse.eodetails.naturalPersons[0].birthDate = moment(utcDate);
    //
    // let result = JSON.stringify(utcDate);
    // console.log(result);

    if (typeof this.espdResponse.eodetails.naturalPersons[0].birthDate !== 'string'
      && this.espdResponse.eodetails.naturalPersons[0].birthDate !== null) {
      let utcDate = this.toUTCDate(this.espdResponse.eodetails.naturalPersons[0].birthDate);
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
    this.APIService.getXMLRequest(JSON.stringify(this.createESPDRequest()))
      .then(res => {
        console.log(res);
        this.createFile(res);
      })
      .catch(err => {
        console.log(err);
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });

    // this.APIService.getXMLRequestV2(JSON.stringify(this.createESPDRequest()))
    //   .then(res => {
    //     console.log(res);
    //     this.createFileV2(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });

  }

  // procedureSubmit(eoRelatedCriteria: EoRelatedCriterion[], reductionCriteria: ReductionCriterion[]) {
  //   this.eoRelatedCriteria = eoRelatedCriteria;
  //   this.reductionCriteria = reductionCriteria;
  //   // console.log(this.reductionCriteria);
  // }


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

  finishEOSubmit(reductionCriteria: ReductionCriterion[]) {
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


    this.APIService.getXMLResponse(JSON.stringify(this.createESPDResponse()))
      .then(res => {
        console.log(res);
        this.createFile(res);
        this.saveFile(this.blob);
      })
      .catch(err => {
        console.log(err);
        console.log(err.error);
        const message: string = err.error +
          ' ' + err.message;
        const action = 'close';
        this.openSnackBar(message, action);
      });

    // if (this.APIService.version == 'v1') {
    //   this.APIService.getXMLResponse(JSON.stringify(this.createESPDResponse()))
    //     .then(res => {
    //       console.log(res);
    //       this.createFile(res);
    //       this.saveFile(this.blob);
    //     })
    //     .catch(err => {
    //       console.log(err);
    //     });
    // } else if (this.APIService.version == 'v2') {
    //   this.APIService.getXMLResponseV2(JSON.stringify(this.createESPDResponse()))
    //     .then(res => {
    //       console.log(res);
    //       // this.createFileV2(res);
    //       // this.saveFile(this.blobV2);
    //
    //       this.createFile(res);
    //       this.saveFile(this.blob);
    //     })
    //     .catch(err => {
    //       console.log(err);
    //     });
    // }

  }


  /* ================================== EXPORT FILES ============================= */


  createFile(response) {
    // const filename:string = "espd-request";
    this.blob = new Blob([response.body], {type: 'text/xml'});
  }

  // createFileV2(response) {
  //   // const filename:string = "espd-request";
  //   this.blobV2 = new Blob([response.body], {type: 'text/xml'});
  // }

  saveFile(blob) {
    if (this.isCA && this.APIService.version === 'v1') {
      var filename = 'espd-request-v1';
    } else if (this.isEO && this.APIService.version === 'v1') {
      var filename = 'espd-response-v1';
    } else if (this.isCA && this.APIService.version === 'v2') {
      var filename = 'espd-request-v2';
    } else if (this.isEO && this.APIService.version === 'v2') {
      var filename = 'espd-response-v2';
    }

    saveAs(blob, filename);
  }


  /* ================================= CA REUSE ESPD REQUEST ====================== */

  ReuseESPD(filesToUpload: File[], form: NgForm, role: string) {
    if (filesToUpload.length > 0 && role == 'CA') {
      this.APIService.postFile(filesToUpload)
        .then(res => {
          this.APIService.version = res.documentDetails.version.toLowerCase();
          // res.cadetails=this.CADetails;
          // console.log(res.fullCriterionList);
          console.log(res.cadetails);
          this.CADetails = res.cadetails;
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
          this.selectedCountry = this.CADetails.cacountry;
          this.EODetails = res.eodetails;
          console.log(this.EODetails);
          console.log(this.EODetails.naturalPersons);
          // console.log(this.EODetails.naturalPersons['birthDate']);
          this.selectedEOCountry = this.EODetails.postalAddress.countryCode;

          // get evidence list only in v2
          if (this.APIService.version === 'v2') {
            this.evidenceList = res.evidenceList;
            console.log(this.evidenceList);
          }


          // Fill in EoDetails Form

          this.eoDetailsFormUpdate();
          // console.log(this.EOForm.value);

          console.log(res.fullCriterionList);

          this.eoRelatedACriteria = this.filterEoRelatedCriteria(this.EO_RELATED_A_REGEXP, res.fullCriterionList);
          // console.log(this.eoRelatedACriteria);
          this.eoRelatedCCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_C_REGEXP, res.fullCriterionList);
          // console.log(this.eoRelatedCCriteria);
          this.eoRelatedDCriteria = this.filterEoRelatedCriteria(this.EO_RELATED_D_REGEXP, res.fullCriterionList);
          // console.log(this.eoRelatedDCriteria);
          this.eoRelatedACriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedACriteria);
          // console.log(this.eoRelatedACriteriaForm);
          this.eoRelatedCCriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedCCriteria);
          // console.log(this.eoRelatedCCriteriaForm);
          this.eoRelatedDCriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedDCriteria);
          // console.log(this.eoRelatedDCriteriaForm);


          this.exclusionACriteria = this.filterExclusionCriteria(this.EXCLUSION_CONVICTION_REGEXP, res.fullCriterionList);
          // console.log(this.exclusionACriteria);
          this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_CONTRIBUTION_REGEXP, res.fullCriterionList);
          // console.log(this.exclusionBCriteria);
          this.exclusionCCriteria = this.filterExclusionCriteria(this.EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP, res.fullCriterionList);
          // console.log(this.exclusionCCriteria);
          this.exclusionDCriteria = this.filterExclusionCriteria(this.EXCLUSION_NATIONAL_REGEXP, res.fullCriterionList);
          // console.log(this.exclusionDCriteria);

          this.exclusionACriteriaForm = this.createExclusionCriterionForm(this.exclusionACriteria);
          console.log(this.exclusionACriteriaForm);
          this.exclusionBCriteriaForm = this.createExclusionCriterionForm(this.exclusionBCriteria);
          console.log(this.exclusionBCriteriaForm);
          this.exclusionCCriteriaForm = this.createExclusionCriterionForm(this.exclusionCCriteria);
          // console.log(this.exclusionCCriteriaForm);
          this.exclusionDCriteriaForm = this.createExclusionCriterionForm(this.exclusionDCriteria);
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


          this.selectionACriteriaForm = this.createSelectionCriterionForm(this.selectionACriteria);
          // console.log(this.selectionACriteriaForm);
          this.selectionBCriteriaForm = this.createSelectionCriterionForm(this.selectionBCriteria);
          // console.log(this.selectionBCriteriaForm);
          this.selectionCCriteriaForm = this.createSelectionCriterionForm(this.selectionCCriteria);
          // console.log(this.selectionCCriteriaForm);
          this.selectionDCriteriaForm = this.createSelectionCriterionForm(this.selectionDCriteria);
          // console.log(this.selectionDCriteriaForm);
          this.selectionALLCriteriaForm = this.createSelectionCriterionForm(this.selectionALLCriteria);


          this.reductionCriteria = this.filterReductionCriteria(this.REDUCTION_OF_CANDIDATES_REGEXP, res.fullCriterionList);
          if (!this.reductionCriteria) {

          }
          this.reductionCriteriaForm = this.createReductionCriterionForm(this.reductionCriteria);


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
      this.isCA = true;
      this.receivedNoticeNumber = form.value.noticeNumber;
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

  }


  /*  ======================================== Date Manipulation ================================*/

  // getDateJSObjectD(...dateArray: any[]): Date {
  //   console.log(dateArray);
  //   console.log(dateArray[0][0]);
  //   let date = new Date(dateArray[0][0], dateArray[0][1], dateArray[0][2]);
  //   console.log(date);
  //   // console.log(date.toDateString());
  //   // date.setMinutes( date.getMinutes() + date.getTimezoneOffset() );
  //   return date;
  // }

  getDateJSObjectMoment(...dateArray: any[]): Moment {
    const date = moment(dateArray[0][0], dateArray[0][1], dateArray[0][2]);
    return date;
  }


  startESPD(form: NgForm) {
    // console.log(form);
    // console.log(form.value);

    if (form.value.chooseRole === 'CA') {
      this.isCA = true;
      this.isEO = false;
      this.receivedNoticeNumber = form.value.noticeNumber;
      if (form.value.CACountry !== '') {
        this.selectedCountry = form.value.CACountry;
      }
    }

    if (form.value.chooseRole === 'EO') {
      this.isEO = true;
      this.isCA = false;
      if (form.value.EOCountry !== '') {
        this.selectedEOCountry = form.value.EOCountry;

      }
    }

    if (form.value.chooseRole == 'EO' && form.value.eoOptions == 'importESPD') {
      this.isImportESPD = true;
      this.isCreateResponse = false;
    } else if (form.value.chooseRole == 'EO' && form.value.eoOptions == 'createResponse') {
      this.isImportESPD = false;
      this.isCreateResponse = true;
    }

    /* ===================== create forms in case of predefined criteria ================== */
    if (this.isCreateResponse) {

      this.getEoRelatedACriteria()
        .then(res => {

          if (this.isCreateResponse) {
            this.eoRelatedACriteria = res;
            console.log('This is create response');
          }


          this.eoRelatedACriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedACriteria);
          console.log(this.eoRelatedACriteriaForm);
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
          this.eoRelatedCCriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedCCriteria);
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
          this.eoRelatedDCriteriaForm = this.createEORelatedCriterionForm(this.eoRelatedDCriteria);
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
          this.exclusionACriteriaForm = this.createExclusionCriterionForm(this.exclusionACriteria);
          console.log(this.exclusionACriteriaForm);
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
          this.exclusionBCriteriaForm = this.createExclusionCriterionForm(this.exclusionBCriteria);
          console.log(this.exclusionBCriteriaForm);
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
          this.exclusionCCriteriaForm = this.createExclusionCriterionForm(this.exclusionCCriteria);
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
          this.exclusionDCriteriaForm = this.createExclusionCriterionForm(this.exclusionDCriteria);
          // console.log(res);
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
          this.selectionALLCriteriaForm = this.createSelectionCriterionForm(this.selectionALLCriteria);
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
          this.selectionACriteriaForm = this.createSelectionCriterionForm(this.selectionACriteria);
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
          this.selectionBCriteriaForm = this.createSelectionCriterionForm(this.selectionBCriteria);
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
          this.selectionCCriteriaForm = this.createSelectionCriterionForm(this.selectionCCriteria);
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
          this.selectionDCriteriaForm = this.createSelectionCriterionForm(this.selectionDCriteria);
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
          this.reductionCriteriaForm = this.createReductionCriterionForm(this.reductionCriteria);

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
    if (this.isCreateNewESPD) {

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


  /* =================================  Get from Codelists ===========================*/


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


  /* =================  Dynamic Forms =========================== */

  // createExclusionCriterionForm(criteria: ExclusionCriteria[]) {
  //   let group: any = {};
  //   criteria.forEach(cr => {
  //     group[cr.typeCode] = this.createCriterionFormGroup(cr);
  //     console.log(group[cr.typeCode]);
  //   });
  //   let fg = new FormGroup(group);
  //
  //   console.log(fg);
  //   return fg;
  // }

  createExclusionCriterionForm(criteria: ExclusionCriteria[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }

  createSelectionCriterionForm(criteria: SelectionCriteria[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }

  createReductionCriterionForm(criteria: ReductionCriterion[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }

  createEORelatedCriterionForm(criteria: EoRelatedCriterion[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }


  createCriterionFormGroup(cr: ExclusionCriteria) {
    let group: any = {};
    if (cr) {
      // console.log('In Criterion: ' + cr.typeCode);
      if (cr.requirementGroups != null || cr.requirementGroups != undefined) {
        group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      }
    }
    let fg = new FormGroup(group);
    // console.log(fg.getRawValue());
    return fg;

  }


  createFormGroups(reqGroups: RequirementGroup[]) {
    let group: any = {};
    reqGroups.forEach(rg => {
      group[rg.uuid] = this.toFormGroup(rg);
    });
    // console.log(group);
    let fg = new FormGroup(group);
    // console.log(fg);
    return fg;
  }


  toFormGroup(rg: RequirementGroup) {

    // let ID = 0;

    let group: any = {};
    if (rg) {
      // console.log('In Req Group: ' + rg.id);
      if (rg.requirements != undefined) {
        rg.requirements.forEach(r => {
          // ID++;
          // console.log(ID);
          // console.log('In Req: ' + r.uuid);
          if (r.response != null || r.response != undefined) {
            group[r.uuid] = new FormControl(r.response.description ||
              r.response.percentage || r.response.indicator || r.response.evidenceURL ||
              r.response.evidenceURLCode || r.response.countryCode ||
              r.response.period || r.response.quantity || r.response.year || '');

            if (r.response.date) {
              console.log(r.response.date);
              // console.log(typeof r.response.date);


              // let date = this.getDateJSObjectD(r.response.date);
              // let momentDate = moment(r.response.date).format('LLL');
              // console.log(momentDate);

              group[r.uuid] = new FormControl(r.response.date);
            }

            // TODO version period
            if (r.response.startDate) {
              group[r.uuid + 'startDate'] = new FormControl(r.response.startDate);
            }
            if (r.response.endDate) {
              group[r.uuid + 'endDate'] = new FormControl(r.response.endDate);
            }

            if (r.response.evidenceSuppliedId) {

              // TODO find evidence in EvidenceList object and import it
              const evi = this.evidenceList.find((ev, i) => {
                if (ev.id === r.response.evidenceSuppliedId) {
                  // this.evidenceList[i].description = 'test';
                  return true;
                }
              });
              console.log(evi);
              console.log(typeof evi);

              group[r.uuid + 'evidenceUrl'] = new FormControl(evi.evidenceURL);
              group[r.uuid + 'evidenceCode'] = new FormControl(evi.description);
              group[r.uuid + 'evidenceIssuer'] = new FormControl(evi.evidenceIssuer.name);
            }

            if (r.response.currency || r.response.amount) {
              group[r.uuid] = new FormControl(r.response.amount);
              if (r.response.currency !== null && r.response.currency !== undefined) {
                group[r.uuid + 'currency'] = new FormControl(r.response.currency);
              }
            }
            // in case of request import
            if (r.response.currency === null || r.response.amount === '0') {
              group[r.uuid + 'currency'] = new FormControl();
            }


            // console.log(r.response);
          } else {
            r.response = new RequirementResponse();
            group[r.uuid] = new FormControl(r.response.description || '');
            if (this.isEO) {
              group[r.uuid + 'currency'] = new FormControl();
              group[r.uuid + 'startDate'] = new FormControl();
              group[r.uuid + 'endDate'] = new FormControl();
              group[r.uuid + 'evidenceUrl'] = new FormControl();
              group[r.uuid + 'evidenceCode'] = new FormControl();
              group[r.uuid + 'evidenceIssuer'] = new FormControl();

            }

          }

          // console.log(group);
          // console.log(group[r.uuid]);
        });
      }
      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {
        rg.requirementGroups.forEach(rg => {
          // console.log('Req Group ' + rg.uuid);
          group[rg.uuid] = this.toFormGroup(rg);
        });
      }
    }
    let fg = new FormGroup(group);

    // console.log(fg.getRawValue());
    return fg;
  }

}
