import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS} from "@angular/forms";
import {IntegerValidation} from "../../validation/integer/integer-validation";

@Directive({
  selector: '[integer]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => IntegerValidationDirective), multi: true}
  ]
})
export class IntegerValidationDirective {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return IntegerValidation(c);
  }
}
