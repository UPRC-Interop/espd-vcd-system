import { Component, OnInit } from '@angular/core';
import {FormControl, NgForm} from "@angular/forms/forms";
import {ApicallService} from "../services/apicall.service";
import {DataService} from "../services/data.service";
import {Country} from "../model/country.model";


@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {

  countries:Country[];

  constructor(private dataService:DataService, private APIService:ApicallService) { }

  ngOnInit() {
    // this.APIService.GETCountryList().then(res=>{console.log(res)}).catch(err=>{console.log(err)});
    // this.APIService.requestCountryList();
    // this.countries=this.APIService.getCountries();
    // console.log(this.countries);
    this.countries=this.dataService.getCountries();
    console.log(this.countries);
  }

  isCA:boolean=false;
  isEO:boolean=false;
  isCreateNewESPD:boolean=false;
  isReuseESPD:boolean=false;
  isReviewESPD:boolean=false;
  isImportESPD:boolean=false;
  isCreateResponse:boolean=false;
  fileToUpload:File[] = [];

  // countries=["Greece", "Germany", "France"];

  handleFileUpload(files:FileList) {
    console.log(files);
    this.fileToUpload.push(files.item(0));
  }

  handleRole(radio:FormControl){
    // console.dir(typeof(radio.value));
    if(radio.value==="CA"){
      this.isCA=true;
      this.isEO=false;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if(radio.value==="EO"){
      this.isEO=true;
      this.isCA=false;
    }
  }

  handleCASelection(radio:FormControl) {
    if(radio.value==="createNewESPD") {
      this.isCreateNewESPD=true;
      this.isReuseESPD=false;
      this.isReviewESPD=false;
    } else if (radio.value==="reuseESPD"){
      this.isCreateNewESPD=false;
      this.isReuseESPD=true;
      this.isReviewESPD=false;
    } else if (radio.value==="reviewESPD"){
      this.isCreateNewESPD=false;
      this.isReuseESPD=false;
      this.isReviewESPD=true;
    }
  }
  handleEOSelection(radio:FormControl){
    if(radio.value==="importESPD") {
      this.isImportESPD=true;
      this.isCreateResponse=false;
    }else if (radio.value==="createResponse"){
      this.isImportESPD=false;
      this.isCreateResponse=true;
    }
  }

  onStartSubmit(form:NgForm){
    console.log(form);
    this.APIService.postFile(this.fileToUpload)
      .then(res=>res)
      .catch(err=>err);

  }

}
