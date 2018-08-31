import {Component, Input, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {DataService} from '../services/data.service';
import {FormGroup, NgForm} from '@angular/forms';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import * as moment from 'moment';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {Evidence} from '../model/evidence.model';
import {FormUtilService} from '../services/form-util.service';

@Component({
  selector: 'app-exclusion-eo',
  templateUrl: './exclusion-eo.component.html',
  styleUrls: ['./exclusion-eo.component.css']
})
export class ExclusionEoComponent implements OnInit {

  @Input() exclusionACriteria: ExclusionCriteria[];
  @Input() exclusionBCriteria: ExclusionCriteria[];
  @Input() exclusionCCriteria: ExclusionCriteria[];
  @Input() exclusionDCriteria: ExclusionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formD: FormGroup;
  @Input() formC: FormGroup;


  constructor(public dataService: DataService, public formUtil: FormUtilService) {
  }

  ngOnInit() {

  }


  onExclusionEOSubmit() {
    this.formUtil.extractFormValuesFromCriteria(this.exclusionACriteria, this.formA, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionBCriteria, this.formB, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionCCriteria, this.formC, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.exclusionDCriteria, this.formD, this.dataService.evidenceList);


    console.log(this.exclusionACriteria);
    console.log(this.exclusionBCriteria);
    console.log(this.exclusionCCriteria);
    console.log(this.exclusionDCriteria);

    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

}
