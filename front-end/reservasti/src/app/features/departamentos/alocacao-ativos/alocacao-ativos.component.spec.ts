import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlocacaoAtivosComponent } from './alocacao-ativos.component';

describe('AlocacaoAtivosComponent', () => {
  let component: AlocacaoAtivosComponent;
  let fixture: ComponentFixture<AlocacaoAtivosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlocacaoAtivosComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlocacaoAtivosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
