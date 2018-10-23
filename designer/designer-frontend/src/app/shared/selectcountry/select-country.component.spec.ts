import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SelectCountryComponent} from './select-country.component';
import {DataService} from "../../services/data.service";
import {NGXLogger} from "ngx-logger";
import {MaterialModule} from "../../material/material.module";
import {Country} from "../../model/country.model";

describe('SelectCountryComponent', () => {
  let component: SelectCountryComponent;
  let fixture: ComponentFixture<SelectCountryComponent>;

  class DataServiceMock {
    getCountries(): Promise<Country[]> {
      return null;
    }
  }


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SelectCountryComponent],
      imports: [MaterialModule],
      providers: [
        {provide: DataService, useValue: DataServiceMock},
        NGXLogger
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectCountryComponent);
    component = fixture.componentInstance;

    let dataService = fixture.debugElement.injector.get(DataService);
    let ngxLogger = fixture.debugElement.injector.get(NGXLogger);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
