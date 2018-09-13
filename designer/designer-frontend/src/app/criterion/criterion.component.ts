import {Component, Input, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {FormGroup} from '@angular/forms';
import {RequirementGroup} from '../model/requirementGroup.model';
import {FormUtilService} from '../services/form-util.service';
import {UUID} from 'angular2-uuid';
import {FullCriterion} from '../model/fullCriterion.model';

@Component({
  selector: 'app-criterion',
  templateUrl: './criterion.component.html',
  styleUrls: ['./criterion.component.css']
})
export class CriterionComponent implements OnInit {

  @Input() criterion: ExclusionCriteria;
  @Input() form: FormGroup;
  @Input() indicator: boolean;
  public placeHolder: any = {};
  fullCriterionList: FullCriterion[] = null;

  constructor(public formUtil: FormUtilService) {
  }

  ngOnInit() {
    this.form = this.form[this.criterion.uuid];
  }

  onAdd(reqGroup: RequirementGroup) {
    console.log(`Called onAdd with groupId ${reqGroup.uuid}:`);
    console.log(this.formUtil.template[reqGroup.uuid]);

    if (this.formUtil.template[reqGroup.uuid] !== undefined) {

// create new formGroup from template
      const fg = this.formUtil.createTemplateFormGroup(reqGroup.uuid);
      console.log('The Requirement Group created from the Template: ');
      console.log(fg);

// push to json Structure

// Overwrite FIX: don't assign by reference.
      this.placeHolder = JSON.parse(JSON.stringify(this.formUtil.template[reqGroup.uuid]));
// TODO: generate uuid
      let uuid = UUID.UUID();
      this.placeHolder.uuid = uuid;
// this.formUtil.pushToReqGroups(this.reqGroup, this.placeHolder);
      console.log('Local ReqGroup:');
      console.log(reqGroup);
      console.log('new ReqGroup:');
      console.log(this.placeHolder);

      // change requirement ids
      this.formUtil.changeReqId(this.placeHolder);


      this.criterion.requirementGroups.push(this.placeHolder);
      console.log('NEW Local ReqGroup:');
      console.log(reqGroup);
// add formGroup to Form object
      console.log('FORM before add: ');
      console.log(this.form);
      console.log(`Adding new control with id:${uuid}`);
      this.form.addControl(uuid, fg);
      console.log('FORM After ADD: ');
      console.log(this.form);

      this.formUtil.getReqGroups(this.placeHolder);

    } else {
      console.log('No template found for criteria creation');
    }
  }

  onRemove(reqGroup: RequirementGroup) {
    console.log('First remove the model, since this is where the iteration depends');
    this.criterion.requirementGroups = this.criterion.requirementGroups.filter(rg => rg !== reqGroup);

    console.log('Removing the Form Requirement Group');
    console.log(this.form);
    this.form.removeControl(reqGroup.uuid);
  }

}
