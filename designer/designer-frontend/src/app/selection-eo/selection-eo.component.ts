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

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit {
  selectionALLCriteria: SelectionCriteria[] = null;
  isSatisfiedALL: boolean = true;
  isAtoD: boolean = false;

  @Input() selectionACriteria: SelectionCriteria[];
  @Input() selectionBCriteria: SelectionCriteria[];
  @Input() selectionCCriteria: SelectionCriteria[];
  @Input() selectionDCriteria: SelectionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formC: FormGroup;
  @Input() formD: FormGroup;


  constructor(public dataService: DataService) {
  }

  ngOnInit() {


    this.dataService.getSelectionALLCriteria()
      .then(res => {
        this.selectionALLCriteria = res;
        // this.formALL = this.dataService.createSelectionCriterionForm(this.selectionALLCriteria);
      })
      .catch(err => {
        console.log(err);
      });
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

    if (rg != null || rg != undefined) {
      // console.log('reqGroup ' + rg.id);

      // let reqFormValues = null;
      // let firstReqRg = rg.id;
      // reqFormValues = formValues;
      if (rg.requirements != undefined || rg.requirements != null) {


        rg.requirements.forEach(req => {
          if (req != null || req != undefined) {

            // if (reqFormValues == null) {
            //   // reqFormValues = form.getRawValue();
            //   // reqFormValues = reqFormValues[cr.id.valueOf()];
            //   // reqFormValues = reqFormValues[rg.id.valueOf()];
            //   // reqFormValues = reqFormValues[req.id.valueOf()];
            // }

            // if (formValues[req.id.valueOf()] == undefined) {
            //   formValues = reqFormValues[firstReqRg.valueOf()];
            // }


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
              // console.log('CHECKING DATE-----------------------------------------------');
              console.log(req.response.date);
              console.log(typeof req.response.date);
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

  onSelectionEOSubmit() {

    this.selectionACriteria.forEach(cr => {
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
      });
    });

    this.selectionBCriteria.forEach(cr => {
      let formValues = this.formB.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      console.log(formValues);
      // console.log('THIS IS FORM B =================================');

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);
        if (testFormValues == null) {
          testFormValues = this.formB.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          console.log('THIS IS undefined');

          // fix
          let testFormValues = null;
          testFormValues = this.formB.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          testFormValues = testFormValues[rg.id.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formB, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formB, formValues);
        }
      });
    });

    this.selectionCCriteria.forEach(cr => {
      let formValues = this.formC.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      // console.log('THIS IS FORM C =================================');
      console.log(formValues);

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);

        if (testFormValues == null) {
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          // fix
          let testFormValues = null;
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          testFormValues = testFormValues[rg.id.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formC, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formC, formValues);
        }
      });
    });

    this.selectionDCriteria.forEach(cr => {
      let formValues = this.formD.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      console.log(formValues);
      // console.log('THIS IS FORM D ==============================');

      // let testFormValues = formValues[cr.id.valueOf()];
      console.log('cr loop: ' + cr.id);

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        console.log('first rg loop: ' + rg.id);

        if (testFormValues == null) {
          testFormValues = this.formD.getRawValue();
          testFormValues = testFormValues[cr.id.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.id.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formD, testFormValues);
        } else if (formValues[rg.id.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.id.valueOf()];
          this.reqGroupMatch(rg, cr, this.formD, formValues);
        }
      });
    });

    console.log(this.selectionACriteria);
    console.log(this.selectionBCriteria);
    console.log(this.selectionCCriteria);
    console.log(this.selectionDCriteria);
    this.dataService.selectionEOSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }
}
