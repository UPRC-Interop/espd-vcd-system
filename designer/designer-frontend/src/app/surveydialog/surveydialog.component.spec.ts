import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveydialogComponent } from './surveydialog.component';

describe('SurveydialogComponent', () => {
  let component: SurveydialogComponent;
  let fixture: ComponentFixture<SurveydialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SurveydialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SurveydialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
