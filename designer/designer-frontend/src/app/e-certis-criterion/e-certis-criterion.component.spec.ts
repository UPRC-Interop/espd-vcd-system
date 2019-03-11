import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ECertisCriterionComponent } from './e-certis-criterion.component';

describe('ECertisCriterionComponent', () => {
  let component: ECertisCriterionComponent;
  let fixture: ComponentFixture<ECertisCriterionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ECertisCriterionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ECertisCriterionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
