import {Injectable} from '@angular/core';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {FormControl, FormGroup} from '@angular/forms';
import {RequirementResponse} from '../model/requirement-response.model';
import {Evidence} from '../model/evidence.model';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import * as moment from 'moment';
import {DataService} from './data.service';
import {ApicallService} from './apicall.service';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {UtilitiesService} from './utilities.service';

@Injectable({
  providedIn: 'root'
})
export class FormUtilService {
  template = [];
  evidenceList: Evidence[] = [];


  constructor(private APIService: ApicallService,
              public utilities: UtilitiesService) {
  }


  getFromForm(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any, evidenceList: Evidence[]) {

    if (rg != null || rg !== undefined) {
      // console.log('reqGroup ' + rg.uuid);

      if (rg.requirements !== undefined || rg.requirements != null) {

        rg.requirements.forEach(req => {
          if (req != null || req !== undefined) {
            // console.log('requirement uuid ' + req.uuid);
            // console.log(formValues[req.uuid.valueOf()]);
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
              // Workaround: if EvidenceURL is not present, do not create the evidence

              req.response.evidenceSuppliedId = req.id;
              req.response.validatedCriterionPropertyID = req.id;
              const evidenceUrlID = req.uuid + 'evidenceUrl';
              const evidenceCodeID = req.uuid + 'evidenceCode';
              const evidenceIssuerID = req.uuid + 'evidenceIssuer';
              if (formValues[evidenceUrlID.valueOf()] !== null) {
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
              }

              // console.log(evidenceList);

              // console.log(JSON.stringify(this.dataService.evidenceList));
              req.response.uuid = null;
            } else if (req.responseDataType === 'CODE') {
              req.response.evidenceURLCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'DATE') {
              req.response.date = formValues[req.uuid.valueOf()];
              // console.log('CHECKING DATE-----------------------------------------------');
              // console.log(req.response.date);
              // console.log(typeof req.response.date);
              if (typeof req.response.date !== 'string' && req.response.date !== null && req.response.date !== undefined) {
                const utcDate = this.utilities.toUTCDate(req.response.date);
                req.response.date = moment(utcDate);
              }
              req.response.uuid = null;
            } else if (req.responseDataType === 'PERCENTAGE') {
              req.response.percentage = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'PERIOD' && this.APIService.version === 'v1') {
              req.response.period = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'PERIOD' && this.APIService.version === 'v2') {
              // req.response.period = formValues[req.uuid.valueOf()];
              const startDateid = req.uuid + 'startDate';
              req.response.startDate = formValues[startDateid.valueOf()];
              if (typeof req.response.startDate !== 'string' && req.response.startDate !== null) {
                const utcDate = this.utilities.toUTCDate(req.response.startDate);
                req.response.startDate = moment(utcDate);
              }
              // console.log(req.response.startDate);
              const endDateid = req.uuid + 'endDate';
              req.response.endDate = formValues[endDateid.valueOf()];
              if (typeof req.response.endDate !== 'string' && req.response.endDate !== null) {
                const utcDate = this.utilities.toUTCDate(req.response.endDate);
                req.response.endDate = moment(utcDate);
              }
              // console.log(req.response.endDate);

              req.response.uuid = null;
            } else if (req.responseDataType === 'CODE_COUNTRY') {
              req.response.countryCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType === 'AMOUNT') {
              const currencyid = req.uuid + 'currency';

              // AMOUNT quickfix
              if (formValues[req.uuid.valueOf()] === '' && formValues[currencyid.valueOf()] === null) {
                req.response = null;
              } else {
                req.response.amount = formValues[req.uuid.valueOf()];
                // console.log(formValues[currencyid.valueOf()]);
                req.response.currency = formValues[currencyid.valueOf()];
                req.response.uuid = null;
              }
            } else if (req.responseDataType === 'QUANTITY_INTEGER') {
              if (formValues[req.uuid.valueOf()] === '') {
                req.response = null;
              } else {
                req.response.quantity = formValues[req.uuid.valueOf()];
                req.response.uuid = null;
              }

            } else if (req.responseDataType === 'QUANTITY') {
              if (formValues[req.uuid.valueOf()] === '') {
                req.response = null;
              } else {
                req.response.quantity = formValues[req.uuid.valueOf()];
                req.response.uuid = null;
              }

            } else if (req.responseDataType === 'QUANTITY_YEAR') {
              if (formValues[req.uuid.valueOf()] === '') {
                req.response = null;
              } else {
                req.response.year = formValues[req.uuid.valueOf()];
                req.response.uuid = null;
              }

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
          // console.log(formValues);
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
      // console.log(formValues);

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

          // fix go up a level
          testFormValues = form.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          // console.log('THIS IS undefined');
          this.getFromForm(rg, cr, form, testFormValues, evidenceList);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          // console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.getFromForm(rg, cr, form, formValues, evidenceList);
        }
      });
    });
  }

