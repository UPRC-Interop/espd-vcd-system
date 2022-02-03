import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { DataService } from 'app/services/data.service';

@Component({
  selector: 'app-message-alert',
  templateUrl: './message-alert.component.html',
  styleUrls: ['./message-alert.component.css']
})
export class MessageAlertComponent implements OnInit {

  constructor(public thisDialogRef: MatDialogRef<MessageAlertComponent>,
              @Inject(MAT_DIALOG_DATA) public id: string,
              public dataService: DataService) { }

  ngOnInit() {
  }

  onClose() {
    this.thisDialogRef.close('CLOSING MODAL');
  }

}
