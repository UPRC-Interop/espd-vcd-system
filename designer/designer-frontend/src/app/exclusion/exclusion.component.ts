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
import {NgForm} from '@angular/forms';
import {ValidationService} from "../services/validation.service";
import {BaseStep} from "../base/base-step";
import {WizardSteps} from "../base/wizard-steps.enum";

@Component({
  selector: 'app-exclusion',
  templateUrl: './exclusion.component.html',
  styleUrls: ['./exclusion.component.css']
})
export class ExclusionComponent implements OnInit, BaseStep {

  @ViewChildren('form') forms: QueryList<NgForm>;

  @Input() exclusionACriteria: ExclusionCriteria[];
  @Input() exclusionBCriteria: ExclusionCriteria[];
  @Input() exclusionCCriteria: ExclusionCriteria[];
  @Input() exclusionDCriteria: ExclusionCriteria[];

  constructor(
    public dataService: DataService,
    private validationService: ValidationService
    ) {
  }

  ngOnInit() {
    // this.dataService.getExclusionACriteria()
    //   .then(res => {
    //     this.exclusionACriteria = res;
    //     // console.log("This is exclusionACriteria: ");
    //     // console.log(this.exclusionACriteria);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getExclusionBCriteria()
    //   .then(res => {
    //     this.exclusionBCriteria = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getExclusionCCriteria()
    //   .then(res => {
    //     this.exclusionCCriteria = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getExclusionDCriteria()
    //   .then(res => {
    //     this.exclusionDCriteria = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });


  }


  onExclusionSubmit(form: NgForm) {
    console.log(form.value);
    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

  getWizardStep(): WizardSteps {
    return WizardSteps.EXCLUSION;
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }
}
