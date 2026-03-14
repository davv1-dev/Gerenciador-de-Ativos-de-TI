import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinhaFilaComponent } from './minha-fila.component';

describe('MinhaFilaComponent', () => {
  let component: MinhaFilaComponent;
  let fixture: ComponentFixture<MinhaFilaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinhaFilaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MinhaFilaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
