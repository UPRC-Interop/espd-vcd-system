import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from "@angular/forms";
import {VatNumberValidation} from "../../validation/vat-number/vat-number-validation";

@Directive({
  selector: '[vatNumber]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => VatNumberValidationDirective), multi: true}
  ]
})
export class VatNumberValidationDirective implements Validator {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return VatNumberValidation(c);
  }
}
