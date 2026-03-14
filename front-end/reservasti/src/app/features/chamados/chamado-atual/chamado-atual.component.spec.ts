import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChamadoAtualComponent } from './chamado-atual.component';

describe('ChamadoAtualComponent', () => {
  let component: ChamadoAtualComponent;
  let fixture: ComponentFixture<ChamadoAtualComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChamadoAtualComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChamadoAtualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
