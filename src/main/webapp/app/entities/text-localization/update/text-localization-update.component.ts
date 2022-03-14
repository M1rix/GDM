import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITextLocalization, TextLocalization } from '../text-localization.model';
import { TextLocalizationService } from '../service/text-localization.service';

@Component({
  selector: 'jhi-text-localization-update',
  templateUrl: './text-localization-update.component.html',
})
export class TextLocalizationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(150)]],
    message: [null, [Validators.required, Validators.maxLength(150)]],
    locale: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(2)]],
    createdBy: [null, [Validators.required, Validators.maxLength(50)]],
    createdDate: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
  });

  constructor(
    protected textLocalizationService: TextLocalizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ textLocalization }) => {
      if (textLocalization.id === undefined) {
        const today = dayjs().startOf('day');
        textLocalization.createdDate = today;
        textLocalization.lastModifiedDate = today;
      }

      this.updateForm(textLocalization);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const textLocalization = this.createFromForm();
    if (textLocalization.id !== undefined) {
      this.subscribeToSaveResponse(this.textLocalizationService.update(textLocalization));
    } else {
      this.subscribeToSaveResponse(this.textLocalizationService.create(textLocalization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITextLocalization>>): void {
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

  protected updateForm(textLocalization: ITextLocalization): void {
    this.editForm.patchValue({
      id: textLocalization.id,
      code: textLocalization.code,
      message: textLocalization.message,
      locale: textLocalization.locale,
      createdBy: textLocalization.createdBy,
      createdDate: textLocalization.createdDate ? textLocalization.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: textLocalization.lastModifiedBy,
      lastModifiedDate: textLocalization.lastModifiedDate ? textLocalization.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ITextLocalization {
    return {
      ...new TextLocalization(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      message: this.editForm.get(['message'])!.value,
      locale: this.editForm.get(['locale'])!.value,
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
