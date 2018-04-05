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
  requirementAGroups: RequirementGroup[] = null;
  CriteriaA: ExclusionCriteria = new ExclusionCriteria();
  CriteriaFull: ExclusionCriteria[] = [];
  public formA = new FormGroup({});

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getExclusionACriteria()
      .then(res => {
        this.exclusionACriteria = res;
        this.formA = this.dataService.createExclusionCriterionFormTest(this.exclusionACriteria);
        console.log(this.formA);
        // this.exclusionACriteria.forEach(criteria => {
        //   // this.requirementAGroups[criteria.typeCode] = criteria.requirementGroups;
        //   // console.log(criteria);
        //   // this.CriteriaA['requirementGroups'] = criteria.requirementGroups;
        //   this.CriteriaA = criteria;
        //   this.CriteriaFull.push(this.CriteriaA);
        //
        // });
        // for (let i = 0; i < this.exclusionACriteria.length; i++) {
        //   // this.CriteriaA[i].requirementGroups = this.exclusionACriteria[i].requirementGroups;
        //   // this.CriteriaA.requirementGroups = [];
        //   // this.CriteriaA.requirementGroups = this.exclusionACriteria[i].requirementGroups;
        //   // this.CriteriaFull.push(this.CriteriaA);
        //
        // }

        // this.exclusionACriteria.forEach(criteria => {
        //   this.requirementAGroups = criteria.requirementGroups;
        //   // console.log(criteria);
        //
        // });
        //
        //
        // this.formA = this.dataService.createForm(this.requirementAGroups);
        // console.log(this.formA);

      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionBCriteria()
      .then(res => {
        this.exclusionBCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionCCriteria()
      .then(res => {
        this.exclusionCCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionDCriteria()
      .then(res => {
        this.exclusionDCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }

  onExclusionEOSubmit(form: NgForm) {
    console.log(form.value);
    console.log(this.formA.value);
    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

}
