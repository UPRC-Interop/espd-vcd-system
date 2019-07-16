import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DataService} from '../services/data.service';

@Component({
  selector: 'app-surveydialog',
  templateUrl: './surveydialog.component.html',
  styleUrls: ['./surveydialog.component.css']
})
export class SurveydialogComponent implements OnInit {

  constructor(public thisDialogRef: MatDialogRef<SurveydialogComponent>,
              @Inject(MAT_DIALOG_DATA) public id: string,
              public dataService: DataService) { }

  ngOnInit() {
  }

  onClose() {
    this.thisDialogRef.close('CLOSING MODAL');
  }

}
