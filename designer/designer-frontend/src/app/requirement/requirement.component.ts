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

import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Requirement} from '../model/requirement.model';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {Currency} from '../model/currency.model';
import {ApicallService} from '../services/apicall.service';
import {UtilitiesService} from '../services/utilities.service';
import {EoIDType} from '../model/eoIDType.model';
import {MatChipInputEvent, MatRadioGroup, MatSelectionList} from '@angular/material';
import {BidType} from '../model/bidType.model';
import {FinancialRatioType} from '../model/financialRatioType.model';
import {COMMA, ENTER} from '../../../node_modules/@angular/cdk/keycodes';

@Component({
  selector: 'app-requirement',
  templateUrl: './requirement.component.html',
  styleUrls: ['./requirement.component.css']
})
export class RequirementComponent implements OnInit, OnChanges {

  @Input() req: Requirement;
  @Input() form: FormGroup;

  @Output() indicatorChanged = new EventEmitter();
  // @Output() lotsInReq = new EventEmitter();

  reqLots: string[] = [];
  countries: Country[] = null;
  currency: Currency[] = null;
  eoIDTypes: EoIDType[] = null;
  bidTypes: BidType[] = null;
  financialRatioTypes: FinancialRatioType[] = null;
  // evaluationMethodTypes: EvaluationMethodType[] = null;
  cpvCodes: string[] = [];
  isWeighted = false;
  /* CPV chips */
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  disabled = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

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

    if (this.req.responseDataType === 'WEIGHT_INDICATOR' && this.utilities.isImport()) {
      this.isWeighted = this.utilities.criterionWeightIndicators[this.req.uuid];
    }


    if (this.req.responseDataType === 'CODE' && this.req.responseValuesRelatedArtefact === 'CPVCodes' && this.utilities.isImport()) {
      // init cpvCodes when import
      this.cpvCodes = this.utilities.renderCpvTemplate[this.req.uuid];

      /* Make Chips non editable when user is EO and is requirement */
      if (this.utilities.isEO && this.req.type === 'REQUIREMENT') {
        this.disabled = true;
        this.removable = false;
      }
      /* make cpvTemplate with chips that are pre-existing at the imported artifact */
      if (this.cpvCodes !== undefined) {
        this.utilities.cpvTemplate[this.req.uuid] = this.utilities.cpvCodeToString(this.cpvCodes);
      }
    }

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

    this.dataService.getBidTypes()
      .then(res => {
        this.bidTypes = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getFinancialRatioTypes()
      .then(res => {
        this.financialRatioTypes = res;
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
  }


  /* SELF-CONTAINED: CODE with CPVCodes as responseValuesRelatedArtefact */
  createChips() {
    this.utilities.cpvTemplate[this.req.uuid] = this.utilities.cpvCodeToString(this.cpvCodes);
    console.log(this.utilities.cpvTemplate);
    // console.log(this.utilities.cpvTemplate['0157cebc-4ba4-4d65-9a6e-3cd5d57a08fb-34']);
  }


  pushSelectedLot() {
    if (this.lots.selectedOptions.selected !== undefined) {
      this.utilities.lotTemplate[this.req.uuid] = this.utilities.createLotListInCriterion(this.lots.selectedOptions.selected);
      console.log(this.utilities.lotTemplate);
      // console.log(this.utilities.lotTemplate['270317fa-6790-42ec-8f1a-575d82ed1d63-27']);
      // this.reqLots = this.utilities.createLotListInCriterion(this.lots.selectedOptions.selected);
      // console.log(this.reqLots);
    }
  }

  /* CPV Chip handling */
  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our cpv
    if ((value || '').trim()) {
      if (this.cpvCodes !== null && this.cpvCodes !== undefined) {
        this.cpvCodes.push(value.trim());
      }
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(cpv: string): void {
    const index = this.cpvCodes.indexOf(cpv);

    if (index >= 0) {
      this.cpvCodes.splice(index, 1);
    }
    /* re-create chip template when chips are removed */
    this.createChips();
  }


}
