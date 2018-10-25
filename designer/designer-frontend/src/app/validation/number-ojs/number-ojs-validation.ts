import {AbstractControl} from "@angular/forms";

export function NumberOjsValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let regEx = new RegExp("^(20|19)\\d{2}/S (((00)?[1-9])|([0]?[1-9][0-9])|(1[0-9][0-9])|(2[0-5][0-9]))-\\d{6}$");

  if (!regEx.test(control.value)) {
    return {
      'numberOjs': true
    }
  }
  return null;

}
