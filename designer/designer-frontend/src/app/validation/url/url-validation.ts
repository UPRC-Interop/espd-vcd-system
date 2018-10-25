import {AbstractControl} from "@angular/forms";

export function UrlValidation(control: AbstractControl): { [key: string]: boolean } | null {
  let urlRegEx = new RegExp("(((http|HTTP|https|HTTPS|ftp|FTP|ftps|FTPS|sftp|SFTP)://)|((w|W){3}(\\d)?\\.))[\\w\\?!\\./:;,\\-_=#+*%@&quot;\\(\\)&amp;]+");
  let blankRegEx = new RegExp("^\\s*$");
  let url = control.value;
  if (url == null || blankRegEx.test(url)) {
    return null;
  }
  if (!urlRegEx.test(url)) {
    return {
      'url': true
    }
  }
  return null;
}
