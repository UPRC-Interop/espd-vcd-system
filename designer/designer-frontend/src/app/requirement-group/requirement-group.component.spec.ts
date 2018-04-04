import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequirementGroupComponent } from './requirement-group.component';

describe('RequirementGroupComponent', () => {
  let component: RequirementGroupComponent;
  let fixture: ComponentFixture<RequirementGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequirementGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequirementGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
