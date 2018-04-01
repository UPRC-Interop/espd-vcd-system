import {Component, OnChanges, OnInit, SimpleChange, SimpleChanges} from '@angular/core';
import {DataService} from "../services/data.service";
import {ProcedureType} from "../model/procedureType.model";
import {Country} from "../model/country.model";
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";

@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit, OnChanges {

  public EOForm:FormGroup;

  countries:Country[]=null;
  procedureTypes:ProcedureType[]=null;

  constructor(public dataService:DataService) { }

  ngOnInit() {



    this.EOForm = new FormGroup({
      "name": new FormControl(this.dataService.EODetails.name),
      "smeIndicator": new FormControl(false),
      "postalAddress":new FormGroup({
        "addressLine1":new FormControl(),
        "postCode":new FormControl(),
        "city":new FormControl(),
        "countryCode":new FormControl(this.dataService.selectedEOCountry),
      }),
      "contactingDetails": new FormGroup({
        "contactPointName":new FormControl(),
        "emailAddress":new FormControl(),
        "telephoneNumber":new FormControl(),
      }),
      "id":new FormControl(),
      "websiteURI": new FormControl()

    });


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

  ngOnChanges(changes:SimpleChanges){
    this.EOForm.patchValue({
      "name": this.dataService.selectedEOCountry
    });
  }


  onProcedureEOSubmit(form:NgForm, eoForm:FormGroup){
    // console.log(form.value);
    console.log(eoForm.value);
    this.dataService.CADetails.cacountry=form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber=form.value.receivedNoticeNumber;

    //TODO put form values to dataService
    console.log(this.dataService.selectedEOCountry);
    // console.log(this.dataService.CADetails);
  }

}
