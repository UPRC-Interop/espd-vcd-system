import {Component, OnInit, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {ApicallService} from '../services/apicall.service';
import {ToopCompanyData} from '../model/toopCompanyData.model';

@Component({
  selector: 'app-toopdialog',
  templateUrl: './toopdialog.component.html',
  styleUrls: ['./toopdialog.component.css']
})
export class TOOPDialogComponent implements OnInit {

  public companyData: ToopCompanyData;

  constructor(public thisDialogRef: MatDialogRef<TOOPDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public id: string,
              private APIService: ApicallService) {
  }

  ngOnInit() {
  }

  onGetTOOPData(id: string) {
    console.log(id);
    this.APIService.getTOOPData(id)
      .then(res => {
        console.log(res);
        this.companyData = res;
        console.log(this.companyData);
      })
      .catch(err => {
        console.log(err);
      });
  }

  onClose() {
    this.thisDialogRef.close('CLOSING MODAL');
  }

}
