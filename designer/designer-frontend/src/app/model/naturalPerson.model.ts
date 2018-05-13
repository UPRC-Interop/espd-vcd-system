import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';
import {Moment} from 'moment';

export class NaturalPerson {
  firstName: string;
  familyName: string;
  role: string;
  birthPlace: string;
  birthDate: Moment;
  postalAddress: PostalAddress;
  contactDetails: ContactingDetails;
}
