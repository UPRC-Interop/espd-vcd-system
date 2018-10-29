import {AbstractControl} from "@angular/forms";
import {StringHelperService} from "../../services/string-helper.service";

export function UrlValidation(control: AbstractControl): { [key: string]: boolean } | null {
  let urlRegEx = new RegExp("(((http|HTTP|https|HTTPS|ftp|FTP|ftps|FTPS|sftp|SFTP)://)|((w|W){3}(\\d)?\\.))[\\w\\?!\\./:;,\\-_=#+*%@&quot;\\(\\)&amp;]+");
  let url = control.value;
  if (StringHelperService.isBlank(url)) {
    return null;
  }
  if (!urlRegEx.test(url)) {
    return {
      'url': true
    }
  }
  return null;
}
