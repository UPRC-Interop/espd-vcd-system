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

import {Component, Input, OnInit} from '@angular/core';
import {FormControl, NgForm} from '@angular/forms/forms';
import {ApicallService} from '../services/apicall.service';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {UtilitiesService} from '../services/utilities.service';
import {MatStepper} from '@angular/material';
import {Cadetails} from '../model/caDetails.model';
import {EoDetails} from '../model/eoDetails.model';
import {PostalAddress} from '../model/postalAddress.model';
import {ContactingDetails} from '../model/contactingDetails.model';

// import {ProcedureType} from "../model/procedureType.model";


@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {

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
  isLoading = false;

  @Input()
  parentStepper: MatStepper;

  // procedureTypes:ProcedureType[];

  constructor(public dataService: DataService, private APIService: ApicallService, public utilities: UtilitiesService) {
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


  handleFileUpload(files: FileList) {
    console.log(files);
    this.fileToUpload.push(files.item(0));
  }

  handleRole(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'CA') {
      this.isCA = true;
      this.isEO = false;
      this.utilities.type = 'ESPD_REQUEST';
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
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
      this.isReuseESPD = false;
      this.isReviewESPD = false;
      this.utilities.isImportReq = false;
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
    this.dataService.startESPD(form)
    const role = ( this.isCA ? 'CA' : 'EO');
    this.dataService.ReuseESPD(this.fileToUpload, form, role)
    .then(() => {
      this.isLoading = false;
        this.parentStepper.next();
        this.utilities.isStarted = true;
    })
    .catch(() => {this.isLoading = false});
  }
}
