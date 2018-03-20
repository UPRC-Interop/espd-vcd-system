import { Component, OnInit } from '@angular/core';
import {ApicallService} from "../services/apicall.service";

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {

  selCriteria:any;
  constructor(private APIService:ApicallService) { }

  ngOnInit() {
    this.APIService.getSelectionCriteria()
      .then(res=>{
        this.selCriteria=res;
        console.log(this.selCriteria);
      })
      .catch(err=>{console.log(err)});
  }

}
