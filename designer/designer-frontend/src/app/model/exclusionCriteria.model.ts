import {LegislationReference} from './legislationReference.model';
import {RequirementGroup} from './requirementGroup.model';

export class ExclusionCriteria {
  typeCode: string;
  name: string;
  description: string;
  selected: boolean;
  legislationReference: LegislationReference;
  requirementGroups: RequirementGroup[];
  id: string;
}
