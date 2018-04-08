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
        this.eoRelatedACriteria = res;
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


    this.EOForm = new FormGroup({
      'name': new FormControl(this.dataService.EODetails.name),
      'smeIndicator': new FormControl(false),
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
      'id': new FormControl(),
      'websiteURI': new FormControl(),
      'eoRelatedCriteria': new FormArray([])

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

  ngOnChanges(changes: SimpleChanges) {
    this.EOForm.patchValue({
      'name': this.dataService.selectedEOCountry
    });
  }

  reqGroupMatch(rg: RequirementGroup, cr: EoRelatedCriterion, form: FormGroup, formValues: any) {

    if (rg != null || rg != undefined) {
      // console.log(formValues[cr.id.valueOf()]);
      console.log('reqGroup ' + rg.id);
      // console.log(cr.id);
      // console.log((<FormGroup>form.controls[cr.id]).controls[rg.id]);
      // console.log(formValues[cr.id.valueOf()][rg.id.valueOf()]);

      if (rg.requirements != undefined || rg.requirements != null) {

        rg.requirements.forEach(req => {
          if (req != null || req != undefined) {
            console.log('requirement id ' + req.id);
            console.log(formValues);
            // if (formValues[req.id.valueOf()] == undefined) {
            //   console.log(formValues.valueOf());
            // }
            console.log(formValues[req.id.valueOf()]);
            // req.response = new RequirementResponse();
          }

        });
      }

      if (rg.requirementGroups != null || rg.requirementGroups != undefined) {
        rg.requirementGroups.forEach(rg2 => {
          console.log('outer reqgroup id ' + rg.id);
          console.log('inner reqgroup id ' + rg2.id);
          // console.log(formValues);
          // console.log(formValues[cr.id.valueOf()]);
          // console.log(formValues[cr.id.valueOf()][rg.id.valueOf()]);
          // console.log(formValues[cr.id.valueOf()][rg.id.valueOf()][rg2.id.valueOf()]);
          // formValues = formValues[cr.id.valueOf()][rg.id.valueOf()][rg2.id.valueOf()];

          // fix
          if (formValues[rg2.id.valueOf()] != undefined) {
            formValues = formValues[rg2.id.valueOf()];
          }

          console.log(formValues);
          console.log('reqGroup inside curs ' + rg2.id);
          this.reqGroupMatch(rg2, cr, form, formValues);
        });
      }


    }

  }


  onProcedureEOSubmit(form: NgForm, eoForm: FormGroup) {
    let formValues = this.formA.getRawValue();
    console.log(formValues);
    // console.log(formValues['2043338f-a38a-490b-b3ec-2607cb25a017']);

    this.eoRelatedACriteria.forEach(cr => {
      let formValues = this.formA.getRawValue();
      // console.log(formValues[cr.id.valueOf()]);
      // console.log(cr.id.valueOf());
      formValues = formValues[cr.id.valueOf()];
      cr.requirementGroups.forEach(rg => {
        formValues = formValues[rg.id.valueOf()];
        this.reqGroupMatch(rg, cr, this.formA, formValues);
      });


    });

    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;

    // TODO put form values to dataService
    console.log(this.dataService.selectedEOCountry);
    // console.log(this.dataService.CADetails);
  }

}
