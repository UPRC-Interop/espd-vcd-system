import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MiniRenderPageComponent } from './mini-render-page.component';

describe('MiniRenderPageComponent', () => {
  let component: MiniRenderPageComponent;
  let fixture: ComponentFixture<MiniRenderPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MiniRenderPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MiniRenderPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
