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

import {AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Requirement} from '../model/requirement.model';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {Currency} from '../model/currency.model';
import {ApicallService} from '../services/apicall.service';
import {UtilitiesService} from '../services/utilities.service';
import {EoIDType} from '../model/eoIDType.model';
import {MatChipInputEvent, MatListOption, MatSelectionList} from '@angular/material';
import {BidType} from '../model/bidType.model';
import {FinancialRatioType} from '../model/financialRatioType.model';
import {COMMA, ENTER} from '../../../node_modules/@angular/cdk/keycodes';
import {SelectionModel} from '@angular/cdk/collections';

@Component({
  selector: 'app-requirement',
  templateUrl: './requirement.component.html',
  styleUrls: ['./requirement.component.css']
})
export class RequirementComponent implements OnInit, OnChanges, AfterViewInit {

  @Input() req: Requirement;
  @Input() form: FormGroup;

  @Output() indicatorChanged = new EventEmitter();
  // @Output() lotsInReq = new EventEmitter();

  // reqLots: string[] = ['Lot1', 'Lot2', 'Lot3'];
  countries: Country[] = [];
  currency: Currency[] = [];
  eoIDTypes: EoIDType[] = [];
  bidTypes: BidType[] = [];
  financialRatioTypes: FinancialRatioType[] = [];
  // evaluationMethodTypes: EvaluationMethodType[] = null;
  cpvCodes: string[] = [];
  isWeighted = false;
  /* CPV chips */
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
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

    if (this.req.responseDataType === 'CODE' && this.req.responseValuesRelatedArtefact === 'CPVCodes' && this.utilities.isImportESPD) {
      // init cpvCodes when import
      this.cpvCodes = this.utilities.renderCpvTemplate[this.req.uuid];
    }

    // this.dataService.getCountries()
    //   .then(res => {
    //     this.countries = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getCurrency()
    //   .then(res => {
    //     this.currency = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getEoIDTypes()
    //   .then(res => {
    //     this.eoIDTypes = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getBidTypes()
    //   .then(res => {
    //     this.bidTypes = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
    //
    // this.dataService.getFinancialRatioTypes()
    //   .then(res => {
    //     this.financialRatioTypes = res;
    //     // console.log(res);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });

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

  ngAfterViewInit() {
    if (this.req.responseDataType === 'LOT_IDENTIFIER') {
      console.log('LOT MODEL for req.uuid: ' + this.req.uuid);
      console.log(this.utilities.renderLotTemplate[this.req.uuid]);
      // this.lots.selectedOptions = new SelectionModel<MatListOption>(true);
      // console.log(this.lots);
      // console.log(this.lots.selectedOptions);
      // console.log(this.lots.selectedOptions.selected);
      // this.importSelectedLots();
    }
  }

  /* SELF-CONTAINED: CODE with CPVCodes as responseValuesRelatedArtefact */
  createChips() {
    this.utilities.cpvTemplate[this.req.uuid] = this.utilities.cpvCodeToString(this.cpvCodes);
  }


  pushSelectedLot() {
    if (this.lots.selectedOptions.selected !== undefined) {
      this.utilities.lotTemplate[this.req.uuid] = this.utilities.createLotListInCriterion(this.lots.selectedOptions.selected);
      console.log(this.utilities.lotTemplate);
      // console.log(this.lots);
    }
  }

  importSelectedLots() {
    if (this.req.responseDataType === 'LOT_IDENTIFIER' && this.utilities.isImport()) {
      /* test lot import */
      // this.utilities.lotToSelectedMatListOption(this.reqLots, this.lots);
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
  }

  onNgModelChange(event: Event) {
    console.log('MODEL CHANGE for req.uuid: ' + this.req.uuid );
    console.log(this.utilities.renderLotTemplate[this.req.uuid]);
  }


}
