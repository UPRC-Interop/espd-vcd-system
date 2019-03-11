import {LegislationReference} from './legislationReference.model';
import {RequirementGroup} from './requirementGroup.model';
import {RequirementResponse} from './requirement-response.model';
import {PropertyKeyMap} from './propertyKeyMap.model';
import {ECertisCriterion} from './eCertisCriterion.model';

export class CaRelatedCriterion {
  type: string;
  typeCode: string;
  name: string;
  description: string;
  selected: boolean;
  legislationReference: LegislationReference;
  requirementGroups: RequirementGroup[];
  id: string;
  uuid: string;
  subCriterionList?: ECertisCriterion[];
  criterionGroup: string;
  ruleset?: string;
  response?: RequirementResponse;
  propertyKeyMap: PropertyKeyMap;
}
