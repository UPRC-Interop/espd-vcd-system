import {LegislationReference} from "./legislationReference.model";
export class ExclusionCriteria {
  typeCode:string;
  name:string;
  description:string;
  selected:boolean;
  legislationReference:LegislationReference;
}
