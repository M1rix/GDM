import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TextLocalizationDetailComponent } from './text-localization-detail.component';

describe('TextLocalization Management Detail Component', () => {
  let comp: TextLocalizationDetailComponent;
  let fixture: ComponentFixture<TextLocalizationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TextLocalizationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ textLocalization: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TextLocalizationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TextLocalizationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load textLocalization on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.textLocalization).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
