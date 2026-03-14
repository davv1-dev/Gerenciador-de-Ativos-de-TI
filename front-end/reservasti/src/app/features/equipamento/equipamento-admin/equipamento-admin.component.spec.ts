import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquipamentoAdminComponent } from './equipamento-admin.component';

describe('EquipamentoAdminComponent', () => {
  let component: EquipamentoAdminComponent;
  let fixture: ComponentFixture<EquipamentoAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EquipamentoAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EquipamentoAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
