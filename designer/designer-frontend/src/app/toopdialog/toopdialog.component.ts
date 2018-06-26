import { Component, OnInit, Inject } from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-toopdialog',
  templateUrl: './toopdialog.component.html',
  styleUrls: ['./toopdialog.component.css']
})
export class TOOPDialogComponent implements OnInit {

  constructor(public thisDialogRef: MatDialogRef<TOOPDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: string) { }

  ngOnInit() {
  }

}
