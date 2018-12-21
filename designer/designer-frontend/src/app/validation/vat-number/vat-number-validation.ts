import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const vatNumberRegExp: RegExp = new RegExp("^[A-Z0-9\\s]+$");

export function VatNumberValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let phoneNumber = control.value;

  if (StringHelperService.isBlank(phoneNumber)) {
    return null;
  }
  if (!vatNumberRegExp.test(phoneNumber)) {
    return {
      'vatNumber': true
    }
  }
  return null;

}
