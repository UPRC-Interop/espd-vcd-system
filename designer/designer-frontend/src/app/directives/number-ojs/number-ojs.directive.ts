import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from '@angular/forms';
import {NumberOjs} from "../../validation/number-ojs/number-ojs";

@Directive({
  selector: '[numberOjs]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => NumberOjsDirective), multi: true}
  ]
  // 2016/S 003-123456
})
export class NumberOjsDirective implements Validator {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return NumberOjs()(c);
  }
}
