import {Component, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {DataService} from '../services/data.service';
import {FormGroup, NgForm} from '@angular/forms';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';

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
        // console.log(this.formA);
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


  reqGroupMatch(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any) {

    if (rg != null || rg != undefined) {
      // console.log('reqGroup ' + rg.id);

      if (rg.requirements != undefined || rg.requirements != null) {

        rg.requirements.forEach(req => {
          if (req != null || req != undefined) {
            // console.log('requirement id ' + req.id);
            console.log(formValues[req.id.valueOf()]);
            req.response = new RequirementResponse();
            if (req.responseDataType == 'INDICATOR') {
              if (formValues[req.id.valueOf()] == 'YES') {
                req.response.indicator = true;
                req.response.id = null;
              } else {
                req.response.indicator = false;
                req.response.id = null;
              }
            } else if (req.responseDataType == 'DESCRIPTION') {
              req.response.description = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'EVIDENCE_URL') {
              req.response.evidenceURL = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'CODE') {
              req.response.evidenceURLCode = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'DATE') {
              req.response.date = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'PERCENTAGE') {
              req.response.percentage = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'PERIOD') {
              req.response.period = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'CODE_COUNTRY') {
              req.response.countryCode = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'AMOUNT') {
              req.response.amount = formValues[req.id.valueOf()];
              req.response.currency = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'QUANTITY_INTEGER') {
              req.response.quantity = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'QUANTITY') {
              req.response.quantity = formValues[req.id.valueOf()];
              req.response.id = null;
            } else if (req.responseDataType == 'QUANTITY_YEAR') {
              req.response.year = formValues[req.id.valueOf()];
              req.response.id = null;
            }
          }
        });
      }
      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {

        let firstRgFormValues = null;
        let firstRgId = rg.id;
        firstRgFormValues = formValues;
        console.log('This is Rg ID out ' + firstRgId);
        rg.requirementGroups.forEach(rg2 => {
          console.log('outer reqgroup id ' + rg.id);
          console.log('inner reqgroup id ' + rg2.id);
          if (rg.id == firstRgId) {
            console.log('Reset to first ReqGroup ');
            formValues = firstRgFormValues;
          }
          // fix
          if (formValues[rg2.id.valueOf()] != undefined) {
            formValues = formValues[rg2.id.valueOf()];
          }
          console.log(formValues);
          // console.log('reqGroup inside curs ' + rg2.id);
          this.reqGroupMatch(rg2, cr, form, formValues);
        });
      }


    }

  }


  onExclusionEOSubmit() {
    // console.log(form.value);
    // console.log(this.formA.value);
    let formValues = this.formA.getRawValue();
    console.log(formValues);
    console.log(this.exclusionACriteria);

    this.exclusionACriteria.forEach(cr => {
      let formValues = this.formA.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);

        if (testFormValues == null) {
          testFormValues = this.formA.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, formValues);
        }


        // formValues = formValues[rg.id.valueOf()];
        // console.log(formValues);
        // this.reqGroupMatch(rg, cr, this.formA, formValues);
      });
    });

    this.exclusionBCriteria.forEach(cr => {
      let formValues = this.formB.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formB, formValues);
      });
    });

    this.exclusionCCriteria.forEach(cr => {
      let formValues = this.formC.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formC, formValues);
      });
    });

    this.exclusionDCriteria.forEach(cr => {
      let formValues = this.formD.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formD, formValues);
      });
    });

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
