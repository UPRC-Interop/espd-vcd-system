import {Component, Input, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-criterion',
  templateUrl: './criterion.component.html',
  styleUrls: ['./criterion.component.css']
})
export class CriterionComponent implements OnInit {

  @Input() criterion: ExclusionCriteria;
  @Input() form: FormGroup;
  @Input() indicator: boolean;

  constructor() {
  }

  ngOnInit() {
    this.form = this.form[this.criterion.uuid];
  }

}
