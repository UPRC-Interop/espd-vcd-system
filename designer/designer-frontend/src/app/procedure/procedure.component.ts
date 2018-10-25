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

import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NumberOjsValidation} from "../validation/number-ojs/number-ojs-validation";
import {InputValidationStateMatcher} from "../validation/input-validation-state-matcher/input-validation-state-matcher";
import {UrlValidation} from "../validation/url/url-validation";

@Component({
  selector: 'app-procedure',
  templateUrl: './procedure.component.html',
  styleUrls: ['./procedure.component.css']
})
export class ProcedureComponent implements OnInit, OnChanges {

  procedureForm: FormGroup;
  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  matcher = new InputValidationStateMatcher();
  // @Input() eoRelatedCriteria: EoRelatedCriterion[];
  // @Input() reductionCriteria: ReductionCriterion[];


  constructor(public dataService: DataService) {
    this.procedureForm = new FormGroup({
      'pub': new FormGroup({
        'receivedNoticeNumber': new FormControl(null, []), // National Notice Number
        'noticeNumberOJS': new FormControl(null, [NumberOjsValidation]), // OJS
        'ojsURL': new FormControl(null, [UrlValidation]) // URL
      }),
      'details': new FormGroup({
        'name': new FormControl(null, []),
        'id': new FormControl(null, []),
        'website': new FormControl(null, [UrlValidation]), // URL
        'city': new FormControl(null, []),
        'street': new FormControl(null, []),
        'postcode': new FormControl(null, []), // PostCode
        'contactname': new FormControl(null, []),
        'telephone': new FormControl(null, []), // Phone Number
        'faxnumber': new FormControl(null, []), // Phone Number
        'email': new FormControl(null, [Validators.email]), // Email
        'country': new FormControl(null, []), // Country Code
      }),
      'procurement': new FormGroup({
        'title': new FormControl(null, []),
        'description': new FormControl(null, []),
        'fileRefNumber': new FormControl(null, [])
      })
    });
  }

  ngOnInit() {

    this.dataService.getCountries()
      .then(res => {
        this.countries = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getProcedureTypes()
      .then(res => {
        this.procedureTypes = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

  }

  ngOnChanges(changes: SimpleChanges) {
    console.log(this.dataService.receivedNoticeNumber);
  }

  onProcedureSubmit() {
    // console.log(form.value);
    this.dataService.CADetails.cacountry = this.procedureForm.get('details.country').value;
    this.dataService.CADetails.receivedNoticeNumber = this.procedureForm.get('pub.receivedNoticeNumber').value;
    //this.dataService.CADetails.postalAddress.countryCode = form.value.CACountry;
    this.dataService.PostalAddress.countryCode = this.procedureForm.get('details.country').value;
    this.dataService.CADetails.postalAddress = this.dataService.PostalAddress;
    this.dataService.CADetails.contactingDetails = this.dataService.ContactingDetails;
    console.log(this.dataService.CADetails);
    // this.dataService.procedureSubmit(this.eoRelatedCriteria, this.reductionCriteria);
  }


}
