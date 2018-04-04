import {Component, Input, Output, OnInit, EventEmitter, OnChanges} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Requirement} from '../model/requirement.model';

@Component({
  selector: 'app-requirement',
  templateUrl: './requirement.component.html',
  styleUrls: ['./requirement.component.css']
})
export class RequirementComponent implements OnInit, OnChanges {

  @Input() req: Requirement;
  @Input() form: FormGroup;

  @Output() indicatorChanged = new EventEmitter();

  constructor() {
  }

  ngOnChanges() {
    this.indicatorChanged.emit(this.form.get(this.req.id).value);
  }

  ngOnInit() {
    if (this.req.responseDataType === 'INDICATOR') {
      this.form.get(this.req.id)
        .valueChanges
        .subscribe(ev => {
          console.log('emit: ' + ev);
          this.indicatorChanged.emit(ev);
        });
    }
  }


}
