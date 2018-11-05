import {Directive, ElementRef, HostListener, Input} from '@angular/core';
import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

/**
 * This directive is a workaround for this angular material bug: https://github.com/angular/material2/issues/13346
 */

@Directive({
  selector: '[appDateValidationFix]'
})
export class DateValidationFixDirective {

  @Input('appDateValidationFix') formControl: AbstractControl;

  constructor(private elementRef: ElementRef) {
  }

  @HostListener('blur') onFocusLost() {
    let nativeElementValue = this.elementRef.nativeElement.value;
    if (this.formControl.value === null && !StringHelperService.isBlank(nativeElementValue)) {
      this.formControl.setErrors({'matDatepickerParse': true});
    }
    else {
      if (this.formControl.hasError('matDatepickerParse')) {
        delete this.formControl.errors['matDatepickerParse'];
        this.formControl.updateValueAndValidity();
      }
    }
  }
}
