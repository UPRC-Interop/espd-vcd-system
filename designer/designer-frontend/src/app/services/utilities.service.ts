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
