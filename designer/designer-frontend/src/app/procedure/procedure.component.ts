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
import {NgForm} from '@angular/forms/forms';

@Component({
  selector: 'app-procedure',
  templateUrl: './procedure.component.html',
  styleUrls: ['./procedure.component.css']
})
export class ProcedureComponent implements OnInit, OnChanges {

  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  // @Input() eoRelatedCriteria: EoRelatedCriterion[];
  // @Input() reductionCriteria: ReductionCriterion[];


  constructor(public dataService: DataService) {
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

    // Get predefined eoRelated Criteria
    // this.dataService.getEoRelatedCriteria()
    //   .then(res => {
    //     this.eoRelatedCriteria = res;
    //     // console.log(this.eoRelatedCriteria);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });

    // this.dataService.getReductionCriteria()
    //   .then(res => {
    //     this.reductionCriteria = res;
    //     // console.log(this.reductionCriteria);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });

  }

  ngOnChanges(changes: SimpleChanges) {
    console.log(this.dataService.receivedNoticeNumber);
  }

  onProcedureSubmit(form: NgForm) {
    // console.log(form.value);
    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;
    // this.dataService.CADetails.postalAddress.countryCode = form.value.CACountry;
    this.dataService.PostalAddress.countryCode = form.value.CACountry;
    this.dataService.CADetails.postalAddress = this.dataService.PostalAddress;
    this.dataService.CADetails.contactingDetails = this.dataService.ContactingDetails;
    console.log(this.dataService.CADetails);
    // this.dataService.procedureSubmit(this.eoRelatedCriteria, this.reductionCriteria);
  }


}
