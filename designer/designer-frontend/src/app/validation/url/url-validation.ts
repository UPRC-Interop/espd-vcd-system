import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

const urlRegExp: RegExp = new RegExp("^(((http|HTTP|https|HTTPS|ftp|FTP|ftps|FTPS|sftp|SFTP)://)|((w|W){3}(\\d)?\\.))[\\w\\?!\\./:;,\\-_=#+*%@&quot;\\(\\)&amp;]+");

export function UrlValidation(control: AbstractControl): { [key: string]: boolean } | null {
  let url = control.value;
  if (StringHelperService.isBlank(url)) {
    return null;
  }
  if (!urlRegExp.test(url)) {
    return {
      'url': true
    }
  }
  return null;
}
