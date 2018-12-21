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

  it('form is not null, untouched and valid, then validation should return true', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = formBuilder.group({
      testField: ['']
    });
    // when
    const result = service.validateForm(form);
    // then
    expect(result).toBeTruthy();
  }));

  it('form is null, then validation should return true', inject([ValidationService], (service: ValidationService) => {
    // given
    const form = null;
    // when
    const result = service.validateForm(form);
    // then
    expect(result).toBeTruthy();
  }));

  it('form is not null, but touched and valid, then validation should return true', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = formBuilder.group({
      testField: ['']
    });
    form.markAsTouched();
    // when
    const result = service.validateForm(form);
    // then
    expect(result).toBeTruthy();
  }));

  it('form is not null, untouched but invalid, then validation should return true', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = formBuilder.group({
      testField: ['']
    });
    form.setErrors({error: true});
    // when
    const result = service.validateForm(form);
    // then
    expect(result).toBeTruthy();
  }));

  it('form is not null, touched and invalid, then validation should return false', inject([ValidationService, FormBuilder], (service: ValidationService, formBuilder: FormBuilder) => {
    // given
    const form = formBuilder.group({
      testField: ['']
    });
    form.setErrors({error: true});
    form.markAsTouched();
    // when
    const result = service.validateForm(form);
    // then
    expect(result).toBeFalsy();
  }));
});
