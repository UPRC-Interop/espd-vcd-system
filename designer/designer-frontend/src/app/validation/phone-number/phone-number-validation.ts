import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const phoneNumberRegExp: RegExp = new RegExp("^[\\d\\s\+\\-\*\#\/\(\)]+$");

export function PhoneNumberValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let phoneNumber = control.value;

  if (StringHelperService.isBlank(phoneNumber)) {
    return null;
  }
  if (!phoneNumberRegExp.test(phoneNumber)) {
    return {
      'phoneNumber': true
    }
  }
  return null;

}
