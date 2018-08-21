import {LegislationReference} from './legislationReference.model';
import {RequirementGroup} from './requirementGroup.model';
import {RequirementResponse} from './requirement-response.model';

export class ReductionCriterion {
  typeCode: string;
  name: string;
  description: string;
  selected: boolean;
  legislationReference: LegislationReference;
  requirementGroups: RequirementGroup[];
  id: string;
  uuid: string;
  criterionGroup: string;
  ruleset?: string;
  response?: RequirementResponse;
}
