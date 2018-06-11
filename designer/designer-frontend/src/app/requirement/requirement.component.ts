import {Component, Input, Output, OnInit, EventEmitter, OnChanges} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Requirement} from '../model/requirement.model';
import {DataService} from '../services/data.service';
import {Country} from '../model/country.model';
import {Currency} from '../model/currency.model';

@Component({
  selector: 'app-requirement',
  templateUrl: './requirement.component.html',
  styleUrls: ['./requirement.component.css']
})
export class RequirementComponent implements OnInit, OnChanges {

  @Input() req: Requirement;
  @Input() form: FormGroup;

  @Output() indicatorChanged = new EventEmitter();

  countries: Country[] = null;
  currency: Currency[] = null;

  constructor(private dataService: DataService) {
  }

  ngOnChanges() {
    this.indicatorChanged.emit(this.form.get(this.req.uuid).value);
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

    this.dataService.getCurrency()
      .then(res => {
        this.currency = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });


    if (this.req.responseDataType === 'INDICATOR') {
      this.form.get(this.req.uuid)
        .valueChanges
        .subscribe(ev => {
          console.log('emit: ' + ev);
          this.indicatorChanged.emit(ev);
        });
    }
  }


}
