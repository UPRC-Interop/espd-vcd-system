import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
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

  postFile(filesToUpload: File[]) {

    const formData: FormData = new FormData();
    for (let i = 0; i < filesToUpload.length; i++) {
      formData.append(`files[]`, filesToUpload[i], filesToUpload[i].name);
    }
    // console.log(formData);
    // const header = new HttpHeaders({'Content-Type':'application/xml; charset=utf-8'});
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/xml; charset=utf-8');
    return this.http.post<any>("http://localhost:8080/api/espd/v1/request", formData, {headers:header}).toPromise();

  }


}
