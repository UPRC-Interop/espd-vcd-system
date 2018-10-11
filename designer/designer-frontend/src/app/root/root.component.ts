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

import {Component, OnChanges, OnInit, ViewChild} from '@angular/core';
import {DataService} from '../services/data.service';
import {UtilitiesService} from '../services/utilities.service';
import {StartComponent} from "../start/start.component";
import {ProcedureComponent} from "../procedure/procedure.component";
import {ProcedureEoComponent} from "../procedure-eo/procedure-eo.component";

@Component({
selector: 'app-root',
templateUrl: './root.component.html',
styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit, OnChanges {

  @ViewChild('startComponent') startComponent: StartComponent;
  @ViewChild('procedureCaComponent') procedureCaComponent: ProcedureComponent;
  @ViewChild('procedureEoComponent') procedureEoComponent: ProcedureEoComponent;
  startStepValid: boolean;
  procedureStepValid: boolean;

  isLinear = true;

  constructor(public dataService: DataService, public utilities: UtilitiesService) {
  }

  ngOnInit() {
    this.startStepValid = true;
    this.procedureStepValid = true;
  }

  ngOnChanges() {
  }

  onLanguageSelection(language: string) {
      // console.log(language);
      this.dataService.switchLanguage(language);
      this.utilities.initLanguage = false;
      this.utilities.start = true;
  }

  validateStep(event) {
    this.startStepValid = this.startComponent.startForm.valid;
    this.procedureStepValid = this.validateProcedureForm();
  }

  validateProcedureForm(): boolean {
    if ('undefined' !== typeof this.procedureCaComponent) {
      return !this.procedureCaComponent.procedureForm.touched || this.procedureCaComponent.procedureForm.valid;
    }

    if ('undefined' !== typeof this.procedureEoComponent) {
      return this.procedureEoComponent.procedureForm.touched || this.procedureEoComponent.procedureForm.valid
        && !this.procedureEoComponent.EOForm.touched || this.procedureEoComponent.EOForm.valid
        && (this.procedureEoComponent.formA == null || this.procedureEoComponent.formA.touched || this.procedureEoComponent.formA.valid)
        && (this.procedureEoComponent.formC == null || this.procedureEoComponent.formC.touched || this.procedureEoComponent.formC.valid)
        && (this.procedureEoComponent.formD == null || this.procedureEoComponent.formD.touched || this.procedureEoComponent.formD.valid);
    }

    return true;
  }

}
