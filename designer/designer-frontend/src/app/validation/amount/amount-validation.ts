import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const amountRegEx: RegExp = new RegExp("^[+-]?\\d+(\\.\\d{1,2})?$");

export function AmountValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let integer = control.value;

  if (StringHelperService.isBlank(integer)) {
    return null;
  }
  if (!amountRegEx.test(integer)) {
    return {
      'amount': true
    }
  }
  return null;

}
