import {Cadetails} from './caDetails.model';
import {FullCriterion} from './fullCriterion.model';
import {EoDetails} from './eoDetails.model';
import {DocumentDetails} from './documentDetails.model';
import {Evidence} from './evidence.model';

export class ESPDResponse {
  cadetails: Cadetails;
  eodetails: EoDetails;
  fullCriterionList: FullCriterion[];
  documentDetails: DocumentDetails;
  evidenceList: Evidence[];

  constructor(cadetails: Cadetails, eodetails: EoDetails, fullCriterionList: FullCriterion[], evidenceList: Evidence[]) {
    this.cadetails = cadetails;
    this.eodetails = eodetails;
    this.fullCriterionList = fullCriterionList;
    this.evidenceList = evidenceList;
  }
}
