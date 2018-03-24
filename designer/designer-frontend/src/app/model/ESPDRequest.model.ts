import {Cadetails} from "./caDetails.model";
import {ExclusionCriteria} from "./exclusionCriteria.model";
import {SelectionCriteria} from "./selectionCriteria.model";
export class ESPDRequest {
  cadetails:Cadetails
  exclusionCriteria:ExclusionCriteria[];
  selectionCriteria:SelectionCriteria[];
}
