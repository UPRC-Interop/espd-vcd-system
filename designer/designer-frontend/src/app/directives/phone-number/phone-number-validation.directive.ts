import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from "@angular/forms";
import {PhoneNumberValidation} from "../../validation/phone-number/phone-number-validation";

@Directive({
  selector: '[phoneNumber]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => PhoneNumberValidationDirective), multi: true}
  ]
})
export class PhoneNumberValidationDirective implements Validator {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return PhoneNumberValidation(c);
  }
}
