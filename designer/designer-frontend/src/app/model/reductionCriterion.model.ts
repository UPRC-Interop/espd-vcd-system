import {LegislationReference} from './legislationReference.model';
import {RequirementGroup} from './requirementGroup.model';

export class ReductionCriterion {
  typeCode: string;
  name: string;
  description: string;
  selected: boolean;
  legislationReference: LegislationReference;
  RequirementGroups: RequirementGroup[];
}
