import {Component, Input} from '@angular/core';
import {ValidationErrors} from '@angular/forms';

@Component({
  selector: 'input-validation-error',
  templateUrl: './input-validation-error.component.html',
  styleUrls: ['./input-validation-error.component.css']
})
export class InputValidationErrorComponent {

  @Input() formControlErrors: ValidationErrors;

  constructor() {
  }

  getValidationErrorsMessage(errors: ValidationErrors) {
    let validationErrors: string[] = Object.keys(errors);

    return this.getValidationErrorMessageKey(validationErrors[0]);
  }

  getValidationErrorMessageKey(validation: String): string {
    return `espd.validator.${validation}`;
  }


}
