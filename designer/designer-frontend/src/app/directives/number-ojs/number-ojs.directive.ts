import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from '@angular/forms';
import {NumberOjsValidation} from "../../validation/number-ojs/number-ojs-validation";

@Directive({
  selector: '[numberOjs]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => NumberOjsDirective), multi: true}
  ]
})
export class NumberOjsDirective implements Validator {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return NumberOjsValidation(c);
  }
}
