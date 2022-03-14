import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TextLocalizationService } from '../service/text-localization.service';
import { ITextLocalization, TextLocalization } from '../text-localization.model';

import { TextLocalizationUpdateComponent } from './text-localization-update.component';

describe('TextLocalization Management Update Component', () => {
  let comp: TextLocalizationUpdateComponent;
  let fixture: ComponentFixture<TextLocalizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let textLocalizationService: TextLocalizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TextLocalizationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TextLocalizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TextLocalizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    textLocalizationService = TestBed.inject(TextLocalizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const textLocalization: ITextLocalization = { id: 456 };

      activatedRoute.data = of({ textLocalization });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(textLocalization));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TextLocalization>>();
      const textLocalization = { id: 123 };
      jest.spyOn(textLocalizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ textLocalization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: textLocalization }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(textLocalizationService.update).toHaveBeenCalledWith(textLocalization);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TextLocalization>>();
      const textLocalization = new TextLocalization();
      jest.spyOn(textLocalizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ textLocalization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: textLocalization }));
      saveSubject.complete();

      // THEN
      expect(textLocalizationService.create).toHaveBeenCalledWith(textLocalization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TextLocalization>>();
      const textLocalization = { id: 123 };
      jest.spyOn(textLocalizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ textLocalization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(textLocalizationService.update).toHaveBeenCalledWith(textLocalization);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
