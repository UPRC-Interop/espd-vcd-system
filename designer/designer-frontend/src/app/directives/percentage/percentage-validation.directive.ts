import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from "@angular/forms";
import {PercentageValidation} from "../../validation/percentage/percentage-validation";

@Directive({
  selector: '[percentage]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => PercentageValidationDirective), multi: true}
  ]
})
export class PercentageValidationDirective implements Validator {

  constructor() { }

  validate(c: AbstractControl): { [key: string]: any } {
    return PercentageValidation(c);
  }

}
