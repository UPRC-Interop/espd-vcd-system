import {Injectable} from '@angular/core';
import {UtilitiesService} from "./utilities.service";

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor(private utilitiesService: UtilitiesService) { }

  public validateFormsInComponent(forms): boolean {
    if (this.utilitiesService.isReviewESPD) {
      return true;
    }

    let valid: boolean = true;
      forms.forEach(ngForm => {
        if (ngForm.form !== null && !ngForm.form.valid) {
          valid = false;
        }
      });
    return valid;
  }

  public validateForm(form): boolean {
    return form === null || !form.touched || form.valid;
  }
}
