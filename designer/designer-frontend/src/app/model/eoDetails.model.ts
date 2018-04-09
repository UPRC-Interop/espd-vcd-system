import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';
import {NaturalPerson} from './naturalPerson.model';

export class EoDetails {
  electronicAddressID: string;
  webSiteURI: string;
  name: string;
  smeIndicator: boolean;
  postalAddress: PostalAddress;
  contactingDetails: ContactingDetails;
  naturalPerson: NaturalPerson[];
  procurementProjectLot: number;
  id: string;
}
