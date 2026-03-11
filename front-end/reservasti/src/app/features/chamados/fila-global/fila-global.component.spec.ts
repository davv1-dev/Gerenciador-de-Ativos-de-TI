import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilaGlobalComponent } from './fila-global.component';

describe('FilaGlobalComponent', () => {
  let component: FilaGlobalComponent;
  let fixture: ComponentFixture<FilaGlobalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FilaGlobalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilaGlobalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
