/** Error when invalid control is dirty, touched, or submitted. */
import {ErrorStateMatcher} from "@angular/material";
import {FormControl, FormGroupDirective, NgForm} from "@angular/forms";

export class InputValidationStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    let isError = !!(control && control.invalid && (control.dirty || control.touched));
    // console.log(`isError: ${isError} für Feld`);
    return isError;
  }
}
