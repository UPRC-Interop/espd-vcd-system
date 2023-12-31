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

import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';
import {NaturalPerson} from './naturalPerson.model';
import {Amount} from './amount.model';

export class EoDetails {
  electronicAddressID: string;
  webSiteURI: string;
  name: string;
  smeIndicator: boolean;
  postalAddress: PostalAddress;
  contactingDetails: ContactingDetails;
  naturalPersons: NaturalPerson[];
  procurementProjectLot: number;
  id: string;
  employeeQuantity?: number;
  eoGroupName?: string;
  generalTurnover?: Amount;
  eoRole: string;
}
