import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const PERCENTAGE_REG = "^100+(\\.0{1,6})?$|(^[0-9]{1,2})+(\\.[0-9]{1,6})?$";

export function PercentageValidation(control: AbstractControl): { [key: string]: boolean } | null {
  let percentageRegEx = new RegExp(PERCENTAGE_REG);
  let percentage = control.value;
  if (StringHelperService.isBlank(percentage)) {
    return null;
  }
  if (!percentageRegEx.test(percentage)) {
    return {
      'percentage': true
    }
  }
  return null;
}
