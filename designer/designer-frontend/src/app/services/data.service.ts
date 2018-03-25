import { Injectable } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {Country} from "../model/country.model"
import {ProcedureType} from "../model/procedureType.model";
import {ExclusionCriteria} from "../model/exclusionCriteria.model";
import {SelectionCriteria} from "../model/selectionCriteria.model";
import {NgForm} from "@angular/forms/forms";
import {Cadetails} from "../model/caDetails.model";
import {ESPDRequest} from "../model/ESPDRequest.model";
import {FullCriterion} from "../model/fullCriterion.model";

@Injectable()
export class DataService {

  countries:Country[]=null;
  procedureTypes:ProcedureType[]=null;
  exclusionACriteria:ExclusionCriteria[]=null;
  exclusionBCriteria:ExclusionCriteria[]=null;
  exclusionCCriteria:ExclusionCriteria[]=null;
  exclusionDCriteria:ExclusionCriteria[]=null;
  selectionACriteria:SelectionCriteria[]=null;
  selectionBCriteria:SelectionCriteria[]=null;
  selectionCCriteria:SelectionCriteria[]=null;
  selectionDCriteria:SelectionCriteria[]=null;
  selectionALLCriteria:SelectionCriteria[]=null;
  fullCriterionList:FullCriterion[]=null;

  CADetails:Cadetails = new Cadetails();
  espdRequest:ESPDRequest;
  espdRequestjson:string;

  isCA:boolean=false;
  isEO:boolean=false;
  receivedNoticeNumber:string;
  selectedCountry:string="";

  constructor(private APIService:ApicallService) {

  }

  makeFullCriterionList(exclusionACriteria:ExclusionCriteria[],
                        exclusionBCriteria:ExclusionCriteria[],
                        exclusionCCriteria:ExclusionCriteria[],
                        exclusionDCriteria:ExclusionCriteria[],
                        isSatisfiedALL:boolean,
                        selectionALLCriteria?:SelectionCriteria[],
                        selectionACriteria?:SelectionCriteria[],
                        selectionBCriteria?:SelectionCriteria[],
                        selectionCCriteria?:SelectionCriteria[],
                        selectionDCriteria?:SelectionCriteria[]):FullCriterion[]{
    // let exAString:string= JSON.stringify(exclusionACriteria);
    // let exBString:string= JSON.stringify(exclusionBCriteria);

    if(isSatisfiedALL) {
      var combineJsonArray = [...exclusionACriteria ,
        ...exclusionBCriteria,
        ...exclusionCCriteria,
        ...exclusionDCriteria,
        ...selectionALLCriteria];
      console.dir(combineJsonArray);
      return combineJsonArray;

    } else {
      var combineJsonArray = [
        ...exclusionACriteria ,
        ...exclusionBCriteria,
        ...exclusionCCriteria,
        ...exclusionDCriteria,
        ...selectionACriteria,
        ...selectionBCriteria,
        ...selectionCCriteria,
        ...selectionDCriteria];
      console.dir(combineJsonArray);
      return combineJsonArray;
    }




  }

  createESPDRequest():ESPDRequest{

      this.espdRequest= new ESPDRequest(this.CADetails,this.fullCriterionList);
      console.log(this.espdRequest);
      return this.espdRequest;

  }

  exclusionSubmit(exclusionCriteriaA:ExclusionCriteria[],
                  exclusionCriteriaB:ExclusionCriteria[],
                  exclusionCriteriaC:ExclusionCriteria[],
                  exclusionCriteriaD:ExclusionCriteria[]){
    this.exclusionACriteria=exclusionCriteriaA;
    this.exclusionBCriteria=exclusionCriteriaB;
    this.exclusionCCriteria=exclusionCriteriaC;
    this.exclusionDCriteria=exclusionCriteriaD;

  }

  selectionSubmit(selectionCriteriaA:SelectionCriteria[],
                  selectionCriteriaB:SelectionCriteria[],
                  selectionCriteriaC:SelectionCriteria[],
                  selectionCriteriaD:SelectionCriteria[],
                  isSatisfiedALL:boolean){
      this.selectionACriteria=selectionCriteriaA;
      this.selectionBCriteria=selectionCriteriaB;
      this.selectionCCriteria=selectionCriteriaC;
      this.selectionDCriteria=selectionCriteriaD;

    //create ESPDRequest
    // console.dir(JSON.stringify(this.createESPDRequest(isSatisfiedALL)));
    this.fullCriterionList=this.makeFullCriterionList(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria,
      isSatisfiedALL,
      this.selectionALLCriteria,
      this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria);

    //apicall service post
    this.APIService.getXMLRequest(JSON.stringify(this.createESPDRequest()))
      .then(res=>{console.log(res);})
      .catch(err=>{console.log(err);});

  }


  startCA(form:NgForm){
    console.log(form);
    console.log(form.value);

    if(form.value.chooseRole=="CA") {
        this.isCA=true;
        this.receivedNoticeNumber=form.value.noticeNumber;
        if(form.value.CACountry!="") {
          this.selectedCountry=form.value.CACountry;
        }

    }
  }


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
    if(this.exclusionACriteria!= null) {
      return Promise.resolve(this.exclusionACriteria);
    } else {
      return this.APIService.getExclusionCriteria_A()
        .then( res => {
          this.exclusionACriteria = res;
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
    if(this.exclusionBCriteria!= null) {
      return Promise.resolve(this.exclusionBCriteria);
    } else {
      return this.APIService.getExclusionCriteria_B()
        .then( res => {
          this.exclusionBCriteria = res;
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
    if(this.exclusionCCriteria!= null) {
      return Promise.resolve(this.exclusionCCriteria);
    } else {
      return this.APIService.getExclusionCriteria_C()
        .then( res => {
          this.exclusionCCriteria = res;
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
    if(this.exclusionDCriteria!= null) {
      return Promise.resolve(this.exclusionDCriteria);
    } else {
      return this.APIService.getExclusionCriteria_D()
        .then( res => {
          this.exclusionDCriteria = res;
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

  getSelectionALLCriteria():Promise<SelectionCriteria[]>{
    if(this.selectionALLCriteria!= null) {
      return Promise.resolve(this.selectionALLCriteria);
    } else {
      return this.APIService.getSelectionCriteria()
        .then( res => {
          this.selectionALLCriteria = res;
          return Promise.resolve(res);
        })
        .catch(err =>
        {
          console.log(err);
          return Promise.reject(err);
        });
    }
  }


  getSelectionACriteria():Promise<SelectionCriteria[]>{
    if(this.selectionACriteria!= null) {
      return Promise.resolve(this.selectionACriteria);
    } else {
      return this.APIService.getSelectionCriteria_A()
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

  getSelectionBCriteria():Promise<SelectionCriteria[]>{
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

  getSelectionCCriteria():Promise<SelectionCriteria[]>{
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

  getSelectionDCriteria():Promise<SelectionCriteria[]>{
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