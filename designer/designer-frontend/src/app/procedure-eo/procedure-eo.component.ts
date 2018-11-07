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

import {Component, Input, OnChanges, OnInit, SimpleChange, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormArray, FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';
import * as moment from 'moment';
import {Moment} from 'moment';
import {Evidence} from '../model/evidence.model';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {FormUtilService} from '../services/form-util.service';
import {UtilitiesService} from '../services/utilities.service';
import {ProjectType} from '../model/projectType.model';
import {EoRoleType} from '../model/eoRoleType.model';
import {Currency} from '../model/currency.model';
import {Amount} from '../model/amount.model';

@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit {


  public EOForm: FormGroup;

  countries: Country[] = null;
  currency: Currency[] = null;
  procedureTypes: ProcedureType[] = null;
  projectTypes: ProjectType[] = null;
  eoRoleTypes: EoRoleType[] = null;


  constructor(public dataService: DataService, public utilities: UtilitiesService) {

    this.EOForm = new FormGroup({
      'name': new FormControl(this.dataService.EODetails.name),
      'smeIndicator': new FormControl(false),
      'employeeQuantity': new FormControl(),
      'eoRole': new FormControl(),
      'generalTurnover': new FormGroup({
        'amount': new FormControl(),
        'currency': new FormControl()
      }),
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

    /* OTHER_EO_LOT TENDERED CRITERION DEMO ---> TODO: CREATE LOT in EO view */
    this.utilities.projectLots = this.utilities.createLotList(this.dataService.CADetails.procurementProjectLots);


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

    this.dataService.getProjectTypes()
      .then(res => {
        this.projectTypes = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getEORoleTypes()
      .then(res => {
        this.eoRoleTypes = res;
      })
      .catch(err => {
          console.log(err);
        }
      );

    this.dataService.getCurrency()
      .then(res => {
        this.currency = res;
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

    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;
    this.dataService.PostalAddress.countryCode = form.value.CACountry;
    this.dataService.CADetails.postalAddress = this.dataService.PostalAddress;
    this.dataService.CADetails.contactingDetails = this.dataService.ContactingDetails;

    console.log(this.dataService.selectedEOCountry);

    console.log(this.dataService.CADetails);
    this.dataService.EODetails = eoForm.value;
    console.log(this.dataService.EODetails);
  }

}
