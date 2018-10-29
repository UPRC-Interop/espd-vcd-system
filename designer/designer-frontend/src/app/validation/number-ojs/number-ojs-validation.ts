import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

export function NumberOjsValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let regEx = new RegExp("^(20|19)\\d{2}/S (((00)?[1-9])|([0]?[1-9][0-9])|(1[0-9][0-9])|(2[0-5][0-9]))-\\d{6}$");
  let numberOjs = control.value;

  if (StringHelperService.isBlank(numberOjs)) {
    return null;
  }
  if (!regEx.test(numberOjs)) {
    return {
      'numberOjs': true
    }
  }
  return null;

}
