import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {FormGroup} from '@angular/forms';
import {RequirementResponse} from '../model/requirement-response.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import * as moment from 'moment';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {Evidence} from '../model/evidence.model';

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
            console.log(formValues[req.uuid.valueOf()]);
            req.response = new RequirementResponse();
            if (req.responseDataType == 'INDICATOR') {
              if (formValues[req.uuid.valueOf()] == true) {
                req.response.indicator = true;
                req.response.uuid = null;
              } else {
                req.response.indicator = false;
                req.response.uuid = null;
              }
            } else if (req.responseDataType == 'DESCRIPTION') {
              req.response.description = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'EVIDENCE_URL') {
              req.response.evidenceURL = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'EVIDENCE_IDENTIFIER') {
              // req.response.evidenceSuppliedId = formValues[req.uuid.valueOf()];
              req.response.evidenceSuppliedId = req.id;
              req.response.id = req.id;
              req.response.validatedCriterionPropertyID = req.id;
              const evidenceUrlID = req.uuid + 'evidenceUrl';
              const evidenceCodeID = req.uuid + 'evidenceCode';
              const evidenceIssuerID = req.uuid + 'evidenceIssuer';

              // create evidence
              let evidence = new Evidence();
              let evidenceIssuer = new EvidenceIssuer();
              evidence.id = req.id;
              // fill in workaround
              // if (formValues[evidenceUrlID.valueOf()] === null) {
              //   evidence.evidenceURL = '';
              // } else {
              //   evidence.evidenceURL = formValues[evidenceUrlID.valueOf()];
              // }
              // if (formValues[evidenceCodeID.valueOf()] === null) {
              //   evidence.description = '';
              // } else {
              //   evidence.description = formValues[evidenceCodeID.valueOf()];
              // }
              // if (formValues[evidenceIssuerID.valueOf()] === null) {
              //   evidence.description = '';
              // } else {
              //   evidence.description = formValues[evidenceIssuerID.valueOf()];
              // }
              evidence.evidenceURL = formValues[evidenceUrlID.valueOf()];
              evidence.description = formValues[evidenceCodeID.valueOf()];
              evidenceIssuer.name = formValues[evidenceIssuerID.valueOf()];
              evidenceIssuer.website = null;
              evidenceIssuer.id = null;
              evidence.evidenceIssuer = evidenceIssuer;
              evidence.confidentialityLevelCode = 'PUBLIC';

              console.log(evidence);
              this.dataService.evidenceList.push(evidence);
              console.log(this.dataService.evidenceList);
              // console.log(JSON.stringify(this.dataService.evidenceList));

              req.response.uuid = null;
            } else if (req.responseDataType == 'CODE') {
              req.response.evidenceURLCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'DATE') {
              req.response.date = formValues[req.uuid.valueOf()];
              if (typeof req.response.date !== 'string') {
                const utcDate = this.dataService.toUTCDate(req.response.date);
                req.response.date = moment(utcDate);
              }
              req.response.uuid = null;
            } else if (req.responseDataType == 'PERCENTAGE') {
              req.response.percentage = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'PERIOD') {
              req.response.period = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'CODE_COUNTRY') {
              req.response.countryCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'AMOUNT') {
              req.response.amount = formValues[req.uuid.valueOf()];
              const currencyid = req.uuid + 'currency';
              req.response.currency = formValues[currencyid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'QUANTITY_INTEGER') {
              req.response.quantity = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'QUANTITY') {
              req.response.quantity = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'QUANTITY_YEAR') {
              req.response.year = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'IDENTIFIER') {
              req.response.identifier = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'URL') {
              req.response.url = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            }
          }
        });
      }
      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {

        let firstRgFormValues = null;
        let firstRgId = rg.uuid;
        firstRgFormValues = formValues;
        console.log('This is Rg ID out ' + firstRgId);
        rg.requirementGroups.forEach(rg2 => {
          console.log('outer reqgroup id ' + rg.uuid);
          console.log('inner reqgroup id ' + rg2.uuid);
          if (rg.uuid == firstRgId) {
            console.log('Reset to first ReqGroup ');
            formValues = firstRgFormValues;
          }
          // fix
          if (formValues[rg2.uuid.valueOf()] != undefined) {
            formValues = formValues[rg2.uuid.valueOf()];
          }
          console.log(formValues);
          // console.log('reqGroup inside curs ' + rg2.uuid);
          this.reqGroupMatch(rg2, cr, form, formValues);
        });
      }
    }
  }

  onExport() {
    // this.dataService.version = 'v1';
    this.reductionCriteria.forEach(cr => {
      let formValues = this.form.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = this.form.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          testFormValues = testFormValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.form, formValues);
        }
      });
    });
    this.dataService.finishEOSubmit(this.reductionCriteria);
  }


  // onExportV1() {
  //   this.dataService.version = 'v1';
  //   this.reductionCriteria.forEach(cr => {
  //     let formValues = this.form.getRawValue();
  //     formValues = formValues[cr.uuid.valueOf()];
  //     console.log(formValues);
  //
  //     // let testFormValues = formValues[cr.uuid.valueOf()];
  //     console.log('cr loop: ' + cr.uuid);
  //
  //     let testFormValues = null;
  //
  //     cr.requirementGroups.forEach(rg => {
  //       console.log('first rg loop: ' + rg.uuid);
  //
  //       if (testFormValues == null) {
  //         testFormValues = this.form.getRawValue();
  //         testFormValues = testFormValues[cr.uuid.valueOf()];
  //         // formValues = testFormValues;
  //       }
  //
  //       if (formValues[rg.uuid.valueOf()] == undefined) {
  //         testFormValues = testFormValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, testFormValues);
  //       } else if (formValues[rg.uuid.valueOf()] != undefined) {
  //         formValues = formValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, formValues);
  //       }
  //     });
  //   });
  //   this.dataService.finishEOSubmit(this.reductionCriteria);
  // }
  //
  // onExportV2() {
  //   this.dataService.version = 'v2';
  //   this.reductionCriteria.forEach(cr => {
  //     let formValues = this.form.getRawValue();
  //     formValues = formValues[cr.uuid.valueOf()];
  //     console.log(formValues);
  //
  //     // let testFormValues = formValues[cr.uuid.valueOf()];
  //     console.log('cr loop: ' + cr.uuid);
  //
  //     let testFormValues = null;
  //
  //     cr.requirementGroups.forEach(rg => {
  //       console.log('first rg loop: ' + rg.uuid);
  //
  //       if (testFormValues == null) {
  //         testFormValues = this.form.getRawValue();
  //         testFormValues = testFormValues[cr.uuid.valueOf()];
  //         // formValues = testFormValues;
  //       }
  //
  //       if (formValues[rg.uuid.valueOf()] == undefined) {
  //         testFormValues = testFormValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, testFormValues);
  //       } else if (formValues[rg.uuid.valueOf()] != undefined) {
  //         formValues = formValues[rg.uuid.valueOf()];
  //         this.reqGroupMatch(rg, cr, this.form, formValues);
  //       }
  //     });
  //   });
  //
  //
  //   this.dataService.finishEOSubmit(this.reductionCriteria);
  // }

}
