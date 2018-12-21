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
import {FormControl, NgForm} from '@angular/forms';
import {ApicallService} from '../services/apicall.service';
import {DataService} from '../services/data.service';
import {UtilitiesService} from '../services/utilities.service';
import {CodelistService} from '../services/codelist.service';
import {MatStepper} from '@angular/material';
import {ValidationService} from '../services/validation.service';
import {BaseStep} from '../base/base-step';
import {WizardSteps} from '../base/wizard-steps.enum';

// import {ProcedureType} from "../model/procedureType.model";


@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css'],
  outputs: ['forms']
})
export class StartComponent implements OnInit, BaseStep {

  @ViewChildren('form') forms: QueryList<NgForm>;
  isCA = false;
  isEO = false;
  isCreateNewESPD = false;
  isReuseESPD = false;
  isReviewESPD = false;
  isImportESPD = false;
  isCreateResponse = false;
  fileToUpload: File[] = [];
  reset = false;
  isLoading = false;

  @Input()
  parentStepper: MatStepper;

  constructor(public dataService: DataService,
              private APIService: ApicallService,
              public utilities: UtilitiesService,
              public codelist: CodelistService,
              private validationService: ValidationService) {
  }

  ngOnInit() {

  }

  handleFileUpload(files: FileList) {
    console.log(files);
    this.fileToUpload.push(files.item(0));
  }

  handleRole(radio: FormControl) {
    if (radio.value === 'CA') {
      this.isCA = true;
      this.isEO = false;
      this.utilities.type = 'ESPD_REQUEST';
    } else if (radio.value === 'EO') {
      this.isEO = true;
      this.isCA = false;
      this.utilities.type = 'ESPD_RESPONSE';
    }
  }

  handleCASelection(radio: FormControl) {
    if (radio.value === 'createNewESPD') {
      this.isCreateNewESPD = true;
      this.utilities.isCreateNewESPD = true;
      this.utilities.isImportReq = false;
      this.isReuseESPD = false;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'reuseESPD') {
      this.isCreateNewESPD = false;
      this.utilities.isCreateNewESPD = false;
      this.utilities.isImportReq = true;
      this.isReuseESPD = true;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'reviewESPD') {
      this.isCreateNewESPD = false;
      this.utilities.isCreateNewESPD = false;
      this.utilities.isImportReq = false;
      this.isReuseESPD = false;
      this.isReviewESPD = true;
      this.utilities.isImportReq = false;
      this.utilities.isReviewESPD = true;
    }
  }

  handleQATypeSelection(radio: FormControl) {
    console.log(radio.value);
    if (radio.value === 'REGULATED') {
      this.utilities.qualificationApplicationType = 'regulated';
    } else if (radio.value === 'SELF-CONTAINED') {
      this.utilities.qualificationApplicationType = 'selfcontained';
      this.APIService.version = 'v2';
    }
  }

  handleVersionSelection(radio: FormControl) {
    console.log(radio.value);
    if (radio.value === 'v1') {
      this.APIService.version = 'v1';
    } else if (radio.value === 'v2') {
      this.APIService.version = 'v2';
    }
  }

  handleEOSelection(radio: FormControl) {
    if (radio.value === 'importESPD') {
      this.isImportESPD = true;
      this.utilities.isImportESPD = true;
      this.isCreateResponse = false;
      this.utilities.isCreateResponse = false;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'createResponse') {
      this.isImportESPD = false;
      this.utilities.isImportESPD = false;
      this.isCreateResponse = true;
      this.utilities.isCreateResponse = true;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'reviewESPD') {
      this.isImportESPD = false;
      this.utilities.isImportESPD = false;
      this.isCreateResponse = false;
      this.utilities.isCreateResponse = false;
      this.isReviewESPD = true;
      this.utilities.isReviewESPD = true;
    }
  }

  onStartSubmit(form: NgForm) {
    this.isLoading = true;
    console.log(this.isLoading);
    this.dataService.startESPD(form).then(() => {
      this.isLoading = false;
      this.parentStepper.next();
      this.utilities.isStarted = true;
    }).catch(() => {
      this.isLoading = false;
    });
    const role = (this.isCA ? 'CA' : 'EO');
    this.dataService.ReuseESPD(this.fileToUpload, form, role)
      .then(() => {
        this.isLoading = false;
        this.parentStepper.next();
        this.utilities.isStarted = true;
      })
      .catch(() => {
        this.isLoading = false;
      });
  }

  getWizardStep(): WizardSteps {
    return WizardSteps.START;
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }

}
