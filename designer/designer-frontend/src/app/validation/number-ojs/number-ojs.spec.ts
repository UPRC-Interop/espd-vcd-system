import {NumberOjs} from "./number-ojs";
import {FormControl} from "@angular/forms";

describe('NumberOjs', () => {
  it('should validate to error on number', () => {
    let errors = NumberOjs()(new FormControl('123456'));

    expect(errors).toEqual({'numberOjs': true});

  });
  it('should not validate to error valid ojs', () => {
    let errors = NumberOjs()(new FormControl('2016/S 123-123456'));

    expect(errors).toBeNull();

  });
});
