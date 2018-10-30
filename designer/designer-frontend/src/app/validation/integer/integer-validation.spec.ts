import {FormControl} from "@angular/forms";
import {IntegerValidation} from "./integer-validation";

describe('IntegerValidation', () => {
  it('should not validate to error on positive number', () => {
    let errors = IntegerValidation(new FormControl('123456'));

    expect(errors).toBeNull();
  });
  it('should validate to error with plus sign before number', () => {
    let errors = IntegerValidation(new FormControl('+5'));

    expect(errors).toEqual({'integer': true});
  });
  it('should validate to error with plus minus before number', () => {
    let errors = IntegerValidation(new FormControl('-5'));

    expect(errors).toEqual({'integer': true});
  });
  it('should validate to error on floating point number', () => {
    let errors = IntegerValidation(new FormControl('5.0'));

    expect(errors).toEqual({'integer': true});
  });
  it('should validate to error on literal', () => {
    let errors = IntegerValidation(new FormControl('f'));

    expect(errors).toEqual({'integer': true});
  });
  it('should not validate to error with empty string', () => {
    let errors = IntegerValidation(new FormControl(' '));

    expect(errors).toBeNull();
  });
});
