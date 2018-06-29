import {Component, OnInit, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {ApicallService} from '../services/apicall.service';
import {ToopCompanyData} from '../model/toopCompanyData.model';
import {DataService} from '../services/data.service';

@Component({
  selector: 'app-toopdialog',
  templateUrl: './toopdialog.component.html',
  styleUrls: ['./toopdialog.component.css']
})
export class TOOPDialogComponent implements OnInit {

  public companyData: ToopCompanyData;

  constructor(public thisDialogRef: MatDialogRef<TOOPDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public id: string,
              private APIService: ApicallService,
              public dataService: DataService) {
  }

  ngOnInit() {
  }

  onGetTOOPData(id: string) {
    console.log(id);
    this.APIService.getTOOPData(id, this.dataService.selectedEOCountry)
      .then(res => {
        console.log(res);
        this.companyData = res;
        console.log(this.companyData);
      })
      .catch(err => {
        console.log(err);
      });
  }

  onUseTOOPData() {
    console.log(this.companyData);
    this.dataService.EODetails.name = this.companyData.name;
    this.dataService.EODetails.id = this.companyData.id;
    this.dataService.EODetails.webSiteURI = this.companyData.webSiteURI;
    this.dataService.EODetails.smeIndicator = this.companyData.smeIndicator;
    this.dataService.EODetails.postalAddress = this.companyData.postalAddress;
    this.dataService.EODetails.postalAddress.addressLine1 = this.companyData.postalAddress.addressLine1;
    this.dataService.EODetails.postalAddress.city = this.companyData.postalAddress.city;
    this.dataService.EODetails.postalAddress.postCode = this.companyData.postalAddress.postCode;
    this.dataService.EODetails.postalAddress.countryCode = this.companyData.postalAddress.countryCode;

    console.log(this.dataService.EODetails);
    this.dataService.eoDetailsFromTOOPFormUpdate();
    this.thisDialogRef.close('CLOSING MODAL');
  }

  onClose() {
    this.thisDialogRef.close('CLOSING MODAL');
  }

}
