import { Injectable } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {Country} from "../model/country.model"

@Injectable()
export class DataService {

  countries:Country[]=null;

  constructor(private APIService:ApicallService) { }


  getCountries():Country[]{
    this.APIService.requestCountryList()
      .then(res=>{
        this.countries=res;
        // return this.countries;
       if(this.countries) {
         return this.countries;
       }
      })
      .catch(err=>{console.log(err); return this.countries;});

  }
}
