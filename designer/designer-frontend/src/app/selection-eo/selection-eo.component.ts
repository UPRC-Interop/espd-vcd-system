import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {RequirementResponse} from '../model/requirement-response.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import * as moment from 'moment';
import {Moment} from 'moment';
import {ApicallService} from '../services/apicall.service';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {Evidence} from '../model/evidence.model';

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit {
  // selectionALLCriteria: SelectionCriteria[] = null;


  @Input() selectionACriteria: SelectionCriteria[];
  @Input() selectionBCriteria: SelectionCriteria[];
  @Input() selectionCCriteria: SelectionCriteria[];
  @Input() selectionDCriteria: SelectionCriteria[];
  @Input() selectionALLCriteria: SelectionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formC: FormGroup;
  @Input() formD: FormGroup;
  @Input() formALL: FormGroup;
  isSatisfiedALL = true;
  isAtoD = false;

  constructor(public dataService: DataService, private APIService: ApicallService) {
  }

  ngOnInit() {


    // this.dataService.getSelectionALLCriteria()
    //   .then(res => {
    //     this.selectionALLCriteria = res;
    //     // this.formALL = this.dataService.createSelectionCriterionForm(this.selectionALLCriteria);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });
  }


  handleALL(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'YES') {
      this.isSatisfiedALL = false;
      this.isAtoD = true;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if (radio.value === 'NO') {
      this.isSatisfiedALL = true;
      this.isAtoD = false;
    }
  }

  reqGroupMatch(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any) {

    if (rg != null || rg !== undefined) {
      // console.log('reqGroup ' + rg.uuid);

      // let reqFormValues = null;
      // let firstReqRg = rg.uuid;
      // reqFormValues = formValues;
      if (rg.requirements !== undefined || rg.requirements != null) {


        rg.requirements.forEach(req => {
          if (req !== null || req !== undefined) {

            // if (reqFormValues == null) {
            //   // reqFormValues = form.getRawValue();
            //   // reqFormValues = reqFormValues[cr.uuid.valueOf()];
            //   // reqFormValues = reqFormValues[rg.uuid.valueOf()];
            //   // reqFormValues = reqFormValues[req.uuid.valueOf()];
            // }

            // if (formValues[req.uuid.valueOf()] == undefined) {
            //   formValues = reqFormValues[firstReqRg.valueOf()];
            // }


            // console.log('requirement id ' + req.uuid);
            console.log(formValues[req.uuid.valueOf()]);
            // console.log(formValues[req.id.valueOf()]);
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
                evidence.description = '';
              } else {
                evidence.description = formValues[evidenceIssuerID.valueOf()];
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

              const evi = this.dataService.evidenceList.find((ev, i) => {
                if (ev.id === evidence.id) {
                  this.dataService.evidenceList[i] = evidence;
                  console.log('inside if');
                  return true;
                }
              });
              if (!evi) {
                this.dataService.evidenceList.push(evidence);
              }

              console.log(this.dataService.evidenceList);
              // console.log(JSON.stringify(this.dataService.evidenceList));

              req.response.uuid = null;
            } else if (req.responseDataType == 'CODE') {
              req.response.evidenceURLCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'DATE') {
              req.response.date = formValues[req.uuid.valueOf()];
              // console.log('CHECKING DATE-----------------------------------------------');
              // console.log(req.response.date);
              // console.log(typeof req.response.date);
              if (typeof req.response.date !== 'string' && req.response.date !== null && req.response.date !== undefined) {
                const utcDate = this.dataService.toUTCDate(req.response.date);
                req.response.date = moment(utcDate);
              }
              req.response.uuid = null;
            } else if (req.responseDataType == 'PERCENTAGE') {
              req.response.percentage = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'PERIOD' && this.APIService.version === 'v1') {
              req.response.period = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'PERIOD' && this.APIService.version === 'v2') {
              // req.response.period = formValues[req.uuid.valueOf()];
              const startDateid = req.uuid + 'startDate';
              req.response.startDate = formValues[startDateid.valueOf()];
              if (typeof req.response.startDate !== 'string' && req.response.startDate !== null) {
                const utcDate = this.dataService.toUTCDate(req.response.startDate);
                req.response.startDate = moment(utcDate);
              }
              console.log(req.response.startDate);
              const endDateid = req.uuid + 'endDate';
              req.response.endDate = formValues[endDateid.valueOf()];
              if (typeof req.response.endDate !== 'string' && req.response.endDate !== null) {
                const utcDate = this.dataService.toUTCDate(req.response.endDate);
                req.response.endDate = moment(utcDate);
              }
              console.log(req.response.endDate);

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

  onSelectionEOSubmit() {

    this.selectionACriteria.forEach(cr => {
      let formValues = this.formA.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      console.log(formValues);

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = this.formA.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, formValues);
        }
      });
    });

    this.selectionBCriteria.forEach(cr => {
      let formValues = this.formB.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      console.log(formValues);
      // console.log('THIS IS FORM B =================================');

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);
        if (testFormValues == null) {
          testFormValues = this.formB.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');

          // fix
          let testFormValues = null;
          testFormValues = this.formB.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formB, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formB, formValues);
        }
      });
    });

    this.selectionCCriteria.forEach(cr => {
      let formValues = this.formC.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      // console.log('THIS IS FORM C =================================');
      console.log(formValues);

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          // fix
          let testFormValues = null;
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formC, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formC, formValues);
        }
      });
    });

    this.selectionDCriteria.forEach(cr => {
      let formValues = this.formD.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      console.log(formValues);
      // console.log('THIS IS FORM D ==============================');

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = this.formD.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formD, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formD, formValues);
        }
      });
    });

    this.dataService.selectionALLCriteria.forEach(cr => {
      let formValues = this.dataService.selectionALLCriteriaForm.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];
      // console.log('THIS IS FORM C =================================');
      console.log(formValues);

      // let testFormValues = formValues[cr.uuid.valueOf()];
      console.log('cr loop: ' + cr.uuid);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.uuid);

        if (testFormValues == null) {
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          // fix
          let testFormValues = null;
          testFormValues = this.dataService.selectionALLCriteriaForm.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.dataService.selectionALLCriteriaForm, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.dataService.selectionALLCriteriaForm, formValues);
        }
      });
    });

    console.log(this.selectionACriteria);
    console.log(this.selectionBCriteria);
    console.log(this.selectionCCriteria);
    console.log(this.selectionDCriteria);
    console.log(this.isSatisfiedALL);
    this.dataService.selectionEOSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }
}
