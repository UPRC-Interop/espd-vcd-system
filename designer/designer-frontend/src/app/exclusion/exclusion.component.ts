import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";

@Component({
  selector: 'app-exclusion',
  templateUrl: './exclusion.component.html',
  styleUrls: ['./exclusion.component.css']
})
export class ExclusionComponent implements OnInit {

  exlusionACriteria:any;
  exlusionBCriteria:any;
  exlusionCCriteria:any;
  exlusionDCriteria:any;

  constructor(private APIService:ApicallService) { }

  ngOnInit() {
    this.APIService.getExclusionCriteria_A()
      .then(res=>{
        this.exlusionACriteria=res;
        // console.log(this.exlusionACriteria);
      })
      .catch(err=>{console.log(err)});

    this.APIService.getExclusionCriteria_B()
      .then(res=>{
        this.exlusionBCriteria=res;
        // console.log(this.exlusionBCriteria);
      })
      .catch(err=>{console.log(err)});

    this.APIService.getExclusionCriteria_C()
      .then(res=>{
        this.exlusionCCriteria=res;
        // console.log(this.exlusionCCriteria);
      })
      .catch(err=>{console.log(err)});

    this.APIService.getExclusionCriteria_D()
      .then(res=>{
        this.exlusionDCriteria=res;
        // console.log(this.exlusionDCriteria);
      })
      .catch(err=>{console.log(err)});
  }

}
