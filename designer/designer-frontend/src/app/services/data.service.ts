import { Injectable } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {Country} from "../model/country.model"

@Injectable()
export class DataService {

  countries:Country[]=null;

  constructor(private APIService:ApicallService) { }


  getCountries():Promise<Country[]>{
    if(this.countries!= null) {
      return Promise.resolve(this.countries);
    } else {
      return this.APIService.requestCountryList()
        .then( res => {
          this.countries = res;
          return Promise.resolve(res);
        })
        .catch(err =>
         {
           console.log(err);
           return Promise.reject(err);
         });
    }
  }
}
