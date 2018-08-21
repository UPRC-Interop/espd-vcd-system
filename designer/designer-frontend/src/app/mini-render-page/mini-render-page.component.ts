import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {FormGroup} from '@angular/forms';
import {ApicallService} from '../services/apicall.service';

@Component({
  selector: 'app-mini-render-page',
  templateUrl: './mini-render-page.component.html',
  styleUrls: ['./mini-render-page.component.css']
})
export class MiniRenderPageComponent implements OnInit {

  // exclusionACriteria: ExclusionCriteria[] = null;
  // public exclusionACriteriaForm: FormGroup = null;

  constructor(public dataService: DataService, private APIService: ApicallService) {
  }

  ngOnInit() {
    this.APIService.version = 'v2';

    this.APIService.getExclusionCriteria_A().then(res => {
      this.dataService.exclusionACriteria = res;
      this.dataService.exclusionACriteriaForm = this.dataService.createExclusionCriterionForm(this.dataService.exclusionACriteria);
      console.log(this.dataService.exclusionACriteriaForm);
    }).catch(err => {
      console.log(err);
    });


  }

}
