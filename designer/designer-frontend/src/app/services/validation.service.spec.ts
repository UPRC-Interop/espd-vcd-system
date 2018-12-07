import {inject, TestBed} from '@angular/core/testing';

import {ValidationService} from './validation.service';
import {FormBuilder} from "@angular/forms";
import {QueryList} from "@angular/core";

describe('ValidationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ValidationService,
        FormBuilder
      ]
    });
  });

  it('should be created', inject([ValidationService], (service: ValidationService) => {
    expect(service).toBeTruthy();
  }));

  it('forms are valid if forms are valid and not touched', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = {
      form: formBuilder.group({
        testField: ['']
      })
    };
    const listOfForms = new QueryList();
    listOfForms.reset([form]);
    // when
    const result = service.validateFormsInComponent(listOfForms);
    // then
    expect(result).toBeTruthy();
  }));

  it('forms are valid if forms are valid and touched', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = {
      form: formBuilder.group({
        testField: ['']
      })
    };
    form.form.markAsTouched();
    const listOfForms = new QueryList();
    listOfForms.reset([form]);
    // when
    const result = service.validateFormsInComponent(listOfForms);
    // then
    expect(result).toBeTruthy();
  }));

  it('forms are valid if forms are invalid but not touched', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = {
      form: formBuilder.group({
        testField: ['']
      })
    };
    form.form.setErrors({incorrect: true});
    const listOfForms = new QueryList();
    listOfForms.reset([form]);
    // when
    const result = service.validateFormsInComponent(listOfForms);
    // then
    expect(result).toBeTruthy();
  }));

  it('forms are invalid if one form is invalid and touched', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = {
      form: formBuilder.group({
        testField: ['']
      })
    };
    form.form.setErrors({incorrect: true});
    form.form.markAsTouched();
    const listOfForms = new QueryList();
    listOfForms.reset([form]);
    // when
    const result = service.validateFormsInComponent(listOfForms);
    // then
    expect(result).toBeFalsy();
  }));
});
