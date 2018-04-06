import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';

@Component({
  selector: 'app-finish',
  templateUrl: './finish.component.html',
  styleUrls: ['./finish.component.css']
})
export class FinishComponent implements OnInit {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
  }

  exportFile() {
    this.dataService.saveFile(this.dataService.blob);
  }

}
