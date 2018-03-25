import {Cadetails} from "./caDetails.model";
import {ExclusionCriteria} from "./exclusionCriteria.model";
import {SelectionCriteria} from "./selectionCriteria.model";
export class ESPDRequest {
  cadetails:Cadetails;
  exclusionACriteria:ExclusionCriteria[];
  exclusionBCriteria:ExclusionCriteria[];
  exclusionCCriteria:ExclusionCriteria[];
  exclusionDCriteria:ExclusionCriteria[];
  selectionALLCriteria:SelectionCriteria[];
  selectionACriteria:SelectionCriteria[];
  selectionBCriteria:SelectionCriteria[];
  selectionCCriteria:SelectionCriteria[];
  selectionDCriteria:SelectionCriteria[];

  constructor(cadetails:Cadetails,
              exclusionACriteria:ExclusionCriteria[],
              exclusionBCriteria:ExclusionCriteria[],
              exclusionCCriteria:ExclusionCriteria[],
              exclusionDCriteria:ExclusionCriteria[],
              selectionALLCriteria?:SelectionCriteria[],
              selectionACriteria?:SelectionCriteria[],
              selectionBCriteria?:SelectionCriteria[],
              selectionCCriteria?:SelectionCriteria[],
              selectionDCriteria?:SelectionCriteria[])
  {
    this.cadetails=cadetails;
    this.exclusionACriteria=exclusionACriteria;
    this.exclusionBCriteria=exclusionBCriteria;
    this.exclusionCCriteria=exclusionCCriteria;
    this.exclusionDCriteria=exclusionDCriteria;
    this.selectionALLCriteria=selectionALLCriteria;
    this.selectionACriteria=selectionACriteria;
    this.selectionBCriteria=selectionBCriteria;
    this.selectionCCriteria=selectionCCriteria;
    this.selectionDCriteria=selectionDCriteria;

  }
}
