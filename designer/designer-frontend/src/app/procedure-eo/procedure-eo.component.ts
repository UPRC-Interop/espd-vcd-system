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
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormArray, FormControl, FormGroup, NgForm} from '@angular/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {FormUtilService} from '../services/form-util.service';
import {UtilitiesService} from '../services/utilities.service';

@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit {

  @ViewChildren('form') forms: QueryList<NgForm>;

  public EOForm: FormGroup;
  // public formA = new FormGroup({});
  @Input() formA: FormGroup;
  @Input() formD: FormGroup;
  @Input() formC: FormGroup;
  // public formC = new FormGroup({});
  // public formD = new FormGroup({});
  test = true;

  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  // eoRelatedACriteria: EoRelatedCriterion[] = null;
  @Input() eoRelatedACriteria: EoRelatedCriterion[];
  @Input() eoRelatedCCriteria: EoRelatedCriterion[];
  @Input() eoRelatedDCriteria: EoRelatedCriterion[];

  constructor(public dataService: DataService, public formUtil: FormUtilService, public utilities: UtilitiesService) {
    this.EOForm = new FormGroup({
      'name': new FormControl(this.dataService.EODetails.name),
      'smeIndicator': new FormControl(false),
      'postalAddress': new FormGroup({
        'addressLine1': new FormControl(),
        'postCode': new FormControl(),
        'city': new FormControl(),
        'countryCode': new FormControl(this.dataService.selectedEOCountry),
      }),
      'contactingDetails': new FormGroup({
        'contactPointName': new FormControl(),
        'emailAddress': new FormControl(),
        'faxNumber': new FormControl(),
        'telephoneNumber': new FormControl(),
      }),
      'naturalPersons': new FormArray([this.initNaturalPerson()]),
      'id': new FormControl(),
      'webSiteURI': new FormControl(),
      'procurementProjectLot': new FormControl(0)
    });
    this.dataService.EOForm = this.EOForm;

  }

  ngOnInit() {

    // make EODetails and Natural Person forms non editable if user selects review ESPD
    if (this.dataService.isReadOnly()) {
      this.EOForm.disable();
    }


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


  /* ================================================= natural person form ================================================ */

  initNaturalPerson() {
    return new FormGroup({
      'firstName': new FormControl(),
      'familyName': new FormControl(),
      'role': new FormControl(),
      'birthPlace': new FormControl(),
      'birthDate': new FormControl(),
      'postalAddress': new FormGroup({
        'addressLine1': new FormControl(),
        'postCode': new FormControl(),
        'city': new FormControl(),
        'countryCode': new FormControl(),
      }),
      'contactDetails': new FormGroup({
        'contactPointName': new FormControl(),
        'emailAddress': new FormControl(),
        'telephoneNumber': new FormControl(),
      })
    });
  }

  addPerson() {
    const control = <FormArray>this.EOForm.controls['naturalPersons'];
    control.push(this.initNaturalPerson());
  }

  removePerson(i: number) {
    const control = <FormArray>this.EOForm.controls['naturalPersons'];
    control.removeAt(i);
  }

  getNaturalPersonFormData() {
    return <FormArray>this.EOForm.controls['naturalPersons'];
  }


  /* ====================================================== Getting values from Form ======================================*/


  onProcedureEOSubmit(form: NgForm, eoForm: FormGroup) {
    // console.log(form.value);
    // console.log(eoForm.value);
    // const formValues = this.formA.getRawValue();
    // console.log(formValues);

    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedACriteria, this.formA, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedCCriteria, this.formC, this.formUtil.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.eoRelatedDCriteria, this.formD, this.formUtil.evidenceList);


    console.log(this.eoRelatedACriteria);
    console.log(this.eoRelatedCCriteria);
    console.log(this.eoRelatedDCriteria);

    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;
    this.dataService.PostalAddress.countryCode = form.value.CACountry;
    this.dataService.CADetails.postalAddress = this.dataService.PostalAddress;
    this.dataService.CADetails.contactingDetails = this.dataService.ContactingDetails;

    console.log(this.dataService.selectedEOCountry);

    console.log(this.dataService.CADetails);
    this.dataService.EODetails = eoForm.value;
    console.log(this.dataService.EODetails);
    // console.log(this.dataService.CADetails);
    this.dataService.procedureEOSubmit(this.eoRelatedACriteria,
      this.eoRelatedCCriteria,
      this.eoRelatedDCriteria);
  }

}
