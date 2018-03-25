import { Component, OnInit } from '@angular/core';
import {SelectionCriteria} from "../model/selectionCriteria.model";
import {DataService} from "../services/data.service";
import {FormControl, NgForm} from "@angular/forms/forms";

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
  selectionALLCriteria:SelectionCriteria[]=null;
  isSatisfiedALL:boolean=true;
  isAtoD:boolean=false;


  constructor(private dataService:DataService) { }

  ngOnInit() {




    this.dataService.getSelectionALLCriteria()
      .then(res=>{
        this.selectionALLCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionACriteria()
      .then(res=>{
        this.selectionACriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});


    this.dataService.getSelectionBCriteria()
      .then(res=>{
        this.selectionBCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionCCriteria()
      .then(res=>{
        this.selectionCCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});

    this.dataService.getSelectionDCriteria()
      .then(res=>{
        this.selectionDCriteria=res;
        // console.log(res);
      })
      .catch(err=>{console.log(err)});


  }


  handleALL(radio:FormControl){
    // console.dir(typeof(radio.value));
    if(radio.value==="YES"){
      this.isSatisfiedALL=false;
      this.isAtoD=true;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if(radio.value==="NO"){
      this.isSatisfiedALL=true;
      this.isAtoD=false;
    }
  }

  onSelectionSubmit(form:NgForm){
    this.dataService.selectionSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL)
  }

}
