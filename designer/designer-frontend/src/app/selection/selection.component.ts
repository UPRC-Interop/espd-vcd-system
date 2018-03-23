import { Component, OnInit } from '@angular/core';
import {SelectionCriteria} from "../model/selectionCriteria.model";
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {

  selectionACriteria:SelectionCriteria[]=null;
  selectionBCriteria:SelectionCriteria[]=null;
  selectionCCriteria:SelectionCriteria[]=null;
  selectionDCriteria:SelectionCriteria[]=null;


  constructor(private dataService:DataService) { }

  ngOnInit() {

    this.dataService.getSelectionACriteria()
      .then(res=>{
        this.selectionACriteria=res;
        console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionBCriteria()
      .then(res=>{
        this.selectionBCriteria=res;
        console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionCCriteria()
      .then(res=>{
        this.selectionCCriteria=res;
        console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionDCriteria()
      .then(res=>{
        this.selectionDCriteria=res;
        console.log(res);
      })
      .catch(err=>{console.log(err)});


  }

}
