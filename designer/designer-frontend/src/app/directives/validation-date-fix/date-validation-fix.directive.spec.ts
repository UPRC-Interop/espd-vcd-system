import {DateValidationFixDirective} from './date-validation-fix.directive';
import {ElementRef} from "@angular/core";
import {FormControl} from "@angular/forms";
import moment = require("moment");

describe('DateValidationFixDirective', () => {

  it('should validate to error with string literal inputValue and formControlValue null on blur', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = 'abc';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl(null);

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.errors).toEqual({'matDatepickerParse': true});
  });

  it('should not validate to error with blank inputValue and formControlValue null on blur', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = ' ';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl(null);

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.errors).toBeNull();
  });

  it('should not validate to error on valid date on blur', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = 'abc';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl(moment('2018-11-02', 'YYYY-MM-DD'));

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.errors).toBeNull();
  });

  it('should remove matDatepickerParse validation error on empty inputs on blur', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = '';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl(null);
    directive.formControl.setErrors({'matDatepickerParse': true});

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.hasError('matDatepickerParse')).toBeFalsy();
  });

  it('should not remove present matDatepickerParse validation error on literal inputs on blur', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = 'abc123';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl(null);
    directive.formControl.setErrors({'matDatepickerParse': true});

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.hasError('matDatepickerParse')).toBeTruthy();
  });

  it('should validate to error on empty form control', () => {
    // given
    const elementRef = new ElementRef(document.createElement('input'));
    elementRef.nativeElement.value = '123';

    const directive = new DateValidationFixDirective(elementRef);
    directive.formControl = new FormControl('');

    // when
    directive.onFocusLost();

    // then
    expect(directive.formControl.hasError('matDatepickerParse')).toBeTruthy();
  });
});
