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

import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormControl, NgForm} from '@angular/forms';
import {ApicallService} from '../services/apicall.service';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {UtilitiesService} from '../services/utilities.service';
import {ValidationService} from "../services/validation.service";

// import {ProcedureType} from "../model/procedureType.model";


@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css'],
  outputs: ['forms']
})
export class StartComponent implements OnInit {

  @ViewChildren('form') forms: QueryList<NgForm>;

  countries: Country[];
  isCA = false;
  isEO = false;
  isCreateNewESPD = false;
  isReuseESPD = false;
  isReviewESPD = false;
  isImportESPD = false;
  isCreateResponse = false;
  fileToUpload: File[] = [];
  reset = false;

  // procedureTypes:ProcedureType[];

  constructor(
    public dataService: DataService,
    private APIService: ApicallService,
    public utilities: UtilitiesService,
    private validationService: ValidationService) {
  }

  ngOnInit() {
    this.dataService.getCountries()
      .then(res => {
        this.countries = res;
        // console.log("this is from start component"); console.log(res);
      })
      .catch(err => {
        console.log(err);
      });


  }

  public areFormsValid(): boolean {
    return this.validationService.validateFormsInComponent(this.forms);
  }

  handleFileUpload(files: FileList) {
    console.log(files);
    this.fileToUpload.push(files.item(0));
  }

  handleRole(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'CA') {
      this.isCA = true;
      this.isEO = false;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if (radio.value === 'EO') {
      this.isEO = true;
      this.isCA = false;
    }
  }

  handleCASelection(radio: FormControl) {
    if (radio.value === 'createNewESPD') {
      this.isCreateNewESPD = true;
      this.utilities.isCreateNewESPD = true;
      this.isReuseESPD = false;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'reuseESPD') {
      this.isCreateNewESPD = false;
      this.utilities.isCreateNewESPD = false;
      this.isReuseESPD = true;
      this.isReviewESPD = false;
      this.utilities.isReviewESPD = false;
    } else if (radio.value === 'reviewESPD') {
      this.isCreateNewESPD = false;
      this.utilities.isCreateNewESPD = false;
      this.isReuseESPD = false;
      this.isReviewESPD = true;
      this.utilities.isReviewESPD = true;
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
    // console.log(form);
    // form and model reset in case of start
    this.utilities.isStarted = true;


    console.log(this.dataService.isReadOnly());
    // CA reuses ESPDRequest
    if (this.isCA) {
      const role = 'CA';
      this.dataService.ReuseESPD(this.fileToUpload, form, role);
    } else if (this.isEO) {
      const role = 'EO';
      this.dataService.ReuseESPD(this.fileToUpload, form, role);
    }


    // Start New ESPD
    this.dataService.startESPD(form);


  }

}
