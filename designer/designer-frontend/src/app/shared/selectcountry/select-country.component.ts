import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DataService} from "../../services/data.service";
import {Country} from "../../model/country.model";
import {NGXLogger} from "ngx-logger";


@Component({
  selector: 'select-country',
  templateUrl: './select-country.component.html',
  styleUrls: ['./select-country.component.css']
})
export class SelectCountryComponent implements OnInit {

  @Input() descriptionPropertyKey: string;
  @Input() shouldEnable: boolean = true;
  @Output() selectedCountry = new EventEmitter<String>();

  countries: Country[];
  constructor(public dataService: DataService, private logger: NGXLogger) {
  }

  ngOnInit() {
    this.logger.info("SelectCountryComponent");
    this.dataService.getCountries().then(res => {
      this.countries = res;
    })
      .catch(err => {
        this.logger.info("err");
      });

  }

  changeClient(event) {
    this.logger.info(event);
    console.log(event);
  }

}
