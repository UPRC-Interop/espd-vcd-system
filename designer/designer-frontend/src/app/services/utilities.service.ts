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

import {Injectable} from '@angular/core';
import {Moment} from 'moment';
import * as moment from 'moment';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {MatListOption, MatSnackBar} from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class UtilitiesService {
  isCA = false;
  isEO = false;
  isImportESPD = false;
  isImportReq = false;
  isCreateResponse = false;
  isCreateNewESPD = false;
  isReviewESPD = false;
  initLanguage = true;
  start = false;
  isReset = false;
  isStarted = false;
  satisfiedALLCriterionExists = false;
  isSatisfiedALL = true;
  isAtoD = false;
  qualificationApplicationType: string;
  isGloballyWeighted = false;
  isDividedIntoLots = false;
  projectLots = [];
  lotTemplate = [];
  cpvTemplate = [];
  renderCpvTemplate = [];
  criterionWeightIndicators = [];
  cpvString = [];
  type: string;
  selectedLang = 'en';
  // qualificationApplicationType = 'SELF-CONTAINED';
  // qualificationApplicationType = 'REGULATED';


  constructor(public snackBar: MatSnackBar) {
  }

  /*  ======================================== Date Manipulation ================================*/

  toUTCDate(date: Moment): Moment {
    if (date !== null && date !== undefined) {
      const utcDate = new Date(Date.UTC(date.toDate().getFullYear(),
        date.toDate().getMonth(),
        date.toDate().getDate(),
        date.toDate().getHours(),
        date.toDate().getMinutes()));

      return moment(utcDate);
    }
  }

  isEmpty(obj: Object): Boolean {
    if (Object.keys(obj).length === 0) {
      return true;
    } else {
      return false;
    }
  }

  findCriterion(criteria: SelectionCriteria[], id: string): boolean {

    const criterionFound = criteria.find((cr) => {
      return cr.id === id;
    });

    if (criterionFound) {
      return true;
    } else if (criterionFound === undefined) {
      return false;
    }

  }

  setAllFields(obj: Object, val: any) {
    Object.keys(obj).forEach(function (k) {
      obj[k] = val;
    });
  }

  createLotList(lots: number): string[] {
    const projectLotsList = [];
    for (let i = 1; i <= lots; i++) {
      projectLotsList.push('Lot' + i);
    }

    // console.log(projectLotsList);

    return projectLotsList;
  }

  createLotListInCriterion(lots: MatListOption[]): string[] {
    const reqLot = lots.map(lot => {
      return lot.value;
    });
    return reqLot;
    // console.log(reqLot);
  }

  cpvCodeToString(cpvs: string[]): string {
    return cpvs.join(',');
  }

  stringToCpvCode(cpvString: string): string[] {
    return cpvString.split(',');
  }

  // makeDummyESPDRequest(): ESPDRequest {
  //   const json = JSON.parse(this.requestJSON);
  //   return new ESPDRequest(json.cadetails, json.fullCriterionList, json.documentDetails);
  // }

  isImport(): boolean {
    return this.isImportReq || this.isImportESPD || this.isReviewESPD;
  }

  /* ============================ snackbar ===================================== */
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action);
  }

}
