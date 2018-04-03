import {RequirementResponse} from './requirement-response.model';

export class Requirement {
  description: string;
  responseDataType: string;
  response?: RequirementResponse;
  id: string;
}
