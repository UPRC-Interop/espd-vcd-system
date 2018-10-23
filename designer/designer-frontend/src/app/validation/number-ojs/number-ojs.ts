import {AbstractControl, ValidatorFn} from "@angular/forms";

export function NumberOjs(): ValidatorFn {

  let regEx = new RegExp("^(20|19)\\d{2}/S (((00)?[1-9])|([0]?[1-9][0-9])|(1[0-9][0-9])|(2[0-5][0-9]))-\\d{6}$");

  return (control: AbstractControl): {[key: string]: any} | null => {
    if(!regEx.test(control.value)) {
      return {
        'numberOjs': true
      }
    }
    return null;
  };
}
