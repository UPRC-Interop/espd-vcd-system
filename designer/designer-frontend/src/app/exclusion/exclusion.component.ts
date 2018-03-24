import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";
import {ExclusionCriteria} from "../model/exclusionCriteria.model";
import {LegislationReference} from "../model/legislationReference.model";
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-exclusion',
  templateUrl: './exclusion.component.html',
  styleUrls: ['./exclusion.component.css']
})
export class ExclusionComponent implements OnInit {

  exlusionACriteria:ExclusionCriteria[]=null;
  exlusionBCriteria:ExclusionCriteria[]=null;
  exlusionCCriteria:ExclusionCriteria[]=null;
  exlusionDCriteria:ExclusionCriteria[]=null;

  constructor(private dataService:DataService) { }

  ngOnInit() {
    this.dataService.getExclusionACriteria()
      .then(res=>{
        this.exlusionACriteria=res;
        // console.log("This is exclusionACriteria: ");
        // console.log(this.exlusionACriteria);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionBCriteria()
      .then(res=>{
        this.exlusionBCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionCCriteria()
      .then(res=>{
        this.exlusionCCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getExclusionDCriteria()
      .then(res=>{
        this.exlusionDCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});


  }

}
