import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {RequirementGroup} from '../model/requirementGroup.model';
import {FormGroup} from '@angular/forms';
import {FormUtilService} from '../services/form-util.service';
import {UUID} from 'angular2-uuid';

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
  public placeHolder: any = {};

  public showIndicator: boolean;

  constructor(public formUtil: FormUtilService) {
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

  onAdd(groupId: string) {
    console.log(`Called onAdd with groupId ${groupId}:`);
    console.log(this.formUtil.template[groupId]);

    if (this.formUtil.template[groupId] !== undefined) {

      // create new formGroup from template
      const fg = this.formUtil.createTemplateFormGroup(groupId);
      console.log('The Requirement Group created from the Template:');
      console.log(fg);

      // push to json Structure

      // Overwrite FIX: don't assign by reference.
      this.placeHolder = JSON.parse(JSON.stringify(this.formUtil.template[groupId]));
      // TODO: generate uuid
      let uuid = UUID.UUID();
      this.placeHolder.uuid = uuid;
//      this.formUtil.pushToReqGroups(this.reqGroup, this.placeHolder);
      console.log('Local ReqGroup:');
      console.log(this.reqGroup);
      console.log('new ReqGroup:');
      console.log(this.placeHolder);
      this.reqGroup.requirementGroups.push(this.placeHolder);
      console.log('NEW Local ReqGroup:');
      console.log(this.reqGroup);
      // add formGroup to Form object
      console.log('FORM before add: ');
      console.log(this.form);
      console.log(`Adding new control with id:${uuid}`);
      this.form.addControl(uuid, fg);
      console.log('FORM After ADD: ');
      console.log(this.form);

    } else {
      console.log('No template found for criteria creation');
    }
  }

  onRemove(reqGroup: RequirementGroup) {
    console.log('First remove the model, since this is where the iteration depends');
    this.reqGroup.requirementGroups = this.reqGroup.requirementGroups.filter(rg => rg !== reqGroup);

    console.log('Removing the Form Requirement Group');
    console.log(this.form);
    this.form.removeControl(reqGroup.uuid);
  }


}
