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
  blob = null;

  CADetails: Cadetails = new Cadetails();
  EODetails: EoDetails = new EoDetails();
  espdRequest: ESPDRequest;
  espdRequestjson: string;

  isCA: boolean = false;
  isEO: boolean = false;
  receivedNoticeNumber: string;
  selectedCountry: string = '';
  selectedEOCountry: string = '';

  constructor(private APIService: ApicallService) {

  }

  /* ================= Merge criterions into one fullcriterion list ================*/

  makeFullCriterionList(exclusionACriteria: ExclusionCriteria[],
                        exclusionBCriteria: ExclusionCriteria[],
                        exclusionCCriteria: ExclusionCriteria[],
                        exclusionDCriteria: ExclusionCriteria[],
                        isSatisfiedALL: boolean,
                        selectionALLCriteria?: SelectionCriteria[],
                        selectionACriteria?: SelectionCriteria[],
                        selectionBCriteria?: SelectionCriteria[],
                        selectionCCriteria?: SelectionCriteria[],
                        selectionDCriteria?: SelectionCriteria[]): FullCriterion[] {
    // let exAString:string= JSON.stringify(exclusionACriteria);
    // let exBString:string= JSON.stringify(exclusionBCriteria);

    if (isSatisfiedALL) {
      var combineJsonArray = [...exclusionACriteria,
        ...exclusionBCriteria,
        ...exclusionCCriteria,
        ...exclusionDCriteria,
        ...selectionALLCriteria];
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
        ...selectionDCriteria];
      // console.dir(combineJsonArray);
      return combineJsonArray;
    }
  }


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


  filterSelectionCriteria(regex: RegExp, criteriaList: FullCriterion[]): SelectionCriteria[] {
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
    this.fullCriterionList = this.makeFullCriterionList(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria,
      isSatisfiedALL,
      this.selectionALLCriteria,
      this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria);

    // apicall service post
    this.APIService.getXMLRequest(JSON.stringify(this.createESPDRequest()))
      .then(res => {
        console.log(res);
        this.createFile(res);
      })
      .catch(err => {
        console.log(err);
      });

  }

  /* ================================== EXPORT FILES ============================= */


  createFile(response) {
    // const contentDispositionHeader: string = response.headers.get('Content-Disposition');
    // const parts: string[] = contentDispositionHeader.split(';');
    // const filename = parts[1].split('=')[1];

    // const filename:string = "espd-request";
    this.blob = new Blob([response.body], {type: 'text/xml'});
    // console.log(this.blob);
    // saveAs(blob, filename);
  }

  saveFile(blob) {

    // TODO if isCA ->espd-request.xml, if isEO -> espd-response.xml
    const filename: string = 'espd-request';
    saveAs(blob, filename);
  }


  /* ================================= CA REUSE ESPD REQUEST ====================== */

  CAReuseESPD(filesToUpload: File[], form: NgForm) {
    // TODO rename to ReuseESPD, if isCA... if isEO... etc.
    if (filesToUpload.length > 0) {
      this.APIService.postFile(filesToUpload)
        .then(res => {
          // res.cadetails=this.CADetails;
          // console.log(res.fullCriterionList);
          // console.log(res.cadetails);
          this.CADetails = res.cadetails;
          this.selectedCountry = this.CADetails.cacountry;


          this.exclusionACriteria = this.filterExclusionCriteria(this.EXCLUSION_CONVICTION_REGEXP, res.fullCriterionList);
          this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_CONTRIBUTION_REGEXP, res.fullCriterionList);
          this.exclusionCCriteria = this.filterExclusionCriteria(this.EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP, res.fullCriterionList);
          this.exclusionBCriteria = this.filterExclusionCriteria(this.EXCLUSION_NATIONAL_REGEXP, res.fullCriterionList);

          this.selectionACriteria = this.filterSelectionCriteria(this.SELECTION_SUITABILITY_REGEXP, res.fullCriterionList);
          this.selectionBCriteria = this.filterSelectionCriteria(this.SELECTION_ECONOMIC_REGEXP, res.fullCriterionList);
          this.selectionCCriteria = this.filterSelectionCriteria(this.SELECTION_TECHNICAL_REGEXP, res.fullCriterionList);
          this.selectionBCriteria = this.filterSelectionCriteria(this.SELECTION_CERTIFICATES_REGEXP, res.fullCriterionList);
          console.log(this.exclusionACriteria);
          console.log(this.selectionACriteria);
          console.log(res);


        })
        .catch(err => err);
    }

    if (form.value.chooseRole == 'CA') {
      this.isCA = true;
      this.receivedNoticeNumber = form.value.noticeNumber;
    }

  }

  startESPD(form: NgForm) {
    // console.log(form);
    // console.log(form.value);

    if (form.value.chooseRole == 'CA') {
      this.isCA = true;
      this.isEO = false;
      this.receivedNoticeNumber = form.value.noticeNumber;
      if (form.value.CACountry != '') {
        this.selectedCountry = form.value.CACountry;
      }
    }

    if (form.value.chooseRole == 'EO') {
      this.isEO = true;
      this.isCA = false;
      if (form.value.EOCountry != '') {
        this.selectedEOCountry = form.value.EOCountry;

      }
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
          return Promise.reject(err);
        });
    }
  }



  /* ========================================EO related Criteria ========================= */

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
      group[cr.typeCode] = this.createFormGroups(cr.requirementGroups);
      console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    console.log(fg);
    return fg;
  }

  createSelectionCriterionForm(criteria: SelectionCriteria[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.typeCode] = this.createFormGroups(cr.requirementGroups);
      console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    console.log(fg);
    return fg;
  }



  createCriterionFormGroup(cr: ExclusionCriteria) {
    let group: any = {};
    if (cr) {
      console.log('In Criterion: ' + cr.typeCode);
      if (cr.requirementGroups != null || cr.requirementGroups != undefined) {
        group[cr.typeCode] = this.createFormGroups(cr.requirementGroups);
      }
    }
    let fg = new FormGroup(group);
    console.log(fg.getRawValue());
    return fg;

  }


  createFormGroups(reqGroups: RequirementGroup[]) {
    let group: any = {};
    reqGroups.forEach(rg => {
      group[rg.id] = this.toFormGroup(rg);
    });
    // console.log(group);
    let fg = new FormGroup(group);
    console.log(fg);
    return fg;
  }


  toFormGroup(rg: RequirementGroup) {
    let group: any = {};
    if (rg) {
      console.log('In Req Group: ' + rg.id);
      if (rg.requirements != undefined) {
        rg.requirements.forEach(r => {
          console.log('In Req: ' + r.id);
          r.response = new RequirementResponse();
          group[r.id] = new FormControl(r.response.description || '');
          // console.log(group);
          // console.log(group[r.id]);
        });
      }
      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {
        rg.requirementGroups.forEach(rg => {
          console.log('Req Group ' + rg.id);
          group[rg.id] = this.toFormGroup(rg);
        });
      }
    }
    let fg = new FormGroup(group);
    console.log(fg.getRawValue());
    return fg;
  }

}
