import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcedureEoComponent } from './procedure-eo.component';

describe('ProcedureEoComponent', () => {
  let component: ProcedureEoComponent;
  let fixture: ComponentFixture<ProcedureEoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcedureEoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcedureEoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
