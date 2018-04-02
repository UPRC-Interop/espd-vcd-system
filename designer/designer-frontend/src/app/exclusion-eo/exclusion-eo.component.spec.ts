import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExclusionEoComponent } from './exclusion-eo.component';

describe('ExclusionEoComponent', () => {
  let component: ExclusionEoComponent;
  let fixture: ComponentFixture<ExclusionEoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExclusionEoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExclusionEoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
