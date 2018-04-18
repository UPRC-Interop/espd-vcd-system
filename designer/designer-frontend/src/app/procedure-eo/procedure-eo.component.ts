import {Component, OnChanges, OnInit, SimpleChange, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormArray, FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {RequirementResponse} from '../model/requirement-response.model';

@Component({
  selector: 'app-procedure-eo',
  templateUrl: './procedure-eo.component.html',
  styleUrls: ['./procedure-eo.component.css']
})
export class ProcedureEoComponent implements OnInit, OnChanges {

  public EOForm: FormGroup;
  public formA = new FormGroup({});
  public formC = new FormGroup({});
  public formD = new FormGroup({});

  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  eoRelatedACriteria: EoRelatedCriterion[] = null;
  eoRelatedCCriteria: EoRelatedCriterion[] = null;
  eoRelatedDCriteria: EoRelatedCriterion[] = null;

  constructor(public dataService: DataService) {
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
        'telephoneNumber': new FormControl(),
      }),
      'naturalPersons': new FormArray([this.initNaturalPerson()]),
      'id': new FormControl(),
      'webSiteURI': new FormControl()
    });
    this.dataService.EOForm = this.EOForm;
  }

  ngOnInit() {


    this.dataService.getEoRelatedCriteria()
      .then(res => {
        this.eoRelatedCriteria = res;
        // this.createControls(this.eoRelatedCriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getEoRelatedACriteria()
      .then(res => {

        if (this.dataService.isCreateResponse) {
          this.eoRelatedACriteria = res;
          console.log('This is create response');
        } else if (this.dataService.isImportESPD) {
          console.log('This is import');
          this.eoRelatedACriteria = this.dataService.eoRelatedACriteria;
          // this.eoRelatedACriteria = res;
          // TODO patchValue to EoForm
          if (this.dataService.EODetails) {
            console.log('this is before patch values');
            console.log(this.dataService.EODetails);

          }


        }


        this.formA = this.dataService.createEORelatedCriterionForm(this.eoRelatedACriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getEoRelatedCCriteria()
      .then(res => {
        this.eoRelatedCCriteria = res;
        this.formC = this.dataService.createEORelatedCriterionForm(this.eoRelatedCCriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getEoRelatedDCriteria()
      .then(res => {
        this.eoRelatedDCriteria = res;
        this.formD = this.dataService.createEORelatedCriterionForm(this.eoRelatedDCriteria);
      })
      .catch(err => {
        console.log(err);
      });


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

  ngOnChanges() {
    if (this.EOForm) {
      // this.EOForm.patchValue(this.dataService.EODetails);
    }

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
        // console.log('This is ID out ' + firstRgId);
        rg.requirementGroups.forEach(rg2 => {
          // console.log('outer reqgroup id ' + rg.id);
          // console.log('inner reqgroup id ' + rg2.id);


          if (rg.id == firstRgId) {
            // console.log('Reset to first ReqGroup ');
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


  onProcedureEOSubmit(form: NgForm, eoForm: FormGroup) {
    console.log(form.value);
    console.log(eoForm.value);
    let formValues = this.formA.getRawValue();
    console.log(formValues);

    this.eoRelatedACriteria.forEach(cr => {
      let formValues = this.formA.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formA, formValues);
      });
    });

    this.eoRelatedCCriteria.forEach(cr => {
      let formValues = this.formC.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formC, formValues);
      });
    });

    this.eoRelatedDCriteria.forEach(cr => {
      let formValues = this.formD.getRawValue();
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formD, formValues);
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

}
