import {NumberOjsValidation} from "./number-ojs-validation";
import {FormControl} from "@angular/forms";

describe('NumberOjsValidation', () => {
  it('should validate to error on number', () => {
    let errors = NumberOjsValidation(new FormControl('123456'));

    expect(errors).toEqual({'numberOjs': true});

  });
  it('should not validate to error with valid numberOjs', () => {
    let errors = NumberOjsValidation(new FormControl('2016/S 123-123456'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with empty string', () => {
    let errors = NumberOjsValidation(new FormControl(' '));

    expect(errors).toBeNull();
  });
});
