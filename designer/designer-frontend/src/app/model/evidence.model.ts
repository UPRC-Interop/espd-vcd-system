import {EvidenceIssuer} from './evidenceIssuer.model';

export class Evidence {
  description?: string;
  confidentialityLevelCode?: string;
  evidenceURL?: string;
  id: string;
  evidenceIssuer?: EvidenceIssuer;
}
