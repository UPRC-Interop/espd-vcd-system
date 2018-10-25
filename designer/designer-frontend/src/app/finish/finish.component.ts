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
import {NgForm} from "@angular/forms";
import {ValidationService} from "../services/validation.service";
import {BaseStep} from "../base/base-step";
import {WizardSteps} from "../base/wizard-steps.enum";

@Component({
  selector: 'app-finish',
  templateUrl: './finish.component.html',
  styleUrls: ['./finish.component.css']
})
export class FinishComponent implements OnInit, BaseStep {

  @ViewChildren('form') forms: QueryList<NgForm>;

  @Input() startStepValid: boolean;
  @Input() procedureStepValid: boolean;
  @Input() exclusionStepValid: boolean;
  @Input() selectionStepValid: boolean;
  @Input() finishStepValid: boolean;

  constructor(
    public dataService: DataService,
    private validationService: ValidationService
    ) {
  }

  ngOnInit() {
  }

  exportFile() {
    // this.dataService.version = 'v1';
    this.dataService.saveFile(this.dataService.blob);
  }

  getWizardStep(): WizardSteps {
    return WizardSteps.FINISH;
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }

  // exportFile() {
  //   this.dataService.version = 'v1';
  //   this.dataService.saveFile(this.dataService.blob);
  // }
  //
  // exportFileV2() {
  //   this.dataService.version = 'v2';
  //   this.dataService.saveFile(this.dataService.blobV2);
  // }

}
