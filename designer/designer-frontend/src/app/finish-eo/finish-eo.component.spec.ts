import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinishEoComponent } from './finish-eo.component';

describe('FinishEoComponent', () => {
  let component: FinishEoComponent;
  let fixture: ComponentFixture<FinishEoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinishEoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinishEoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
