import {Component, Input} from '@angular/core';
import {AbstractControl, FormControl, ValidationErrors} from "@angular/forms";

@Component({
  selector: 'input-validation-error',
  templateUrl: './input-validation-error.component.html',
  styleUrls: ['./input-validation-error.component.css']
})
export class InputValidationErrorComponent {

  @Input() formControl: FormControl;
  @Input() formControlErrors: ValidationErrors;

  constructor() {
  }

  // 2016/S 123-123456
  getValidationErrorMessage(formControl: AbstractControl) {
    let validationErrors: string[] = Object.keys(formControl.errors).map(key => this.getValidationErrorMessageKey(key));

    return validationErrors.join(',');
  }

  getValidationErrorsMessage(errors: ValidationErrors) {
    let validationErrors: string[] = Object.keys(errors);
    return this.getValidationErrorMessageKey(validationErrors[0]);
  }

  getFirstValidationErrorMessage(formControl: FormControl): string {
    let validationErrors: string[] = Object.keys(formControl.errors);
    return this.getValidationErrorMessageKey(validationErrors[0]);
  }

  getValidationErrorMessageKey(validation: String): string {
    return `espd.validator.${validation}`;
  }


}
