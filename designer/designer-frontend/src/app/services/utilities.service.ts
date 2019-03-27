///
/// Copyright 2016-2019 University of Piraeus Research Center
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
import * as moment from 'moment';
import {Moment} from 'moment';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {MatListOption, MatSnackBar} from '@angular/material';
import {RequirementGroup} from '../model/requirementGroup.model';
import {FullCriterion} from '../model/fullCriterion.model';


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
  isSatisfiedALL = false;
  isAtoD = false;
  isSatisfiedALLSelected = false;
  qualificationApplicationType: string;
  isGloballyWeighted = false;
  isDividedIntoLots = false;
  projectLots = [];
  lotTemplate = [];
  cpvTemplate = [];
  renderCpvTemplate = [];
  criterionWeightIndicators = [];
  renderLotTemplate = [];
  cpvString = [];
  type: string;
  selectedLang = 'en';
  eCertisTemplate = [];
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
    // console.log(Object.keys(obj).length);
    if (Object.keys(obj).length === 0) {
      return true;
    } else {
      return false;
    }
  }

  isEmptyStringArray(arr: string[]): Boolean {
    return arr.every(item => item.trim() === '');
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


  getSatisfiesALLCriterion(criteria: SelectionCriteria[], id: string): SelectionCriteria {
    return criteria.find((cr) => {
      return cr.id === id;
    });
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

  isImport(): boolean {
    return this.isImportReq || this.isImportESPD || this.isReviewESPD;
  }

  CAReqExists(rg: RequirementGroup): boolean {
    let result: boolean;
    if (rg) {
      if (rg.requirements !== undefined) {
        result = rg.requirements.some(req => {
          return req.type === 'REQUIREMENT';
        });
      }
    }

    if (rg.requirementGroups != null || rg.requirementGroups != undefined) {
      rg.requirementGroups.forEach(rg => {
        // console.log('Req Group ' + rg.uuid);
        this.CAReqExists(rg);
      });
    }


    if (result) {
      return true;
    } else if (result === undefined) {
      return false;
    }
  }

  // makeDummyESPDRequest(): ESPDRequest {
  //   const json = JSON.parse(this.requestJSON);
  //   return new ESPDRequest(json.cadetails, json.fullCriterionList, json.documentDetails);
  // }

  /* Show/hide eCertis criteria and related buttons */

  createShowECertisTemplate(criteria: FullCriterion[]) {
    criteria.forEach(cr => {
      this.eCertisTemplate[cr.id] = false;
    });
  }

  toggleECertis(id: string) {
    this.eCertisTemplate[id] = !this.eCertisTemplate[id];
  }

  resetSubCriterionList(criteria: FullCriterion[]) {
     criteria.forEach(cr => {
       if (cr.subCriterionList.length > 0) {
         cr.subCriterionList.length = 0;
         this.eCertisTemplate[cr.id] = false;
       }
     });
    }


  /* ============================ snackbar ===================================== */
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action);
  }

}
