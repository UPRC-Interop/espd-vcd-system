import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';

export class ToopCompanyData {
  electronicAddressID: string;
  webSiteURI: string;
  name: string;
  smeIndicator: boolean;
  postalAddress: PostalAddress;
  contactingDetails: ContactingDetails;
  procurementProjectLot: number;
  id: string;
}
