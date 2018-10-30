import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const integerRegExp: RegExp = new RegExp("^\\d+$");

export function IntegerValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let integer = control.value;

  if (StringHelperService.isBlank(integer)) {
    return null;
  }
  if (!integerRegExp.test(integer)) {
    return {
      'number': true
    }
  }
  return null;

}
