import {Requirement} from "./requirement.model";
export class RequirementGroup{
  requirements:Requirement[];
  requirementGroup?:RequirementGroup[];
  ruleset?:string;
  condition?:string;
  id:string;
}
