import {RequirementResponse} from './requirement-response.model';

export class Requirement {
  description: string;
  ruleset?: string;
  typecode?: string;
  responseDataType: string;
  response?: RequirementResponse;
  id: string;
  uuid: string;
}
