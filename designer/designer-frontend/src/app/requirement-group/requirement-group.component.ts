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

  public showIndicator: boolean;

  constructor() {
  }

  ngOnInit() {
  }

  ngOnChanges() {
    this.showIndicator = this.checkIndicator(this.indicator);
    // console.log('Reg Group Id: ' + this.reqGroup.id + ' show Indicator is: ' + this.showIndicator);
  }

  checkIndicator(b: boolean): boolean {

    // console.log('Check Indicator called ' + this.reqGroup.id + ' with value of indicator: ' + this.indicator);
    if (this.reqGroup.condition) {
      if (this.reqGroup.condition.endsWith('ON_FALSE')) {
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
