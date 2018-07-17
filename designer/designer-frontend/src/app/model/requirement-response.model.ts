import {Moment} from 'moment';

export class RequirementResponse {
  indicator?: boolean;
  date?: Moment;
  description: string;
  evidenceURL?: string;
  evidenceURLCode?: string;
  percentage?: string;
  period?: string;
  countryCode?: string;
  amount?: string;
  currency?: string;
  quantity?: number;
  year?: number;
  id?: string;
  uuid: string;
  startDate?: Moment;
  endDate?: Moment;
  evidenceSuppliedId?: string;
  identifier?: string;
  url?: string;
}
