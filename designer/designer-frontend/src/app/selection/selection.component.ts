import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {

  selectionACriteria:any;
  selectionBCriteria:any;
  selectionCCriteria:any;
  selectionDCriteria:any;


  selCriteria:any;
  constructor(private APIService:ApicallService) { }

  ngOnInit() {
    this.APIService.getSelectionCriteria_A()
      .then(res=>{
        this.selectionACriteria=res;
      })
      .catch(err=>{console.log(err)});

    this.APIService.getSelectionCriteria_B()
      .then(res=>{
        this.selectionBCriteria=res;
      })
      .catch(err=>{console.log(err)});

    this.APIService.getSelectionCriteria_C()
      .then(res=>{
        this.selectionCCriteria=res;
      })
      .catch(err=>{console.log(err)});

    this.APIService.getSelectionCriteria_D()
      .then(res=>{
        this.selectionDCriteria=res;
      })
      .catch(err=>{console.log(err)});
  }

}
