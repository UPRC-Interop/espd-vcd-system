import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {FormGroup} from '@angular/forms';
import {RequirementResponse} from '../model/requirement-response.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import * as moment from 'moment';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {Evidence} from '../model/evidence.model';
import {FormUtilService} from '../services/form-util.service';

@Component({
  selector: 'app-finish-eo',
  templateUrl: './finish-eo.component.html',
  styleUrls: ['./finish-eo.component.css']
})
export class FinishEoComponent implements OnInit {

  @Input() reductionCriteria: ReductionCriterion[];
  @Input() form: FormGroup;

  constructor(public dataService: DataService, private formUtil: FormUtilService) {
  }

  ngOnInit() {

  }

  onExport() {
    // this.dataService.version = 'v1';
    this.formUtil.extractFormValuesFromCriteria(this.reductionCriteria, this.form, this.formUtil.evidenceList);
    this.dataService.finishEOSubmit(this.reductionCriteria);
  }


  // onExportV1() {
  //   this.dataService.version = 'v1';
  //   this.reductionCriteria.forEach(cr => {
  //     let formValues = this.form.getRawValue();
  //     formValues = formValues[cr.uuid.valueOf()];
  //     console.log(formValues);
  //
  //     // let testFormValues = formValues[cr.uuid.valueOf()];
  //     console.log('cr loop: ' + cr.uuid);
  //
  //     let testFormValues = null;
  //
  //     cr.requirementGroups.forEach(rg => {
  //       console.log('first rg loop: ' + rg.uuid);
  //
  //       if (testFormValues == null) {
  //         testFormValues = this.form.getRawValue();
  //         testFormValues = testFormValues[cr.uuid.valueOf()];
  //         // formValues = testFormValues;
  //       }
  //
  //       if (formValues[rg.uuid.valueOf()] == undefined) {
  //         testFormValues = testFormValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, testFormValues);
  //       } else if (formValues[rg.uuid.valueOf()] != undefined) {
  //         formValues = formValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, formValues);
  //       }
  //     });
  //   });
  //   this.dataService.finishEOSubmit(this.reductionCriteria);
  // }
  //
  // onExportV2() {
  //   this.dataService.version = 'v2';
  //   this.reductionCriteria.forEach(cr => {
  //     let formValues = this.form.getRawValue();
  //     formValues = formValues[cr.uuid.valueOf()];
  //     console.log(formValues);
  //
  //     // let testFormValues = formValues[cr.uuid.valueOf()];
  //     console.log('cr loop: ' + cr.uuid);
  //
  //     let testFormValues = null;
  //
  //     cr.requirementGroups.forEach(rg => {
  //       console.log('first rg loop: ' + rg.uuid);
  //
  //       if (testFormValues == null) {
  //         testFormValues = this.form.getRawValue();
  //         testFormValues = testFormValues[cr.uuid.valueOf()];
  //         // formValues = testFormValues;
  //       }
  //
  //       if (formValues[rg.uuid.valueOf()] == undefined) {
  //         testFormValues = testFormValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, testFormValues);
  //       } else if (formValues[rg.uuid.valueOf()] != undefined) {
  //         formValues = formValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, formValues);
  //       }
  //     });
  //   });
  //
  //
  //   this.dataService.finishEOSubmit(this.reductionCriteria);
  // }

}
