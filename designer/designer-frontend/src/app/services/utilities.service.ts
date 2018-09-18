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

import { Injectable } from '@angular/core';
import {Moment} from 'moment';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class UtilitiesService {
  isCA = false;
  isEO = false;
  isImportESPD = false;
  isCreateResponse = false;
  isCreateNewESPD = false;
  isReviewESPD = false;

  constructor() { }

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


}
