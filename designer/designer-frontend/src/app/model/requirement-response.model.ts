///
/// Copyright 2016-2019 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {Moment} from 'moment';

export class RequirementResponse {
  indicator?: boolean;
  date?: Moment;
  description: string;
  evidenceURL?: string;
  evidenceURLCode?: string;
  code?: string[];
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
  validatedCriterionPropertyID?: string;
  mandatory?: boolean;
  multiple?: boolean;
  evaluationMethodType?: string;
  weight?: number;
  evaluationMethodDescription?: string;
  eoidtype?: string;
  lots?: string[];
}
