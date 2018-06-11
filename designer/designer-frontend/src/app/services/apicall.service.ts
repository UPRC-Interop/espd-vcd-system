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
// import {DataService} from '../services/data.service';

@Injectable()
export class ApicallService {

  /* ============ CODELISTS =============*/

  version: string = 'v1';
  // version: string = 'v2';
  // version: string;


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


  /* ==================== EO related criteria ========================= */

  getEO_RelatedCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/predefined/eo_related').toPromise();
  }

  getEO_RelatedACriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/predefined/eo_related_A').toPromise();
  }

  getEO_RelatedCCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/predefined/eo_related_B').toPromise();
  }

  getEO_RelatedDCriteria() {
    return this.http.get<EoRelatedCriterion[]>(environment.apiUrl + this.version + '/criteria/predefined/eo_related_C').toPromise();
  }

  /* =========================== Reduction of Candidates ================= */

  get_ReductionCriteria() {
    return this.http.get<ReductionCriterion[]>(environment.apiUrl + this.version + '/criteria/predefined/reduction').toPromise();
  }

  /* ============= EXCLUSION CRITERIA ===================*/
  getExclusionCriteria() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/exclusion').toPromise();
  }

  getExclusionCriteria_A() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/exclusion_a').toPromise();
  }

  getExclusionCriteria_B() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/exclusion_b').toPromise();
  }

  getExclusionCriteria_C() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/exclusion_c').toPromise();
  }

  getExclusionCriteria_D() {
    return this.http.get<ExclusionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/exclusion_d').toPromise();
  }

  /* ============= SELECTION CRITERIA ===================*/

  getSelectionCriteria() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/selection').toPromise();
  }

  getSelectionCriteria_A() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/selection_a').toPromise();
  }

  getSelectionCriteria_B() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/selection_b').toPromise();
  }

  getSelectionCriteria_C() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/selection_c').toPromise();
  }

  getSelectionCriteria_D() {
    return this.http.get<SelectionCriteria[]>(environment.apiUrl + this.version + '/criteria/predefined/selection_d').toPromise();
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
    return this.http.post<any>(environment.apiUrl + 'v1/espd/request', ESPDRequest, options).toPromise();
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
    return this.http.post<any>(environment.apiUrl + 'v1/espd/response', ESPDResponse, options).toPromise();
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
