///
/// Copyright 2016-2018 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataService} from '../services/data.service';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {FormGroup, NgForm} from '@angular/forms';
import {FormUtilService} from '../services/form-util.service';
import {ValidationService} from "../services/validation.service";

@Component({
  selector: 'app-finish-eo',
  templateUrl: './finish-eo.component.html',
  styleUrls: ['./finish-eo.component.css']
})
export class FinishEoComponent implements OnInit {

  @ViewChildren('form') forms: QueryList<NgForm>;

  @Input() reductionCriteria: ReductionCriterion[];
  @Input() form: FormGroup;

  constructor(
    public dataService: DataService,
    private formUtil: FormUtilService,
    private validationService: ValidationService
    ) {
  }

  ngOnInit() {

  }

  onExport() {
    // this.dataService.version = 'v1';
    this.formUtil.extractFormValuesFromCriteria(this.reductionCriteria, this.form, this.formUtil.evidenceList);
    this.dataService.finishEOSubmit(this.reductionCriteria);
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
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
