import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DebtDetailComponent } from './debt-detail.component';

describe('Debt Management Detail Component', () => {
  let comp: DebtDetailComponent;
  let fixture: ComponentFixture<DebtDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DebtDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ debt: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DebtDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DebtDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load debt on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.debt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
