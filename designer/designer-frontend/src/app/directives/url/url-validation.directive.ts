import {Directive, forwardRef} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, Validator} from '@angular/forms';
import {UrlValidation} from "../../validation/url/url-validation";

@Directive({
  selector: '[url]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: forwardRef(() => UrlValidationDirective), multi: true}
  ]
})
export class UrlValidationDirective implements Validator {

  constructor() {
  }

  validate(c: AbstractControl): { [key: string]: any } {
    return UrlValidation(c);
  }
}
