import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ReductionCriterion} from '../model/reductionCriterion.model';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-finish-eo',
  templateUrl: './finish-eo.component.html',
  styleUrls: ['./finish-eo.component.css']
})
export class FinishEoComponent implements OnInit {

  reductionCriteria: ReductionCriterion[] = null;
  public form = new FormGroup({});

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.dataService.getReductionCriteria()
      .then(res => {
        this.reductionCriteria = res;
        this.form = this.dataService.createReductionCriterionForm(this.reductionCriteria);

      })
      .catch(err => {
        console.log(err);
      });
  }

  exportFile() {
    this.dataService.saveFile(this.dataService.blob);
  }

}
