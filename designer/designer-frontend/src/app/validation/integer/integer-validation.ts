import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const integerRegEx: RegExp = new RegExp("^\\d+$");

export function IntegerValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let integer = control.value;

  if (StringHelperService.isBlank(integer)) {
    return null;
  }
  if (!integerRegEx.test(integer)) {
    return {
      'number': true
    }
  }
  return null;

}
