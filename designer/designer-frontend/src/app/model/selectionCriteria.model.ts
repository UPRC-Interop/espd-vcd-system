import {LegislationReference} from "./legislationReference.model";
export class SelectionCriteria {
  typeCode:string;
  name:string;
  description:string;
  selected:boolean;
  legislationReference:LegislationReference;
}
