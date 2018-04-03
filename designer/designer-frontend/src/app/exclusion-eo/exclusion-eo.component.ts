import {Component, OnInit} from '@angular/core';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {DataService} from '../services/data.service';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-exclusion-eo',
  templateUrl: './exclusion-eo.component.html',
  styleUrls: ['./exclusion-eo.component.css']
})
export class ExclusionEoComponent implements OnInit {

  exclusionACriteria: ExclusionCriteria[] = null;
  exclusionBCriteria: ExclusionCriteria[] = null;
  exclusionCCriteria: ExclusionCriteria[] = null;
  exclusionDCriteria: ExclusionCriteria[] = null;

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getExclusionACriteria()
      .then(res => {
        this.exclusionACriteria = res;
        // console.log("This is exclusionACriteria: ");
        console.log(this.exclusionACriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionBCriteria()
      .then(res => {
        this.exclusionBCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionCCriteria()
      .then(res => {
        this.exclusionCCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getExclusionDCriteria()
      .then(res => {
        this.exclusionDCriteria = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }

  onExclusionEOSubmit(form: NgForm) {
    console.log(form.value);
    this.dataService.exclusionSubmit(this.exclusionACriteria,
      this.exclusionBCriteria,
      this.exclusionCCriteria,
      this.exclusionDCriteria);
  }

}
