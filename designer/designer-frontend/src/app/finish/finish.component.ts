import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';

@Component({
  selector: 'app-finish',
  templateUrl: './finish.component.html',
  styleUrls: ['./finish.component.css']
})
export class FinishComponent implements OnInit {

  constructor(public dataService: DataService) {
  }

  ngOnInit() {
  }

  exportFile() {
    // this.dataService.version = 'v1';
    this.dataService.saveFile(this.dataService.blob);
  }

  // exportFile() {
  //   this.dataService.version = 'v1';
  //   this.dataService.saveFile(this.dataService.blob);
  // }
  //
  // exportFileV2() {
  //   this.dataService.version = 'v2';
  //   this.dataService.saveFile(this.dataService.blobV2);
  // }

}
