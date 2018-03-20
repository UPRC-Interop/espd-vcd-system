import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";

@Component({
  selector: 'app-exclusion',
  templateUrl: './exclusion.component.html',
  styleUrls: ['./exclusion.component.css']
})
export class ExclusionComponent implements OnInit {

  exCriteria:any;

  constructor(private APIService:ApicallService) { }

  ngOnInit() {
    this.APIService.getExclusionCriteria()
      .then(res=>{
        this.exCriteria=res;
        console.log(this.exCriteria);
      })
      .catch(err=>{console.log(err)});
  }

}
