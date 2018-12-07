import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor() { }

  public validateFormsInComponent(forms): boolean {
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
