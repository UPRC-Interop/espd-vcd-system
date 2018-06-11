import {Cadetails} from './caDetails.model';
import {FullCriterion} from './fullCriterion.model';
import {DocumentDetails} from './documentDetails.model';

export class ESPDRequest {
  cadetails: Cadetails;
  fullCriterionList: FullCriterion[];
  documentDetails: DocumentDetails;

  constructor(cadetails: Cadetails, fullCriterionList: FullCriterion[]) {
    this.cadetails = cadetails;
    this.fullCriterionList = fullCriterionList;
  }
}
