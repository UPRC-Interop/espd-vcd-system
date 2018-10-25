import {UrlValidation} from "./url-validation";
import {FormControl} from "@angular/forms";

describe('UrlValidation', () => {
  it('should validate to error on random numbers', () => {
    let errors = UrlValidation(new FormControl('123456'));

    expect(errors).toEqual({'url': true});

  });
  it('should not validate to error with valid url and subdomain', () => {
    let errors = UrlValidation(new FormControl('http://stash.ds.unipi.gr'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with valid url', () => {
    let errors = UrlValidation(new FormControl('https://www.example.com'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with valid url simap ted europa url', () => {
    let errors = UrlValidation(new FormControl('https://simap.ted.europa.eu/web/simap/standard-forms-for-public-procurement'));

    expect(errors).toBeNull();
  });
  it('should not validate to error with blank input string', () => {
    let errors = UrlValidation(new FormControl(' '));

    expect(errors).toBeNull();
  });
  it('should not validate to error with empty input string', () => {
    let errors = UrlValidation(new FormControl(''));

    expect(errors).toBeNull();
  });
  it('should not validate to error with null string', () => {
    let errors = UrlValidation(new FormControl(null));

    expect(errors).toBeNull();
  });
});
