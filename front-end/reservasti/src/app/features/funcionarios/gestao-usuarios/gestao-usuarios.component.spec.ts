import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestaoUsuariosComponent } from './gestao-usuarios.component';

describe('GestaoUsuariosComponent', () => {
  let component: GestaoUsuariosComponent;
  let fixture: ComponentFixture<GestaoUsuariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GestaoUsuariosComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestaoUsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
