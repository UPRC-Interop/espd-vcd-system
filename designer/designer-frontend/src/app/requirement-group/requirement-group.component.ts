import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {RequirementGroup} from '../model/requirementGroup.model';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-requirement-group',
  templateUrl: './requirement-group.component.html',
  styleUrls: ['./requirement-group.component.css']
})
export class RequirementGroupComponent implements OnInit, OnChanges {

  @Input() reqGroup: RequirementGroup;
  @Input() form: FormGroup;
  @Input() indicator: boolean;
  public childIndicator: boolean;
  public tempForm: FormGroup;

  public showIndicator: boolean;

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges() {
    this.showIndicator = this.checkIndicator(this.indicator);
  }

  checkIndicator(b: boolean): boolean {

    // console.log('Check Indicator called ' + this.reqGroup.id + ' with value of indicator: ' + this.indicator);
    if (this.reqGroup.condition) {
      // console.log(this.reqGroup.condition === 'ONTRUE');
      // console.log(this.reqGroup.condition === 'ONFALSE');
      if (this.reqGroup.condition.endsWith('ON_FALSE') || this.reqGroup.condition === 'ONFALSE') {
        return !b;
      } else {
        return b;
      }
    }
    return true;
  }

  childIndicatorChangedHandler(event: boolean) {
    this.childIndicator = (event);

  }
}
