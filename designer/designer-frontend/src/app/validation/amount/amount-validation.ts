///
/// Copyright 2016-2019 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

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
