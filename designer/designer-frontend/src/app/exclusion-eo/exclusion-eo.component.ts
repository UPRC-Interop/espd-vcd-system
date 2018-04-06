import {Component, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {DataService} from '../services/data.service';
import {FormGroup, NgForm} from '@angular/forms';
import {RequirementGroup} from '../model/requirementGroup.model';

@Component({
  selector: 'app-exclusion-eo',
  templateUrl: './exclusion-eo.component.html',
  styleUrls: ['./exclusion-eo.component.css']
})
export class ExclusionEoComponent implements OnInit {

  exclusionACriteria: ExclusionCriteria[] = null;
  exclusionBCriteria: ExclusionCriteria[] = null;
  exclusionCCriteria: ExclusionCriteria[] = null;
  exclusionDCriteria: ExclusionCriteria[] = null;

  public formA = new FormGroup({});
  public formB = new FormGroup({});
  public formC = new FormGroup({});
  public formD = new FormGroup({});

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getExclusionACriteria()
      .then(res => {
        this.exclusionACriteria = res;
        this.formA = this.dataService.createExclusionCriterionForm(this.exclusionACriteria);
        console.log(this.formA);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionBCriteria()
      .then(res => {
        this.exclusionBCriteria = res;
        this.formB = this.dataService.createExclusionCriterionForm(this.exclusionBCriteria);
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionCCriteria()
      .then(res => {
        this.exclusionCCriteria = res;
        this.formC = this.dataService.createExclusionCriterionForm(this.exclusionCCriteria);
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionDCriteria()
      .then(res => {
        this.exclusionDCriteria = res;
        this.formD = this.dataService.createExclusionCriterionForm(this.exclusionDCriteria);
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }

  onExclusionEOSubmit(form: NgForm) {
    // console.log(form.value);
    // console.log(this.formA.value);
    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

}
