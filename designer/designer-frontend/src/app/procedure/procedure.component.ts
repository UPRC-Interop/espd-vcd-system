import {Component, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import {DataService} from '../services/data.service';
import {ProcedureType} from '../model/procedureType.model';
import {Country} from '../model/country.model';
import {NgForm} from '@angular/forms/forms';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {ReductionCriterion} from '../model/reductionCriterion.model';

@Component({
  selector: 'app-procedure',
  templateUrl: './procedure.component.html',
  styleUrls: ['./procedure.component.css']
})
export class ProcedureComponent implements OnInit, OnChanges {

  countries: Country[] = null;
  procedureTypes: ProcedureType[] = null;
  eoRelatedCriteria: EoRelatedCriterion[] = null;
  reductionCriteria: ReductionCriterion[] = null;


  constructor(public dataService: DataService) {
  }

  ngOnInit() {

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

    // Get predefined eoRelated Criteria
    this.dataService.getEoRelatedCriteria()
      .then(res => {
        this.eoRelatedCriteria = res;
        // console.log(this.eoRelatedCriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getReductionCriteria()
      .then(res => {
        this.reductionCriteria = res;
        // console.log(this.reductionCriteria);
      })
      .catch(err => {
        console.log(err);
      });

  }

  ngOnChanges(changes: SimpleChanges) {
    console.log(this.dataService.receivedNoticeNumber);
  }

  onProcedureSubmit(form: NgForm) {
    // console.log(form.value);
    this.dataService.CADetails.cacountry = form.value.CACountry;
    this.dataService.CADetails.receivedNoticeNumber = form.value.receivedNoticeNumber;
    console.log(this.dataService.CADetails);
    this.dataService.procedureSubmit(this.eoRelatedCriteria, this.reductionCriteria);
  }


}
