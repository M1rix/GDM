import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDebt, Debt } from '../debt.model';
import { DebtService } from '../service/debt.service';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { DebtStatus } from 'app/entities/enumerations/debt-status.model';

@Component({
  selector: 'jhi-debt-update',
  templateUrl: './debt-update.component.html',
})
export class DebtUpdateComponent implements OnInit {
  isSaving = false;
  debtStatusValues = Object.keys(DebtStatus);

  profilesSharedCollection: IProfile[] = [];
  currenciesSharedCollection: ICurrency[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required]],
    status: [null, [Validators.required]],
    dueDate: [null, [Validators.required]],
    description: [null, [Validators.maxLength(255)]],
    debtScore: [null, [Validators.min(0), Validators.max(10)]],
    createdBy: [null, [Validators.required, Validators.maxLength(50)]],
    createdDate: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    profile: [],
    currency: [],
  });

  constructor(
    protected debtService: DebtService,
    protected profileService: ProfileService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ debt }) => {
      if (debt.id === undefined) {
        const today = dayjs().startOf('day');
        debt.dueDate = today;
        debt.createdDate = today;
        debt.lastModifiedDate = today;
      }

      this.updateForm(debt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const debt = this.createFromForm();
    if (debt.id !== undefined) {
      this.subscribeToSaveResponse(this.debtService.update(debt));
    } else {
      this.subscribeToSaveResponse(this.debtService.create(debt));
    }
  }

  trackProfileById(index: number, item: IProfile): number {
    return item.id!;
  }

  trackCurrencyById(index: number, item: ICurrency): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDebt>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(debt: IDebt): void {
    this.editForm.patchValue({
      id: debt.id,
      amount: debt.amount,
      status: debt.status,
      dueDate: debt.dueDate ? debt.dueDate.format(DATE_TIME_FORMAT) : null,
      description: debt.description,
      debtScore: debt.debtScore,
      createdBy: debt.createdBy,
      createdDate: debt.createdDate ? debt.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: debt.lastModifiedBy,
      lastModifiedDate: debt.lastModifiedDate ? debt.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      profile: debt.profile,
      currency: debt.currency,
    });

    this.profilesSharedCollection = this.profileService.addProfileToCollectionIfMissing(this.profilesSharedCollection, debt.profile);
    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing(this.currenciesSharedCollection, debt.currency);
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query()
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) => this.profileService.addProfileToCollectionIfMissing(profiles, this.editForm.get('profile')!.value))
      )
      .subscribe((profiles: IProfile[]) => (this.profilesSharedCollection = profiles));

    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing(currencies, this.editForm.get('currency')!.value)
        )
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));
  }

  protected createFromForm(): IDebt {
    return {
      ...new Debt(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      status: this.editForm.get(['status'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value ? dayjs(this.editForm.get(['dueDate'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      debtScore: this.editForm.get(['debtScore'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      profile: this.editForm.get(['profile'])!.value,
      currency: this.editForm.get(['currency'])!.value,
    };
  }
}
