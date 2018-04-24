import {Component, OnChanges, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit, OnChanges {

  isLinear = true;
  // eoRelatedFormA = this.dataService.eoRelatedACriteriaForm;

  constructor(public dataService: DataService) {
  }

  ngOnInit() {
  }

  ngOnChanges() {
  }

}
