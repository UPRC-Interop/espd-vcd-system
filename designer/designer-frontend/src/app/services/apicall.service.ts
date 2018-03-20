import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import 'rxjs/add/operator/toPromise';

@Injectable()
export class ApicallService {

  constructor(private http:HttpClient) {}

  requestCountryList(){
   return this.http.get<any>("http://localhost:8080/api/codelists/v2/countryID").toPromise()
  }

  getExclusionCriteria(){
    return this.http.get<any>("http://localhost:8080/api/criteriaList/predefined/exclusion").toPromise()
  }
  getSelectionCriteria(){
    return this.http.get<any>("http://localhost:8080/api/criteriaList/predefined/selection").toPromise()
  }


}
