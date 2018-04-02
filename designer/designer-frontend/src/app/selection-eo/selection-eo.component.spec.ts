import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectionEoComponent } from './selection-eo.component';

describe('SelectionEoComponent', () => {
  let component: SelectionEoComponent;
  let fixture: ComponentFixture<SelectionEoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectionEoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectionEoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
