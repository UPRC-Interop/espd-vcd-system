import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {FormGroup} from '@angular/forms';
import {RequirementResponse} from '../model/requirement-response.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import * as moment from 'moment';

@Component({
  selector: 'app-finish-eo',
  templateUrl: './finish-eo.component.html',
  styleUrls: ['./finish-eo.component.css']
})
export class FinishEoComponent implements OnInit {

  @Input() reductionCriteria: ReductionCriterion[];
  @Input() form: FormGroup;

  constructor(public dataService: DataService) {
  }

  ngOnInit() {

  }

  reqGroupMatch(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any) {

    if (rg != null || rg != undefined) {
      if (rg.requirements != undefined || rg.requirements != null) {
        rg.requirements.forEach(req => {
          if (req != null || req != undefined) {
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
              if (typeof req.response.date !== 'string') {
                const utcDate = this.dataService.toUTCDate(req.response.date);
                req.response.date = moment(utcDate);
              }
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
              const currencyid = req.id + 'currency';
              req.response.currency = formValues[currencyid.valueOf()];
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

  onExportV1() {
    this.dataService.version = 'v1';
    this.reductionCriteria.forEach(cr => {
      let formValues = this.form.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);

        if (testFormValues == null) {
          testFormValues = this.form.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          testFormValues = testFormValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, formValues);
        }
      });
    });
    this.dataService.finishEOSubmit(this.reductionCriteria);
  }

  onExportV2() {
    this.dataService.version = 'v2';
    this.reductionCriteria.forEach(cr => {
      let formValues = this.form.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);

        if (testFormValues == null) {
          testFormValues = this.form.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          testFormValues = testFormValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, formValues);
        }
      });
    });
    this.dataService.finishEOSubmit(this.reductionCriteria);
  }

}
