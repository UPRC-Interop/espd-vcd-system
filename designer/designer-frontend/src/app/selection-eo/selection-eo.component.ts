import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {FormControl, FormGroup} from '@angular/forms';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {FormUtilService} from '../services/form-util.service';

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

  constructor(public dataService: DataService,
              public formUtil: FormUtilService) {
  }

  ngOnInit() {
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



  onSelectionEOSubmit() {

    this.formUtil.extractFormValuesFromCriteria(this.selectionACriteria, this.formA, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionBCriteria, this.formB, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionCCriteria, this.formC, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionDCriteria, this.formD, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.dataService.selectionALLCriteria, this.dataService.selectionALLCriteriaForm, this.dataService.evidenceList);

    console.log("Selection A Criteria");
    console.log(this.selectionACriteria);
    console.log("Selection B Criteria");
    console.log(this.selectionBCriteria);
    console.log("Selection C Criteria");
    console.log(this.selectionCCriteria);
    console.log("Selection D Criteria");
    console.log(this.selectionDCriteria);
    console.log("Selection ALL Criteria");
    console.log(this.isSatisfiedALL);

    this.dataService.selectionEOSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }
}
