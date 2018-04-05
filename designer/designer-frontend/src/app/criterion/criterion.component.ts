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
    console.log(this.criterion);
    console.log(this.form);
    console.log(this.form.controls);
    console.log(this.criterion.typeCode);
    console.log(this.form.get(this.criterion.typeCode));
  }

}
