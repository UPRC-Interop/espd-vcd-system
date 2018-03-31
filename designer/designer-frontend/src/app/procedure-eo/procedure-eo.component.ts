import { Component, OnInit } from '@angular/core';
import {DataService} from "../services/data.service";
import {ProcedureType} from "../model/procedureType.model";
import {Country} from "../model/country.model";
import {FormGroup, NgForm} from "@angular/forms";

@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit {

  public EOForm:FormGroup;

  countries:Country[]=null;
  procedureTypes:ProcedureType[]=null;

  constructor(public dataService:DataService) { }

  ngOnInit() {
    this.dataService.getCountries()
      .then(res=>{
        this.countries=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getProcedureTypes()
      .then(res=>{
        this.procedureTypes=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});
  }

  onProcedureEOSubmit(form:NgForm, eoForm:FormGroup){
    // console.log(form.value);
    console.log(eoForm.value);
    this.dataService.CADetails.cacountry=form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber=form.value.receivedNoticeNumber;
    console.log("THIS IS EO");
    // console.log(this.dataService.CADetails);
  }

}
