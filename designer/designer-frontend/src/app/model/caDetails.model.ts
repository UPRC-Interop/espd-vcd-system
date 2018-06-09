import {PostalAddress} from './postalAddress.model';
import {ContactingDetails} from './contactingDetails.model';

export class Cadetails {
  procurementProcedureTitle: string;
  procurementProcedureDesc: string;
  procurementProcedureFileReferenceNo: string;
  procurementPublicationNumber: string;
  electronicAddressID?: string;
  webSiteURI?: string;
  postalAddress?: PostalAddress;
  contactingDetails?: ContactingDetails;
  procurementPublicationURI: string;
  id: string;
  caofficialName: string;
  cacountry: string;
  receivedNoticeNumber?: string;
  nationalOfficialJournal?: string;
  procurementProcedureType?: string;

}
