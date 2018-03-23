import { Injectable } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {Country} from "../model/country.model"
import {ProcedureType} from "../model/procedureType.model";
import {ExclusionCriteria} from "../model/exclusionCriteria.model";

@Injectable()
export class DataService {

  countries:Country[]=null;
  procedureTypes:ProcedureType[]=null;
  exlusionACriteria:ExclusionCriteria[]=null;
  exlusionBCriteria:ExclusionCriteria[]=null;
  exlusionCCriteria:ExclusionCriteria[]=null;
  exlusionDCriteria:ExclusionCriteria[]=null;
  selectionACriteria:ExclusionCriteria[]=null;
  selectionBCriteria:ExclusionCriteria[]=null;
  selectionCCriteria:ExclusionCriteria[]=null;
  selectionDCriteria:ExclusionCriteria[]=null;

  constructor(private APIService:ApicallService) { }


  getCountries():Promise<Country[]>{
    if(this.countries!= null) {
      return Promise.resolve(this.countries);
    } else {
      return this.APIService.getCountryList()
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

  getProcedureTypes():Promise<ProcedureType[]>{
    if(this.procedureTypes!= null) {
      return Promise.resolve(this.procedureTypes);
    } else {
      return this.APIService.getProcedureType()
        .then( res => {
          this.procedureTypes = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  /* =================== Exclusion Criteria ========================== */

  getExclusionACriteria():Promise<ExclusionCriteria[]>{
    if(this.exlusionACriteria!= null) {
      return Promise.resolve(this.exlusionACriteria);
    } else {
      return this.APIService.getExclusionCriteria_A()
        .then( res => {
          this.exlusionACriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getExclusionBCriteria():Promise<ExclusionCriteria[]>{
    if(this.exlusionBCriteria!= null) {
      return Promise.resolve(this.exlusionBCriteria);
    } else {
      return this.APIService.getExclusionCriteria_B()
        .then( res => {
          this.exlusionBCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getExclusionCCriteria():Promise<ExclusionCriteria[]>{
    if(this.exlusionCCriteria!= null) {
      return Promise.resolve(this.exlusionCCriteria);
    } else {
      return this.APIService.getExclusionCriteria_C()
        .then( res => {
          this.exlusionCCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getExclusionDCriteria():Promise<ExclusionCriteria[]>{
    if(this.exlusionDCriteria!= null) {
      return Promise.resolve(this.exlusionDCriteria);
    } else {
      return this.APIService.getExclusionCriteria_D()
        .then( res => {
          this.exlusionDCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  /* =================== Selection Criteria ========================== */

  getSelectionACriteria():Promise<ExclusionCriteria[]>{
    if(this.selectionACriteria!= null) {
      return Promise.resolve(this.selectionACriteria);
    } else {
      return this.APIService.getSelectionCriteria()
        .then( res => {
          this.selectionACriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getSelectionBCriteria():Promise<ExclusionCriteria[]>{
    if(this.selectionBCriteria!= null) {
      return Promise.resolve(this.selectionBCriteria);
    } else {
      return this.APIService.getSelectionCriteria_B()
        .then( res => {
          this.selectionBCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getSelectionCCriteria():Promise<ExclusionCriteria[]>{
    if(this.selectionCCriteria!= null) {
      return Promise.resolve(this.selectionCCriteria);
    } else {
      return this.APIService.getSelectionCriteria_C()
        .then( res => {
          this.selectionCCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }

  getSelectionDCriteria():Promise<ExclusionCriteria[]>{
    if(this.selectionDCriteria!= null) {
      return Promise.resolve(this.selectionDCriteria);
    } else {
      return this.APIService.getSelectionCriteria_D()
        .then( res => {
          this.selectionDCriteria = res;
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
