import {Component, Input, OnInit} from '@angular/core';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {DataService} from '../services/data.service';
import {FormControl, NgForm} from '@angular/forms/forms';

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {

  @Input() selectionACriteria: SelectionCriteria[];
  @Input() selectionBCriteria: SelectionCriteria[];
  @Input() selectionCCriteria: SelectionCriteria[];
  @Input() selectionDCriteria: SelectionCriteria[];
  @Input() selectionALLCriteria: SelectionCriteria[];
  isSatisfiedALL: boolean = true;
  isAtoD: boolean = false;


  constructor(private dataService: DataService) {
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

  onSelectionSubmit(form: NgForm) {
    console.log(form.value);
    this.dataService.selectionSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }

}
