import {Injectable} from '@angular/core';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {FormGroup} from '@angular/forms';
import {RequirementResponse} from '../model/requirement-response.model';
import {Evidence} from '../model/evidence.model';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import * as moment from 'moment';
import {DataService} from './data.service';

@Injectable({
  providedIn: 'root'
})
export class FormUtilService {

  constructor(private dataService: DataService) {
  }


  getFromForm(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any, evidenceList: Evidence[]) {

    if (rg != null || rg !== undefined) {
      // console.log('reqGroup ' + rg.uuid);

      if (rg.requirements !== undefined || rg.requirements != null) {

        rg.requirements.forEach(req => {
          if (req != null || req !== undefined) {
            // console.log('requirement uuid ' + req.uuid);
            console.log(formValues[req.uuid.valueOf()]);
            req.response = new RequirementResponse();
            if (req.responseDataType === 'INDICATOR') {
              if (formValues[req.uuid.valueOf()] === true) {
                req.response.indicator = true;
                req.response.uuid = null;
              } else {
                req.response.indicator = false;
                req.response.uuid = null;
              }
            } else if (req.responseDataType === 'DESCRIPTION') {
              req.response.description = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'EVIDENCE_URL') {
              req.response.evidenceURL = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'EVIDENCE_IDENTIFIER') {
              // req.response.evidenceSuppliedId = formValues[req.uuid.valueOf()];
              req.response.evidenceSuppliedId = req.id;
              req.response.validatedCriterionPropertyID = req.id;
              const evidenceUrlID = req.uuid + 'evidenceUrl';
              const evidenceCodeID = req.uuid + 'evidenceCode';
              const evidenceIssuerID = req.uuid + 'evidenceIssuer';

              // create evidence
              let evidence = new Evidence();
              let evidenceIssuer = new EvidenceIssuer();
              evidence.id = req.id;

              // fill in workaround
              if (formValues[evidenceUrlID.valueOf()] === null) {
                evidence.evidenceURL = '';
              } else {
                evidence.evidenceURL = formValues[evidenceUrlID.valueOf()];
              }
              if (formValues[evidenceCodeID.valueOf()] === null) {
                evidence.description = '';
              } else {
                evidence.description = formValues[evidenceCodeID.valueOf()];
              }
              if (formValues[evidenceIssuerID.valueOf()] === null) {
                evidenceIssuer.name = '';
              } else {
                evidenceIssuer.name = formValues[evidenceIssuerID.valueOf()];
              }
              // evidence.evidenceURL = formValues[evidenceUrlID.valueOf()];
              // evidence.description = formValues[evidenceCodeID.valueOf()];
              // evidenceIssuer.name = formValues[evidenceIssuerID.valueOf()];
              evidenceIssuer.website = '';
              evidenceIssuer.id = null;
              evidence.evidenceIssuer = evidenceIssuer;
              evidence.confidentialityLevelCode = 'PUBLIC';

              console.log(evidence);
              // check if evidence already exists, if exists edit evidence object else push new evidence

              const evi = evidenceList.find((ev, i) => {
                if (ev.id === evidence.id) {
                  evidenceList[i] = evidence;
                  return true;
                }
              });
              if (!evi) {
                evidenceList.push(evidence);
              }
              console.log(evidenceList);
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
              // req.response.date.setMinutes( req.response.date.getMinutes() + req.response.date.getTimezoneOffset() );
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
              console.log(formValues[currencyid.valueOf()]);
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
        // console.log('This is Rg ID out ' + firstRgId);
        rg.requirementGroups.forEach(rg2 => {
          // console.log('outer reqgroup id ' + rg.uuid);
          // console.log('inner reqgroup id ' + rg2.uuid);
          if (rg.uuid == firstRgId) {
            // console.log('Reset to first ReqGroup ');
            formValues = firstRgFormValues;
          }
          // fix
          if (formValues[rg2.uuid.valueOf()] != undefined) {
            formValues = formValues[rg2.uuid.valueOf()];
          }
          console.log(formValues);
          // console.log('reqGroup inside curs ' + rg2.uuid);
          this.getFromForm(rg2, cr, form, formValues, evidenceList);
        });
      }
    }
  }

  extractFormValuesFromCriteria(criteria: EoRelatedCriterion[], form: FormGroup, evidenceList: Evidence[]) {
    criteria.forEach(cr => {
      let formValues = form.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.uuid.valueOf()];
      // console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        // console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = form.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          // console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.uuid.valueOf()];
          this.getFromForm(rg, cr, form, testFormValues, evidenceList);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          // console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.getFromForm(rg, cr, form, formValues, evidenceList);
        }
      });
    });
  }


}
