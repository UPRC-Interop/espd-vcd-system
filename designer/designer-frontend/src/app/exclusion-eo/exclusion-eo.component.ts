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

@Component({
  selector: 'app-exclusion-eo',
  templateUrl: './exclusion-eo.component.html',
  styleUrls: ['./exclusion-eo.component.css']
})
export class ExclusionEoComponent implements OnInit {

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


  onExclusionEOSubmit() {
    this.formUtil.extractFormValuesFromCriteria(this.exclusionACriteria, this.formA, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionBCriteria, this.formB, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionCCriteria, this.formC, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionDCriteria, this.formD, this.formUtil.evidenceList);


    console.log(this.exclusionACriteria);
    console.log(this.exclusionBCriteria);
    console.log(this.exclusionCCriteria);
    console.log(this.exclusionDCriteria);

    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }
}
