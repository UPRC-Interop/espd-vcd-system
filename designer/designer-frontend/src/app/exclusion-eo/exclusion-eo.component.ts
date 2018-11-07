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
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {DataService} from '../services/data.service';
import {FormGroup, NgForm} from '@angular/forms';
import {FormUtilService} from '../services/form-util.service';
import {ValidationService} from "../services/validation.service";
import {BaseStep} from "../base/base-step";
import {WizardSteps} from "../base/wizard-steps.enum";

@Component({
  selector: 'app-exclusion-eo',
  templateUrl: './exclusion-eo.component.html',
  styleUrls: ['./exclusion-eo.component.css']
})
export class ExclusionEoComponent implements OnInit, BaseStep {

  @ViewChildren('form') forms: QueryList<NgForm>;

  @Input() exclusionACriteria: ExclusionCriteria[];
  @Input() exclusionBCriteria: ExclusionCriteria[];
  @Input() exclusionCCriteria: ExclusionCriteria[];
  @Input() exclusionDCriteria: ExclusionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formD: FormGroup;
  @Input() formC: FormGroup;


  constructor(
    public dataService: DataService,
    public formUtil: FormUtilService,
    private validationService: ValidationService
    ) {
  }

  ngOnInit() {

  }

  getWizardStep(): WizardSteps {
    return WizardSteps.EXCLUSION;
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }
}
