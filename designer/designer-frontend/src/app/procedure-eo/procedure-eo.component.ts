import {Component, Input, OnChanges, OnInit, SimpleChange, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormArray, FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';
import * as moment from 'moment';
import {Moment} from 'moment';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {TOOPDialogComponent} from '../toopdialog/toopdialog.component';


@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit {


  public EOForm: FormGroup;
  // public formA = new FormGroup({});
  @Input() formA: FormGroup;
  @Input() formD: FormGroup;
  @Input() formC: FormGroup;
  // public formC = new FormGroup({});
  // public formD = new FormGroup({});
  test = true;

  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  // eoRelatedACriteria: EoRelatedCriterion[] = null;
  @Input() eoRelatedACriteria: EoRelatedCriterion[];
  @Input() eoRelatedCCriteria: EoRelatedCriterion[];
  @Input() eoRelatedDCriteria: EoRelatedCriterion[];

  constructor(public dataService: DataService, public dialog: MatDialog) {
    this.EOForm = new FormGroup({
      'name': new FormControl(this.dataService.EODetails.name),
      'smeIndicator': new FormControl(this.dataService.EODetails.smeIndicator),
      'postalAddress': new FormGroup({
        'addressLine1': new FormControl(),
        'postCode': new FormControl(),
        'city': new FormControl(),
        'countryCode': new FormControl(this.dataService.selectedEOCountry),
      }),
      'contactingDetails': new FormGroup({
        'contactPointName': new FormControl(),
        'emailAddress': new FormControl(),
        'faxNumber': new FormControl(),
        'telephoneNumber': new FormControl(),
      }),
      'naturalPersons': new FormArray([this.initNaturalPerson()]),
      'id': new FormControl(),
      'webSiteURI': new FormControl()
    });
    this.dataService.EOForm = this.EOForm;
  }

  ngOnInit() {


    // this.dataService.getEoRelatedCriteria()
    //   .then(res => {
    //     this.eoRelatedCriteria = res;
    //     // this.createControls(this.eoRelatedCriteria);
    //   })
    //   .catch(err => {
    //     console.log(err);
    //   });

    this.dataService.getCountries()
      .then(res => {
        this.countries = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getProcedureTypes()
      .then(res => {
        this.procedureTypes = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }


  /* ================================================= natural person form ================================================ */

  initNaturalPerson() {
    return new FormGroup({
      'firstName': new FormControl(),
      'familyName': new FormControl(),
      'role': new FormControl(),
      'birthPlace': new FormControl(),
      'birthDate': new FormControl(),
      'postalAddress': new FormGroup({
        'addressLine1': new FormControl(),
        'postCode': new FormControl(),
        'city': new FormControl(),
        'countryCode': new FormControl(),
      }),
      'contactDetails': new FormGroup({
        'contactPointName': new FormControl(),
        'emailAddress': new FormControl(),
        'telephoneNumber': new FormControl(),
      })
    });
  }

  addPerson() {
    const control = <FormArray>this.EOForm.controls['naturalPersons'];
    control.push(this.initNaturalPerson());
  }

  removePerson(i: number) {
    const control = <FormArray>this.EOForm.controls['naturalPersons'];
    control.removeAt(i);
  }

  getNaturalPersonFormData() {
    return <FormArray>this.EOForm.controls['naturalPersons'];
  }


  /* ====================================================== Getting values from Form ======================================*/

  reqGroupMatch(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any) {

    if (rg != null || rg != undefined) {
      // console.log('reqGroup ' + rg.uuid);

      if (rg.requirements != undefined || rg.requirements != null) {

        rg.requirements.forEach(req => {
          if (req != null || req != undefined) {
            // console.log('requirement uuid ' + req.uuid);

            console.log(formValues[req.uuid.valueOf()]);
            req.response = new RequirementResponse();

            if (req.responseDataType == 'INDICATOR') {
              // console.log(formValues[req.uuid.valueOf()]);
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
            } else if (req.responseDataType == 'CODE') {
              req.response.evidenceURLCode = formValues[req.uuid.valueOf()];
              req.response.uuid = null;
            } else if (req.responseDataType == 'DATE') {
              req.response.date = formValues[req.uuid.valueOf()];
              console.log(req.response.date);
              if (typeof req.response.date !== 'string') {
                const utcDate = this.dataService.toUTCDate(req.response.date);
                req.response.date = moment(utcDate);
              }
              // req.response.date.setMinutes( req.response.date.getMinutes() + req.response.date.getTimezoneOffset() );

              console.log(req.response.date);
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
              // const currencyid = req.uuid + 'currency';
              // req.response.currency = formValues[currencyid.valueOf()];
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
            }


          }

        });
      }

      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {

        let firstRgFormValues = null;
        let firstRgId = rg.uuid;
        firstRgFormValues = formValues;
        // console.log('This is ID out ' + firstRgId);
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
          this.reqGroupMatch(rg2, cr, form, formValues);
        });
      }


    }

  }


  onProcedureEOSubmit(form: NgForm, eoForm: FormGroup) {
    console.log(form.value);
    console.log(eoForm.value);
    let formValues = this.formA.getRawValue();
    console.log(formValues);

    this.eoRelatedACriteria.forEach(cr => {
      let formValues = this.formA.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        // formValues = formValues[rg.uuid.valueOf()];
        // this.reqGroupMatch(rg, cr, this.formA, formValues);
        if (testFormValues == null) {
          testFormValues = this.formA.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          // console.log('THIS IS undefined');
          testFormValues = testFormValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          // console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formA, formValues);
        }
      });
    });

    this.eoRelatedCCriteria.forEach(cr => {
      let formValues = this.formC.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        if (testFormValues == null) {
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          // fix
          let testFormValues = null;
          testFormValues = this.formC.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formC, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formC, formValues);
        }
      });
    });

    this.eoRelatedDCriteria.forEach(cr => {
      let formValues = this.formD.getRawValue();
      formValues = formValues[cr.uuid.valueOf()];

      let testFormValues = null;

      cr.requirementGroups.forEach(rg => {
        if (testFormValues == null) {
          testFormValues = this.formD.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          // formValues = testFormValues;
        }

        if (formValues[rg.uuid.valueOf()] == undefined) {
          console.log('THIS IS undefined');
          // fix
          let testFormValues = null;
          testFormValues = this.formD.getRawValue();
          testFormValues = testFormValues[cr.uuid.valueOf()];
          testFormValues = testFormValues[rg.uuid.valueOf()];
          // fix

          this.reqGroupMatch(rg, cr, this.formD, testFormValues);
        } else if (formValues[rg.uuid.valueOf()] != undefined) {
          console.log('THIS IS DEFINED');
          formValues = formValues[rg.uuid.valueOf()];
          this.reqGroupMatch(rg, cr, this.formD, formValues);
        }
      });
    });

    console.log(this.eoRelatedACriteria);
    console.log(this.eoRelatedCCriteria);
    console.log(this.eoRelatedDCriteria);

    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;

    console.log(this.dataService.selectedEOCountry);

    console.log(this.dataService.CADetails);
    this.dataService.EODetails = eoForm.value;
    console.log(this.dataService.EODetails);
    // console.log(this.dataService.CADetails);
    this.dataService.procedureEOSubmit(this.eoRelatedACriteria,
      this.eoRelatedCCriteria,
      this.eoRelatedDCriteria);
  }


  openTOOPDialog() {
    console.log('this is toop test');
    const dialogRef = this.dialog.open(TOOPDialogComponent, {
      width: '500px',
      id: ''
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      console.log('closed dialog');
    });
  }

}
