import {Directive, HostListener, Input} from '@angular/core';
import {AbstractControl} from "@angular/forms";

/**
 * This directive is a workaround for this angular material bug: https://github.com/angular/material2/issues/13346
 */

@Directive({
  selector: '[appDateValidationFix]'
})
export class DateValidationFixDirective {

  @Input('appDateValidationFix') formControl: AbstractControl;

  @HostListener('blur') onFocusLost() {
    if (this.formControl.value === null || this.formControl.value === "") {
      this.formControl.setErrors({'matDatepickerParse': true});
    }
  }
}
