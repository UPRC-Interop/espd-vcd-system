import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {SelectionCriteria} from '../model/selectionCriteria.model';

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit {
  selectionACriteria: SelectionCriteria[] = null;
  selectionBCriteria: SelectionCriteria[] = null;
  selectionCCriteria: SelectionCriteria[] = null;
  selectionDCriteria: SelectionCriteria[] = null;
  selectionALLCriteria: SelectionCriteria[] = null;
  isSatisfiedALL: boolean = true;
  isAtoD: boolean = false;

  public formA = new FormGroup({});
  public formB = new FormGroup({});
  public formC = new FormGroup({});
  public formD = new FormGroup({});


  constructor(private dataService: DataService) {
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

    this.dataService.getSelectionACriteria()
      .then(res => {
        this.selectionACriteria = res;
        this.formA = this.dataService.createSelectionCriterionForm(this.selectionACriteria);
      })
      .catch(err => {
        console.log(err);
      });


    this.dataService.getSelectionBCriteria()
      .then(res => {
        this.selectionBCriteria = res;
        this.formB = this.dataService.createSelectionCriterionForm(this.selectionBCriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getSelectionCCriteria()
      .then(res => {
        this.selectionCCriteria = res;
        this.formC = this.dataService.createSelectionCriterionForm(this.selectionCCriteria);
      })
      .catch(err => {
        console.log(err);
      });

    this.dataService.getSelectionDCriteria()
      .then(res => {
        this.selectionDCriteria = res;
        this.formD = this.dataService.createSelectionCriterionForm(this.selectionDCriteria);
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

  onSelectionEOSubmit() {


    this.dataService.selectionSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }
}
