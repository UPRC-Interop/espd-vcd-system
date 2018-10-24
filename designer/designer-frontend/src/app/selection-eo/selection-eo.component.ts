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
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {FormUtilService} from '../services/form-util.service';
import {ValidationService} from "../services/validation.service";
import {BaseStep} from "../base/base-step";
import {WizardSteps} from "../base/wizard-steps.enum";

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit, BaseStep {
  // selectionALLCriteria: SelectionCriteria[] = null;

  @ViewChildren('form') forms: QueryList<NgForm>;

  @Input() selectionACriteria: SelectionCriteria[];
  @Input() selectionBCriteria: SelectionCriteria[];
  @Input() selectionCCriteria: SelectionCriteria[];
  @Input() selectionDCriteria: SelectionCriteria[];
  @Input() selectionALLCriteria: SelectionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formC: FormGroup;
  @Input() formD: FormGroup;
  @Input() formALL: FormGroup;
  isSatisfiedALL = true;
  isAtoD = false;

  constructor(
    public dataService: DataService,
    public formUtil: FormUtilService,
    private validationService: ValidationService
    ) {
  }

  ngOnInit() {
    if (this.dataService.isReadOnly()) {
      this.isAtoD = true;
      this.isSatisfiedALL = false;
    }
  }


  handleALL(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'YES') {
      this.isSatisfiedALL = false;
      this.isAtoD = true;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if (radio.value === 'NO') {
      this.isSatisfiedALL = true;
      this.isAtoD = false;
    }
  }


  onSelectionEOSubmit() {

    this.formUtil.extractFormValuesFromCriteria(this.selectionACriteria, this.formA, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionBCriteria, this.formB, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionCCriteria, this.formC, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionDCriteria, this.formD, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.dataService.selectionALLCriteria, this.dataService.selectionALLCriteriaForm, this.formUtil.evidenceList);

    console.log('Selection A Criteria');
    console.log(this.selectionACriteria);
    console.log('Selection B Criteria');
    console.log(this.selectionBCriteria);
    console.log('Selection C Criteria');
    console.log(this.selectionCCriteria);
    console.log('Selection D Criteria');
    console.log(this.selectionDCriteria);
    console.log('Selection ALL Criteria');
    console.log(this.isSatisfiedALL);

    this.dataService.selectionEOSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }

  getWizardStep(): WizardSteps {
    return WizardSteps.SELECTION;
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }
}
