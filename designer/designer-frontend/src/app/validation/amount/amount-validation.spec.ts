import {FormControl} from "@angular/forms";
import {AmountValidation} from "./amount-validation";

describe('AmountValidation', () => {
  it('should not validate to error on positive number', () => {
    let errors = AmountValidation(new FormControl('123456'));

    expect(errors).toBeNull();
  });
  it('should not validate to error on positive number', () => {
    let errors = AmountValidation(new FormControl('123456'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with plus sign before number', () => {
    let errors = AmountValidation(new FormControl('+5'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with plus minus before number', () => {
    let errors = AmountValidation(new FormControl('-5'));

    expect(errors).toBeNull();
  });
  it('should not validate to error on floating point number', () => {
    let errors = AmountValidation(new FormControl('5.0'));

    expect(errors).toBeNull();
  });
  it('should not validate to error on negativ floating point number', () => {
    let errors = AmountValidation(new FormControl('-5.0'));

    expect(errors).toBeNull();
  });
  it('should not validate to error on positive floating point number', () => {
    let errors = AmountValidation(new FormControl('+5.0'));

    expect(errors).toBeNull();
  });
  it('should not validate to error on floating point number with two', () => {
    let errors = AmountValidation(new FormControl('5.00'));

    expect(errors).toBeNull();
  });
  it('should validate to error on floating point number with three', () => {
    let errors = AmountValidation(new FormControl('5.000'));

    expect(errors).toEqual({'amount': true});
  });
  it('should validate to error on literal', () => {
    let errors = AmountValidation(new FormControl('f'));

    expect(errors).toEqual({'amount': true});
  });
  it('should not validate to error with empty string', () => {
    let errors = AmountValidation(new FormControl(' '));

    expect(errors).toBeNull();
  });
});
