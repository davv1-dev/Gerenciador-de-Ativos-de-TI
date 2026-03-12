import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AberturaChamadoComponent } from './abertura-chamado.component';

describe('AberturaChamadoComponent', () => {
  let component: AberturaChamadoComponent;
  let fixture: ComponentFixture<AberturaChamadoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AberturaChamadoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AberturaChamadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
