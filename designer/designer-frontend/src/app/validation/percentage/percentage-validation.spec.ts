import {PercentageValidation} from "./percentage-validation";
import {FormControl} from "@angular/forms";

describe('PercentageValidation', () => {
  it('should validate error for strings', () => {
    let errors = PercentageValidation(new FormControl('abc'));
    expect(errors).toEqual({percentage: true});
  });

  it('should validate error for non float value', () => {
    let errors = PercentageValidation(new FormControl('1,23'));
    expect(errors).toEqual({percentage: true});
  });

  it('should validate error for negative percent', () => {
    let errors = PercentageValidation(new FormControl('-101'));
    expect(errors).toEqual({percentage: true});
  });

  it('should validate error for more then 100 percent', () => {
    let errors = PercentageValidation(new FormControl('101'));
    expect(errors).toEqual({percentage: true});
  });

  it('should not validate error for float value', () => {
    let errors = PercentageValidation(new FormControl('1.23'));
    expect(errors).toBeNull();
  });

  it('should validate error for mor then 100 percent in decimal', () => {
    let errors = PercentageValidation(new FormControl('100.000001'));
    expect(errors).toEqual({percentage: true});
  });

  it('should not validate error for 100 percent', () => {
    let errors = PercentageValidation(new FormControl('100'));
    expect(errors).toBeNull();
  });

  it('should not validate error for zero percent', () => {
    let errors = PercentageValidation(new FormControl('0'));
    expect(errors).toBeNull();
  });

  it('should not validate error for zero percent ', () => {
    let errors = PercentageValidation(new FormControl('0'));
    expect(errors).toBeNull();
  });

  it('should not validate error for int value', () => {
    let errors = PercentageValidation(new FormControl('1'));
    expect(errors).toBeNull();
  });
});
