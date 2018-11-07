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
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import 'rxjs/add/operator/toPromise';
import {Country} from '../model/country.model';
import {ProcedureType} from '../model/procedureType.model';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {ESPDRequest} from '../model/ESPDRequest.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {Currency} from '../model/currency.model';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {ESPDResponse} from '../model/ESPDResponse.model';
import {environment} from '../../environments/environment';
import {Language} from '../model/language.model';
import {UtilitiesService} from './utilities.service';
import {EoIDType} from '../model/eoIDType.model';
import {EvaluationMethodType} from '../model/evaluationMethodType.model';
import {ProjectType} from '../model/projectType.model';
import {BidType} from '../model/bidType.model';
import {WeightingType} from '../model/weightingType.model';
import {EoRoleType} from '../model/eoRoleType.model';
import {FinancialRatioType} from '../model/financialRatioType.model';

// import {DataService} from '../services/data.service';

@Injectable()
export class ApicallService {

  /* ============ CODELISTS =============*/

  // version: string = 'v1';
  // version: string = 'v2';
  version: string;


  constructor(private http: HttpClient, public utilities: UtilitiesService) {
  }

  getCountryList() {
    return this.http.get<Country[]>(environment.apiUrl + 'v2/codelists/CountryIdentification').toPromise();
  }

  getCurr() {
    return this.http.get<Currency[]>(environment.apiUrl + 'v1/codelists/Currency').toPromise();
  }

  getLangs() {
    return this.http.get<Language[]>(environment.apiUrl + 'v2/codelists/LanguageCodeEU').toPromise();
  }

  /* SELF-CONTAINED: Codelists*/
  get_eoIDTypes() {
    return this.http.get<EoIDType[]>(environment.apiUrl + 'v2/codelists/EOIDType').toPromise();
  }

  get_EvaluationMethodType() {
    return this.http.get<EvaluationMethodType[]>(environment.apiUrl + 'v2/codelists/EvaluationMethodType').toPromise();
  }

  getProcedureType() {
    return this.http.get<ProcedureType[]>(environment.apiUrl + 'v2/codelists/ProcedureType').toPromise();
  }

  get_ProjectType() {
    return this.http.get<ProjectType[]>(environment.apiUrl + 'v2/codelists/ProjectType').toPromise();
  }

  get_BidType() {
    return this.http.get<BidType[]>(environment.apiUrl + 'v2/codelists/BidType').toPromise();
  }

  get_WeightingType() {
    return this.http.get<WeightingType[]>(environment.apiUrl + 'v2/codelists/WeightingType').toPromise();
  }

  get_eoRoleType() {
    return this.http.get<EoRoleType[]>(environment.apiUrl + 'v2/codelists/EORoleType').toPromise();
  }

  get_financialRatioType() {
    return this.http.get<FinancialRatioType[]>(environment.apiUrl + 'v2/codelists/FinancialRatioType').toPromise();
  }


  /* ==================== EO related criteria ========================= */

  getEO_RelatedCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/eo_related').toPromise();
  }

  getEO_RelatedACriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/eo_related_A').toPromise();
  }

  getEO_RelatedCCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/eo_related_B').toPromise();
  }

  getEO_RelatedDCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/eo_related_C').toPromise();
  }

  getEO_LotCriterion() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/eo_lots').toPromise();
  }

  /* SELF-CONTAINED: CA related Criterion - CA LOTS */

  getCA_RelatedCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/other_ca').toPromise();
  }

  /* =========================== Reduction of Candidates ================= */

  get_ReductionCriteria() {
    return this.http.get<ReductionCriterion[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/reduction').toPromise();
  }

  /* ============= EXCLUSION CRITERIA ===================*/
  getExclusionCriteria() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/exclusion').toPromise();
  }

  getExclusionCriteria_A() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/exclusion_a').toPromise();
  }

  getExclusionCriteria_B() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/exclusion_b').toPromise();
  }

  getExclusionCriteria_C() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/exclusion_c').toPromise();
  }

  getExclusionCriteria_D() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/exclusion_d').toPromise();
  }

  /* ============= SELECTION CRITERIA ===================*/

  getSelectionCriteria() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/selection').toPromise();
  }

  getSelectionCriteria_A() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/selection_a').toPromise();
  }

  getSelectionCriteria_B() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/selection_b').toPromise();
  }

  getSelectionCriteria_C() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/selection_c').toPromise();
  }

  getSelectionCriteria_D() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/' + this.utilities.qualificationApplicationType +
      '/criteria/selection_d').toPromise();
  }

  /* ============ UPLOAD XML GET JSON ================= */
  postFile(filesToUpload: File[]) {

    const formData: FormData = new FormData();
    for (let i = 0; i < filesToUpload.length; i++) {
      formData.append(`files[]`, filesToUpload[i], filesToUpload[i].name);
    }
    // console.log(formData);
    // const header = new HttpHeaders({'Content-Type':'application/xml; charset=utf-8'});
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/xml; charset=utf-8');
    // return this.http.post<ESPDRequest>(environment.apiUrl + '/importESPD/request', formData).toPromise();
    return this.http.post<ESPDRequest>(environment.apiUrl + '/importESPD/request', formData).toPromise();

  }

  postFileResponse(filesToUpload: File[]) {

    const formData: FormData = new FormData();
    for (let i = 0; i < filesToUpload.length; i++) {
      formData.append(`files[]`, filesToUpload[i], filesToUpload[i].name);
    }
    // console.log(formData);
    // const header = new HttpHeaders({'Content-Type':'application/xml; charset=utf-8'});
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/xml; charset=utf-8');
    return this.http.post<ESPDResponse>(environment.apiUrl + '/importESPD/response', formData).toPromise();

  }

  /* ================= UPLOAD JSON GET XML Request ================================= */
  getXMLRequest(ESPDRequest: string) {

    console.log(ESPDRequest);
    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');

    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response'
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(environment.apiUrl + this.version + '/espd/request', ESPDRequest, options).toPromise();
  }

  getXMLRequestV2(ESPDRequest: string) {

    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');

    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response'
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(environment.apiUrl + 'v2/espd/request', ESPDRequest, options).toPromise();
  }

  getXMLResponse(ESPDResponse: string) {

    console.log(ESPDResponse);

    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');

    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response'
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(environment.apiUrl + this.version + '/espd/response', ESPDResponse, options).toPromise();
  }

  // getXMLResponseV2(ESPDResponse: string) {
  //
  //   let header = new HttpHeaders();
  //   let _header = header.append('Content-Type', 'application/json; charset=utf-8');
  //
  //   let options: Object = {
  //     headers: _header,
  //     responseType: 'blob' as 'blob',
  //     observe: 'response' as 'response'
  //   };
  //
  //   // headers = header.append('Content-Type', 'application/json; charset=utf-8');
  //   return this.http.post<any>(environment.apiUrl + 'v2/espd/response', ESPDResponse, options).toPromise();
  // }


}
