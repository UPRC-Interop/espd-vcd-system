import {Cadetails} from './caDetails.model';
import {ExclusionCriteria} from './exclusionCriteria.model';
import {SelectionCriteria} from './selectionCriteria.model';
import {FullCriterion} from './fullCriterion.model';

export class ESPDRequest {
  cadetails: Cadetails;
  fullCriterionList: FullCriterion[];

  constructor(cadetails: Cadetails, fullCriterionList: FullCriterion[]) {
    this.cadetails = cadetails;
    this.fullCriterionList = fullCriterionList;
  }
}
