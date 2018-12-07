import {FormControl} from "@angular/forms";
import {PhoneNumberValidation} from "./phone-number-validation";

describe('PhoneNumberValidation', () => {
  it('should validate to error on number with literals ', () => {
    let errors = PhoneNumberValidation(new FormControl('0231405060a'));

    expect(errors).toEqual({'phoneNumber': true});

  });
  it('should validate to error on number with literals ', () => {
    let errors = PhoneNumberValidation(new FormControl('0231405060a'));

    expect(errors).toEqual({'phoneNumber': true});

  });
  it('should not validate to error with European Parliament in Bruxelles phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('+32(0)2 28 42111'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with European Parliament in Munich phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('+49 / (0)89 / 202 0879-0'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with parentheses in phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('(49)231405060'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with space in phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('49 231405060'));

    expect(errors).toBeNull();
  });
  it('should not validate to error local phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('0231405060'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with pound and asterisks in phone number', () => {
    let errors = PhoneNumberValidation(new FormControl('*100#'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with empty string', () => {
    let errors = PhoneNumberValidation(new FormControl(' '));

    expect(errors).toBeNull();
  });
});