  getReqGroups(rg: RequirementGroup) {
    if (rg !== null || rg !== undefined) {

      this.template[rg.id] = rg;

      // console.log('upper rg.id: ' + rg.id);
      // console.log(rg.id);
      if (rg.requirementGroups !== null || rg.requirementGroups !== undefined) {
        rg.requirementGroups.forEach(rg2 => {
          // console.log('inner for: ' + rg2.id);
          // console.log(this.template);
          // console.log(rg2.id);
          this.getReqGroups(rg2);
        });

      }
    }
  }

  // id as input parameter, to create formGroup for specific requirementGroup object
  createTemplateFormGroup(id: string): FormGroup {
    return this.toFormGroup(this.template[id]);
  }


  createExclusionCriterionForm(criteria: ExclusionCriteria[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);
    // console.log(fg);
    return fg;
  }

  createSelectionCriterionForm(criteria: SelectionCriteria[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);
    // console.log(fg);
    return fg;
  }

  createReductionCriterionForm(criteria: ReductionCriterion[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }

  createEORelatedCriterionForm(criteria: EoRelatedCriterion[]) {
    let group: any = {};
    criteria.forEach(cr => {
      group[cr.uuid] = this.createFormGroups(cr.requirementGroups);
      // console.log(group[cr.typeCode]);
    });
    let fg = new FormGroup(group);

    // console.log(fg);
    return fg;
  }


  createFormGroups(reqGroups: RequirementGroup[]) {
    let group: any = {};
    reqGroups.forEach(rg => {
      group[rg.uuid] = this.toFormGroup(rg);
    });
    // console.log(group);
    let fg = new FormGroup(group);
    // console.log(fg);
    return fg;
  }

  toFormGroup(rg: RequirementGroup) {
    let group: any = {};
    if (rg) {
      // console.log('In Req Group: ' + rg.id);
      if (rg.requirements != undefined) {
        rg.requirements.forEach(r => {
          if (r.response != null || r.response != undefined) {
            group[r.uuid] = new FormControl(r.response.description ||
              r.response.percentage || r.response.evidenceURL ||
              r.response.evidenceURLCode || r.response.countryCode ||
              r.response.period || r.response.quantity || r.response.year || r.response.url || r.response.identifier || '');


            // YES/NO if responseDataType is indicator then pass indicator value to formControl. (initial state problem fixed)
            if (r.responseDataType === 'INDICATOR') {
              group[r.uuid] = new FormControl(r.response.indicator);
            }

            if (r.response.date) {

              group[r.uuid] = new FormControl(r.response.date);
            }

            // FIX: starDate-endDate null value case when AtoD Criteria are selected
            if (r.response.startDate) {
              group[r.uuid + 'startDate'] = new FormControl(r.response.startDate);
            } else if (r.response.startDate === null) {
              group[r.uuid + 'startDate'] = new FormControl();
            }
            if (r.response.endDate) {
              group[r.uuid + 'endDate'] = new FormControl(r.response.endDate);
            } else if (r.response.endDate === null) {
              group[r.uuid + 'endDate'] = new FormControl();
            }

            if (r.response.evidenceSuppliedId) {

              // TODO find evidence in EvidenceList object and import it
              const evi = this.evidenceList.find((ev) => {
                if (ev.id === r.response.evidenceSuppliedId) {
                  // this.evidenceList[i].description = 'test';
                  return true;
                }
              });
              // console.log(evi);
              // console.log(typeof evi);

              group[r.uuid + 'evidenceUrl'] = new FormControl(evi.evidenceURL);
              group[r.uuid + 'evidenceCode'] = new FormControl(evi.description);
              group[r.uuid + 'evidenceIssuer'] = new FormControl(evi.evidenceIssuer.name);
            }

            if (r.response.currency || r.response.amount) {
              group[r.uuid] = new FormControl(r.response.amount);
              if (r.response.currency !== null && r.response.currency !== undefined) {
                group[r.uuid + 'currency'] = new FormControl(r.response.currency);
              }
            }
            // in case of request import
            if (r.response.currency === null || r.response.amount === '0') {
              group[r.uuid + 'currency'] = new FormControl();
            }


            // console.log(r.response);
          } else {
            r.response = new RequirementResponse();
            group[r.uuid] = new FormControl(r.response.description || '');

            // TODO: make util service that hold dataservice EO,CA etc...
            if (this.utilities.isEO) {
              if (r.responseDataType === 'INDICATOR') {
                group[r.uuid] = new FormControl(false);
              }
              if (r.responseDataType === 'AMOUNT') {
                group[r.uuid + 'currency'] = new FormControl();
              }

              // group[r.uuid + 'startDate'] = new FormControl();
              // group[r.uuid + 'endDate'] = new FormControl();
              if (r.responseDataType === 'PERIOD' && this.APIService.version === 'v2') {
                group[r.uuid + 'startDate'] = new FormControl();
                group[r.uuid + 'endDate'] = new FormControl();
              }
              if (r.responseDataType === 'EVIDENCE_IDENTIFIER') {
                group[r.uuid + 'evidenceUrl'] = new FormControl();
                group[r.uuid + 'evidenceCode'] = new FormControl();
                group[r.uuid + 'evidenceIssuer'] = new FormControl();
              }
            }

          }

          // console.log(group);
          // console.log(group[r.uuid]);
        });
      }
      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {
        rg.requirementGroups.forEach(rg => {
          // console.log('Req Group ' + rg.uuid);
          group[rg.uuid] = this.toFormGroup(rg);
        });
      }
    }
    let fg = new FormGroup(group);

    // console.log(fg.getRawValue());
    return fg;
  }


  pushToReqGroups(rg: RequirementGroup, temp: any) {
    if (rg !== null || rg !== undefined) {

      if (rg.requirementGroups !== null || rg.requirementGroups !== undefined) {


        if (rg.id === '7c637c0c-7703-4389-ba52-02997a055bd7') {
          const rgFound = rg.requirementGroups.find((reqGroup) => {
            // console.log('REQGROUP uuid: ' + reqGroup.uuid);
            // console.log('TEMP uuid: ' + temp.uuid);
            if (reqGroup.uuid === temp.uuid) {
              // console.log('Do not add cause it is already added...');
              return false;
            }
          });
          // console.log(rgFound);
          // if reqGroup with temp.uuid not found then push it
          if (!rgFound) {
            // console.log('not found, ADD it: ');
            console.log('Length before push: ' + rg.requirementGroups.length);
            rg.requirementGroups.push(temp);
            console.log('Length after push: ' + rg.requirementGroups.length);
            console.log(rg.requirementGroups);
          }
        }

        rg.requirementGroups.forEach(rg2 => {
          this.pushToReqGroups(rg2, temp);
        });

      }
    }
  }

}
