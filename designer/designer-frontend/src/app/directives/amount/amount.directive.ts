import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS} from "@angular/forms";
import {AmountValidation} from "../../validation/amount/amount-validation";

@Directive({
  selector: '[amount]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => AmountDirective), multi: true}
  ]
})
export class AmountDirective {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return AmountValidation(c);
  }
}
