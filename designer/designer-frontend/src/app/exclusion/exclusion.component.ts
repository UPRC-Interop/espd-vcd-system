import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {ExclusionCriteria} from "../model/exclusionCriteria.model";
import {LegislationReference} from "../model/legislationReference.model";
import {DataService} from "../services/data.service";
import {NgForm} from "@angular/forms/forms";

@Component({
  selector: 'app-exclusion',
  templateUrl: './exclusion.component.html',
  styleUrls: ['./exclusion.component.css']
})
export class ExclusionComponent implements OnInit {

  exclusionACriteria:ExclusionCriteria[]=null;
  exclusionBCriteria:ExclusionCriteria[]=null;
  exclusionCCriteria:ExclusionCriteria[]=null;
  exclusionDCriteria:ExclusionCriteria[]=null;

  constructor(private dataService:DataService) { }

  ngOnInit() {
    this.dataService.getExclusionACriteria()
      .then(res=>{
        //TODO check if exclusionACriteria is from file when posted. dataService.exclusionACriteria=res.... bind it to service in the view
        this.exclusionACriteria=res;
        // console.log("This is exclusionACriteria: ");
        // console.log(this.exclusionACriteria);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionBCriteria()
      .then(res=>{
        this.exclusionBCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionCCriteria()
      .then(res=>{
        this.exclusionCCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionDCriteria()
      .then(res=>{
        this.exclusionDCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});


  }


  onExclusionSubmit(form:NgForm){
    console.log(form.value);
      this.dataService.exclusionSubmit(this.exclusionACriteria,
                                      this.exclusionBCriteria,
                                      this.exclusionCCriteria,
                                      this.exclusionDCriteria);
  }

}
