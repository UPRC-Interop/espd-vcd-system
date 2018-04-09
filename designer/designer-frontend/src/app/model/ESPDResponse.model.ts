import {Cadetails} from './caDetails.model';
import {FullCriterion} from './fullCriterion.model';
import {EoDetails} from './eoDetails.model';

export class ESPDResponse {
  cadetails: Cadetails;
  eodetails: EoDetails;
  fullCriterionList: FullCriterion[];

  constructor(cadetails: Cadetails, fullCriterionList: FullCriterion[]) {
    this.cadetails = cadetails;
    this.fullCriterionList = fullCriterionList;
  }
}
