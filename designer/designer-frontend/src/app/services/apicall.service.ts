import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import 'rxjs/add/operator/toPromise';
import {Country} from "../model/country.model";
import {ProcedureType} from "../model/procedureType.model";
import {ExclusionCriteria} from "../model/exclusionCriteria.model";
import {SelectionCriteria} from "../model/selectionCriteria.model";

@Injectable()
export class ApicallService {

  /* ============ CODELISTS =============*/

  constructor(private http:HttpClient) {}

  getCountryList(){
   return this.http.get<Country[]>("http://localhost:8080/api/codelists/v2/CountryIdentification").toPromise()
  }

  getProcedureType(){
    return this.http.get<ProcedureType[]>("http://localhost:8080/api/codelists/v2/ProcedureType").toPromise()
  }

  /* ============= EXCLUSION CRITERIA ===================*/
  getExclusionCriteria(){
    return this.http.get<ExclusionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/exclusion").toPromise()
  }

  getExclusionCriteria_A(){
    return this.http.get<ExclusionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/exclusion/conviction").toPromise()
  }

  getExclusionCriteria_B(){
    return this.http.get<ExclusionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/exclusion/contribution").toPromise()
  }

  getExclusionCriteria_C(){
    return this.http.get<ExclusionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/exclusion/insolvencyConflictsMisconduct").toPromise()
  }

  getExclusionCriteria_D(){
    return this.http.get<ExclusionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/exclusion/national").toPromise()
  }

  /* ============= SELECTION CRITERIA ===================*/

  getSelectionCriteria(){
    return this.http.get<SelectionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/selection").toPromise()
  }

  getSelectionCriteria_A(){
    return this.http.get<SelectionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/selection/suitability").toPromise()
  }

  getSelectionCriteria_B(){
    return this.http.get<SelectionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/selection/economic").toPromise()
  }

  getSelectionCriteria_C(){
    return this.http.get<SelectionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/selection/technical").toPromise()
  }
  getSelectionCriteria_D(){
    return this.http.get<SelectionCriteria[]>("http://localhost:8080/api/criteriaList/predefined/selection/quality").toPromise()
  }


  postFile(filesToUpload: File[]) {

    const formData: FormData = new FormData();
    for (let i = 0; i < filesToUpload.length; i++) {
      formData.append(`files[]`, filesToUpload[i], filesToUpload[i].name);
    }
    // console.log(formData);
    // const header = new HttpHeaders({'Content-Type':'application/xml; charset=utf-8'});
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/xml; charset=utf-8');
    return this.http.post<any>("http://localhost:8080/api/espd/v1/request", formData).toPromise();

  }


}
