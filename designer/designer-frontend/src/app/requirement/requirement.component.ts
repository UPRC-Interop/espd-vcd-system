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

import {Component, Input, Output, OnInit, EventEmitter, OnChanges, AfterViewInit, ViewChild, ViewChildren} from '@angular/core';
import {FormArray, FormGroup} from '@angular/forms';
import {Requirement} from '../model/requirement.model';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {Currency} from '../model/currency.model';
import {ApicallService} from '../services/apicall.service';
import {UtilitiesService} from '../services/utilities.service';
import {EoIDType} from '../model/eoIDType.model';
import {EvaluationMethodType} from '../model/evaluationMethodType.model';
import {MatSelectionList} from '@angular/material';

@Component({
  selector: 'app-requirement',
  templateUrl: './requirement.component.html',
  styleUrls: ['./requirement.component.css']
})
export class RequirementComponent implements OnInit, OnChanges {

  @Input() req: Requirement;
  @Input() form: FormGroup;

  @Output() indicatorChanged = new EventEmitter();

  reqLots: string[] = [];
  countries: Country[] = null;
  currency: Currency[] = null;
  eoIDTypes: EoIDType[] = null;
  // evaluationMethodTypes: EvaluationMethodType[] = null;
  isWeighted = false;

  @ViewChild('lots') lots: MatSelectionList;

  constructor(public dataService: DataService, public APIService: ApicallService, public utilities: UtilitiesService) {
  }

  ngOnChanges() {
    if (this.req.responseDataType === 'INDICATOR') {
      this.indicatorChanged.emit(this.form.get(this.req.uuid).value);
    }
    // this.indicatorChanged.emit(this.form.get(this.req.uuid).value);

  }

  ngOnInit() {
    // if (this.lots !== undefined) {
    //   console.log(this.lots);
    // }

    this.dataService.getCountries()
      .then(res => {
        this.countries = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getCurrency()
      .then(res => {
        this.currency = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getEoIDTypes()
      .then(res => {
        this.eoIDTypes = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    // this.dataService.getEvalutationMethodTypes()
    //   .then(res => {
    //     this.evaluationMethodTypes = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });


    if (this.req.responseDataType === 'INDICATOR') {
      this.form.get(this.req.uuid)
        .valueChanges
        .subscribe(ev => {
          console.log('emit: ' + ev);
          // console.log(ev);
          // console.log(typeof ev);
          this.indicatorChanged.emit(ev);
        });
    } else {
      this.indicatorChanged.emit(true);
    }


    /* SELF-CONTAINED: WEIGHT_INDICATOR */
    if (this.req.responseDataType === 'WEIGHT_INDICATOR') {
      this.form.get(this.req.uuid)
        .valueChanges
        .subscribe(ev => {
          console.log('emit weight: ' + ev);
          // console.log(ev);
          this.isWeighted = ev;
          console.log(this.isWeighted);
          // console.log(typeof ev);
          // this.indicatorChanged.emit(ev);
        });
    }

    // if (this.lots !== undefined && this.lots.selectedOptions !== undefined && this.lots.selectedOptions.selected !== undefined) {
    //
    //   this.pushSelectedLot();
    // }

  }

  // createReqLot(index, lot) {
  //   // console.log('THIS IS INDEX: ' + index + 'THIS IS LOT: ' + lot.value);
  //   console.log(lot);
  //   console.log(lot.value);
  //   return lot.value;
  // }

  pushSelectedLot() {
    if (this.lots.selectedOptions.selected !== undefined) {
      this.reqLots = this.utilities.createLotListInCriterion(this.lots.selectedOptions.selected);
      // console.log(this.reqLots);
    }
  }

}
