import { Injectable } from '@angular/core';
import {ApicallService} from "../services/apicall.service";

@Injectable()
export class DataService {

  countries:Object=null;

  constructor(private APIService:ApicallService) { }


  getCountries():any {
    this.APIService.requestCountryList()
      .then(res=>{
        this.countries=res;
       if(this.countries) {
         return this.countries;
       }
      })
      .catch(err=>{console.log(err)});

  }
}
