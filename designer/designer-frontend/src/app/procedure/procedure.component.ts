import { Component, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import {DataService} from "../services/data.service";
import {ProcedureType} from "../model/procedureType.model";
import {Country} from "../model/country.model";

@Component({
  selector: 'app-procedure',
  templateUrl: './procedure.component.html',
  styleUrls: ['./procedure.component.css']
})
export class ProcedureComponent implements OnInit, OnChanges {

  countries:Country[]=null;
  procedureTypes:ProcedureType[]=null;



  constructor(private dataService:DataService) { }

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

  ngOnChanges(changes: SimpleChanges){
    console.log(this.dataService.receivedNoticeNumber);
  }



}
