import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICurrency, Currency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';
import { CurrencyStatus } from 'app/entities/enumerations/currency-status.model';

@Component({
  selector: 'jhi-currency-update',
  templateUrl: './currency-update.component.html',
})
export class CurrencyUpdateComponent implements OnInit {
  isSaving = false;
  currencyStatusValues = Object.keys(CurrencyStatus);

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(3)]],
    countryFlag: [null, [Validators.required]],
    position: [],
    status: [null, [Validators.required]],
    createdBy: [null, [Validators.required, Validators.maxLength(50)]],
    createdDate: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
  });

  constructor(protected currencyService: CurrencyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currency }) => {
      if (currency.id === undefined) {
        const today = dayjs().startOf('day');
        currency.createdDate = today;
        currency.lastModifiedDate = today;
      }

      this.updateForm(currency);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const currency = this.createFromForm();
    if (currency.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyService.update(currency));
    } else {
      this.subscribeToSaveResponse(this.currencyService.create(currency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrency>>): void {
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

  protected updateForm(currency: ICurrency): void {
    this.editForm.patchValue({
      id: currency.id,
      code: currency.code,
      countryFlag: currency.countryFlag,
      position: currency.position,
      status: currency.status,
      createdBy: currency.createdBy,
      createdDate: currency.createdDate ? currency.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: currency.lastModifiedBy,
      lastModifiedDate: currency.lastModifiedDate ? currency.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICurrency {
    return {
      ...new Currency(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      countryFlag: this.editForm.get(['countryFlag'])!.value,
      position: this.editForm.get(['position'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
