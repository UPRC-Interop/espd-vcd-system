import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TOOPDialogComponent } from './toopdialog.component';

describe('TOOPDialogComponent', () => {
  let component: TOOPDialogComponent;
  let fixture: ComponentFixture<TOOPDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TOOPDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TOOPDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
