import {Injectable} from "@angular/core";

@Injectable()
export class StringHelperService {

  constructor() {
  }

  static isBlank(str: string): boolean {
    let blankRegEx = new RegExp("^\\s*$");
    if (str == null || blankRegEx.test(str)) {
      return true;
    }
    return false;
  }
}
