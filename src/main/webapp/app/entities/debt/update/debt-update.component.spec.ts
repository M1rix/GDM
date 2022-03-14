import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DebtService } from '../service/debt.service';
import { IDebt, Debt } from '../debt.model';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';

import { DebtUpdateComponent } from './debt-update.component';

describe('Debt Management Update Component', () => {
  let comp: DebtUpdateComponent;
  let fixture: ComponentFixture<DebtUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let debtService: DebtService;
  let profileService: ProfileService;
  let currencyService: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DebtUpdateComponent],
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
      .overrideTemplate(DebtUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DebtUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    debtService = TestBed.inject(DebtService);
    profileService = TestBed.inject(ProfileService);
    currencyService = TestBed.inject(CurrencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profile query and add missing value', () => {
      const debt: IDebt = { id: 456 };
      const profile: IProfile = { id: 3101 };
      debt.profile = profile;

      const profileCollection: IProfile[] = [{ id: 55369 }];
      jest.spyOn(profileService, 'query').mockReturnValue(of(new HttpResponse({ body: profileCollection })));
      const additionalProfiles = [profile];
      const expectedCollection: IProfile[] = [...additionalProfiles, ...profileCollection];
      jest.spyOn(profileService, 'addProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      expect(profileService.query).toHaveBeenCalled();
      expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(profileCollection, ...additionalProfiles);
      expect(comp.profilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Currency query and add missing value', () => {
      const debt: IDebt = { id: 456 };
      const currency: ICurrency = { id: 27765 };
      debt.currency = currency;

      const currencyCollection: ICurrency[] = [{ id: 36850 }];
      jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
      const additionalCurrencies = [currency];
      const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
      jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      expect(currencyService.query).toHaveBeenCalled();
      expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(currencyCollection, ...additionalCurrencies);
      expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const debt: IDebt = { id: 456 };
      const profile: IProfile = { id: 24800 };
      debt.profile = profile;
      const currency: ICurrency = { id: 47302 };
      debt.currency = currency;

      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(debt));
      expect(comp.profilesSharedCollection).toContain(profile);
      expect(comp.currenciesSharedCollection).toContain(currency);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Debt>>();
      const debt = { id: 123 };
      jest.spyOn(debtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: debt }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(debtService.update).toHaveBeenCalledWith(debt);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Debt>>();
      const debt = new Debt();
      jest.spyOn(debtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: debt }));
      saveSubject.complete();

      // THEN
      expect(debtService.create).toHaveBeenCalledWith(debt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Debt>>();
      const debt = { id: 123 };
      jest.spyOn(debtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ debt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(debtService.update).toHaveBeenCalledWith(debt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProfileById', () => {
      it('Should return tracked Profile primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProfileById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCurrencyById', () => {
      it('Should return tracked Currency primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCurrencyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
