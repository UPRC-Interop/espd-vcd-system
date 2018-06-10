import {Cadetails} from './caDetails.model';
import {FullCriterion} from './fullCriterion.model';
import {EoDetails} from './eoDetails.model';
import {DocumentDetails} from './documentDetails.model';

export class ESPDResponse {
  cadetails: Cadetails;
  eodetails: EoDetails;
  fullCriterionList: FullCriterion[];
  documentDetails: DocumentDetails;

  constructor(cadetails: Cadetails, eodetails: EoDetails, fullCriterionList: FullCriterion[]) {
    this.cadetails = cadetails;
    this.eodetails = eodetails;
    this.fullCriterionList = fullCriterionList;
  }
}
