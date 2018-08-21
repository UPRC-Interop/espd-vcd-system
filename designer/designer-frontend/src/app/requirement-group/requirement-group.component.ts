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
    // this.childIndicator = true;
    // this.tempForm = this.form;
    // console.log(this.form.get(this.reqGroup.uuid));
    // console.log(this.form.controls[this.reqGroup.uuid]);
    // this.form = this.form[this.reqGroup.uuid];
    // console.log(this.form);
    // console.log(this.form.controls);
    // for (const control in this.form.controls) {
    //   if (this.form.get(control)) {
    //     console.log(this.form.get(control));
    //     // if (this.form.get(control).value.controls !== null) {
    //     //   console.log(this.form.get(control).value.controls !== null);
    //     // }
    //
    //   }
    // }
  }

  ngOnChanges() {
    this.showIndicator = this.checkIndicator(this.indicator);
    console.log(this.showIndicator);

    // if form.get(rg.uuid) != undefined

    // console.log('Reg Group Id: ' + this.reqGroup.uuid + ' show Indicator is: ' + this.showIndicator);
    // console.log(typeof this.showIndicator);
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
    // console.log('Before ====== child indicator is: ' + this.childIndicator);
    console.log(typeof this.childIndicator);
    //
    // if (typeof this.childIndicator === 'string') {
    //   if (this.childIndicator === '') {
    //     this.childIndicator = false;
    //     if (this.form.controls[this.reqGroup.uuid] !== undefined) {
    //       this.childIndicator = true;
    //     }
    //   } else {
    //     this.childIndicator = true;
    //   }
    // }

    // console.log('child indicator is: ' + this.childIndicator);
    // console.log(typeof this.childIndicator);

    // if (this.form.controls !== undefined) {
    //   this.childIndicator = true;
    // }


    // this.childIndicator = false;


    // if (this.form.get(this.reqGroup.uuid)) {
    //   console.log(this.form.get(this.reqGroup.uuid));
    //   console.log('this is true');
    // } else {
    //   console.log(this.form.get(this.reqGroup.uuid));
    //   console.log('this is false');
    // }

  }
}
