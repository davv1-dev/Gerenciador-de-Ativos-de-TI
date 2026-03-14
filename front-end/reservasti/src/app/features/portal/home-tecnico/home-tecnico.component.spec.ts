import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeTecnicoComponent } from './home-tecnico.component';

describe('HomeTecnicoComponent', () => {
  let component: HomeTecnicoComponent;
  let fixture: ComponentFixture<HomeTecnicoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeTecnicoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeTecnicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
