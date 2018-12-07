import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';
import {AbstractControl} from "@angular/forms";

@Directive({
  selector: '[validationError]'
})
export class ValidationErrorDirective {

  valid:boolean = true;
  formControl: AbstractControl;

  @Input() set validationError(formControl: AbstractControl) {
    console.log("show");
    this.formControl = formControl;
    formControl.statusChanges.subscribe( val => {
      // console.log(`${val}`);
      // if(val === 'VALID' && !this.valid) {
      //   this.vcRef.clear();
      //   this.valid = true;
      // }
      // else if(val === 'INVALID' && this.valid) {
      //   this.vcRef.createEmbeddedView(this.templateRef);
      //   this.valid = false;
      // }
    });

    formControl.valueChanges.subscribe(value => {
      console.log(`${value}`);
      if(this.formControl.dirty && this.formControl.invalid && this.valid) {
        this.vcRef.createEmbeddedView(this.templateRef);
        this.valid = false;
        console.log("invalid -> show!");
      }
      else if(this.formControl.dirty && this.formControl.valid && !this.valid) {
        this.vcRef.clear();
        this.valid = true;
        console.log("valid -> hide!");
      }
    })
  }

  constructor(private templateRef: TemplateRef<any>, private vcRef: ViewContainerRef) {

  }


}
