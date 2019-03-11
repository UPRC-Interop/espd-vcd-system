import {Component, Input, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {ECertisCriterion} from '../model/eCertisCriterion.model';

@Component({
  selector: 'app-e-certis-criterion',
  templateUrl: './e-certis-criterion.component.html',
  styleUrls: ['./e-certis-criterion.component.css']
})
export class ECertisCriterionComponent implements OnInit {

  @Input() criterion: ECertisCriterion;

  constructor() { }

  ngOnInit() {
  }

}
