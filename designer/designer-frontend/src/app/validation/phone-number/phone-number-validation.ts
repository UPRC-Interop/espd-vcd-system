import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

export function PhoneNumberValidation(control: AbstractControl): { [key: string]: boolean } | null {

  let regEx = new RegExp("^[\\d\\s\+\\-\*\#\/\(\)]+$");
  let phoneNumber = control.value;

  if (StringHelperService.isBlank(phoneNumber)) {
    return null;
  }
  if (!regEx.test(phoneNumber)) {
    return {
      'phoneNumber': true
    }
  }
  return null;

}
