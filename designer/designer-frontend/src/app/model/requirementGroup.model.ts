import {Requirement} from './requirement.model';

export class RequirementGroup {
  requirements: Requirement[];
  requirementGroups?: RequirementGroup[];
  ruleset?: string;
  condition?: string;
  id: string;
  uuid: string;
  typecode?: string;
}
