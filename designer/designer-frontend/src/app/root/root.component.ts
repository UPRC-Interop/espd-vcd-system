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
import {ExclusionComponent} from "../exclusion/exclusion.component";
import {ExclusionEoComponent} from "../exclusion-eo/exclusion-eo.component";
import {SelectionEoComponent} from "../selection-eo/selection-eo.component";
import {FinishComponent} from "../finish/finish.component";
import {FinishEoComponent} from "../finish-eo/finish-eo.component";
import {SelectionComponent} from "../selection/selection.component";
import {WizardSteps} from "../base/wizard-steps.enum";

@Component({
selector: 'app-root',
templateUrl: './root.component.html',
styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit, OnChanges {

  @ViewChild('startComponent') startComponent: StartComponent;
  @ViewChild('procedureCaComponent') procedureCaComponent: ProcedureComponent;
  @ViewChild('procedureEoComponent') procedureEoComponent: ProcedureEoComponent;
  @ViewChild('exclusionCaComponent') exclusionCaComponent: ExclusionComponent;
  @ViewChild('exclusionEoComponent') exclusionEoComponent: ExclusionEoComponent;
  @ViewChild('selectionCaComponent') selectionCaComponent: SelectionComponent;
  @ViewChild('selectionEoComponent') selectionEoComponent: SelectionEoComponent;
  @ViewChild('finishCaComponent') finishCaComponent: FinishComponent;
  @ViewChild('finishEoComponent') finishEoComponent: FinishEoComponent;

  startStepValid: boolean;
  procedureStepValid: boolean;
  exclusionStepValid: boolean;
  selectionStepValid: boolean;
  finishStepValid: boolean;

  isLinear = true;

  constructor(public dataService: DataService, public utilities: UtilitiesService) {
  }

  ngOnInit() {
    this.startStepValid = true;
    this.procedureStepValid = true;
    this.exclusionStepValid = true;
    this.selectionStepValid = true;
    this.finishStepValid = true;
  }

  ngOnChanges() {
  }

  onLanguageSelection(language: string) {
      // console.log(language);
      this.dataService.switchLanguage(language);
      this.utilities.initLanguage = false;
      this.utilities.start = true;
  }

  private validateSteps(event) {
    this.startStepValid = event.selectedIndex === WizardSteps.START || this.startComponent.areFormsValid();
    this.procedureStepValid = event.selectedIndex === WizardSteps.PROCEDURE || RootComponent.anyComponentIsValid(this.procedureCaComponent, this.procedureEoComponent);
    this.exclusionStepValid = event.selectedIndex === WizardSteps.EXCLUSION || RootComponent.anyComponentIsValid(this.exclusionCaComponent, this.exclusionEoComponent);
    this.selectionStepValid = event.selectedIndex === WizardSteps.SELECTION || RootComponent.anyComponentIsValid(this.selectionCaComponent, this.selectionEoComponent);
    this.finishStepValid = event.selectedIndex === WizardSteps.FINISH || RootComponent.anyComponentIsValid(this.finishCaComponent, this.finishEoComponent);
  }

  private static anyComponentIsValid(componentCa, componentEo) {
    return ('undefined' === typeof  componentCa || componentCa.areFormsValid()) && ('undefined' === typeof componentEo || componentEo.areFormsValid());
  }
}
