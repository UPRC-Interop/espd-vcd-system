import {Component, OnChanges, OnInit, SimpleChange, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {FormArray, FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';

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


  onProcedureEOSubmit(form: NgForm, eoForm: FormGroup, formA: FormGroup) {
    // console.log(form.value);
    console.log(eoForm.value);
    console.log(formA.getRawValue());
    console.log(formA);
    console.log(formA.controls);

    this.eoRelatedACriteria.forEach(cr => {
      console.log(formA.controls[cr.id]);
      cr.requirementGroups.forEach(rg => {
        // console.log(formA.controls[rg.id]);
        rg.requirementGroups.forEach(rg2 => {
          rg2.requirements.forEach(req => {
            console.log(req.description);
          });
        });
        rg.requirements.forEach(req => {
          console.log(req.description);
        });
      });
    });

    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;

    // TODO put form values to dataService
    console.log(this.dataService.selectedEOCountry);
    // console.log(this.dataService.CADetails);
  }

}
