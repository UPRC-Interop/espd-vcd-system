import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';

export class NaturalPerson {
  firstName: string;
  familyName: string;
  role: string;
  birthPlace: string;
  birthDate: Date;
  postalAddress: PostalAddress;
  contactDetails: ContactingDetails;
}
