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

// import {DataService} from '../services/data.service';

@Injectable()
export class ApicallService {

  /* ============ CODELISTS =============*/

  // version: string = 'v1';
  // version: string = 'v2';
  version: string;


  constructor(private http: HttpClient) {
  }

  getCountryList() {
    return this.http.get<Country[]>(environment.apiUrl + 'v2/codelists/CountryIdentification').toPromise();
  }

  getProcedureType() {
    return this.http.get<ProcedureType[]>(environment.apiUrl + 'v2/codelists/ProcedureType').toPromise();
  }

  getCurr() {
    return this.http.get<Currency[]>(environment.apiUrl + 'v1/codelists/Currency').toPromise();
  }

  getLangs() {
    return this.http.get<Language[]>(environment.apiUrl + 'v2/codelists/LanguageCodeEU').toPromise();
  }


  /* ==================== EO related criteria ========================= */

  getEO_RelatedCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/eo_related').toPromise();
  }

  getEO_RelatedACriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/eo_related_A').toPromise();
  }

  getEO_RelatedCCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/eo_related_B').toPromise();
  }

  getEO_RelatedDCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/eo_related_C').toPromise();
  }

  /* =========================== Reduction of Candidates ================= */

  get_ReductionCriteria() {
    return this.http.get<ReductionCriterion[]>(environment.apiUrl + this.version + '/criteria/reduction').toPromise();
  }

  /* ============= EXCLUSION CRITERIA ===================*/
  getExclusionCriteria() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/exclusion').toPromise();
  }

  getExclusionCriteria_A() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/exclusion_a').toPromise();
  }

  getExclusionCriteria_B() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/exclusion_b').toPromise();
  }

  getExclusionCriteria_C() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/exclusion_c').toPromise();
  }

  getExclusionCriteria_D() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/exclusion_d').toPromise();
  }

  /* ============= SELECTION CRITERIA ===================*/

  getSelectionCriteria() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/selection').toPromise();
  }

  getSelectionCriteria_A() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/selection_a').toPromise();
  }

  getSelectionCriteria_B() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/selection_b').toPromise();
  }

  getSelectionCriteria_C() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/selection_c').toPromise();
  }

  getSelectionCriteria_D() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/selection_d').toPromise();
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
  getXMLRequest(ESPDRequest: string, language: string) {
    return this.getRequestExport(ESPDRequest, '/espd/request/xml', language);
  }

  getHTMLRequest(ESPDRequest: string, language: string) {
    return this.getRequestExport(ESPDRequest, '/espd/request/html', language);
  }

  getPDFRequest(ESPDRequest: string, language: string) {
    return this.getRequestExport(ESPDRequest, '/espd/request/pdf', language);
  }

  private getRequestExport(ESPDRequest: string, endpoint: string, language: string) {
    console.log(ESPDRequest);
    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');
    let params = new HttpParams().set('language', language);
    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response',
      params: params
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');

    return this.http.post<any>(environment.apiUrl + this.version + endpoint, ESPDRequest, options).toPromise();
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

  getHTMLResponse(ESPDResponse: string, language: string) {
    return this.getESPDResponseDocument(ESPDResponse, '/espd/response/html', language);
  }

  getPDFResponse(ESPDResponse: string, language: string) {
    return this.getESPDResponseDocument(ESPDResponse, '/espd/response/pdf', language);
  }

  getXMLResponse(ESPDResponse: string, language: string) {
    return this.getESPDResponseDocument(ESPDResponse, '/espd/response/xml', language);
  }

  private getESPDResponseDocument(ESPDResponse: string, endpoint: string, language: string) {
    console.log(ESPDResponse);

    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');

    let params = new HttpParams().set('language', language);
    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response',
      params: params
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(environment.apiUrl + this.version + endpoint, ESPDResponse, options).toPromise();
  }

  getXMLResponseV2(ESPDResponse: string) {

    let header = new HttpHeaders();
    let _header = header.append('Content-Type', 'application/json; charset=utf-8');

    let options: Object = {
      headers: _header,
      responseType: 'blob' as 'blob',
      observe: 'response' as 'response'
    };

    // headers = header.append('Content-Type', 'application/json; charset=utf-8');
    return this.http.post<any>(environment.apiUrl + 'v2/espd/response', ESPDResponse, options).toPromise();
  }


}
