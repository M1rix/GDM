import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProfile, Profile } from '../profile.model';
import { ProfileService } from '../service/profile.service';
import { ProfileStatus } from 'app/entities/enumerations/profile-status.model';

@Component({
  selector: 'jhi-profile-update',
  templateUrl: './profile-update.component.html',
})
export class ProfileUpdateComponent implements OnInit {
  isSaving = false;
  profileStatusValues = Object.keys(ProfileStatus);

  editForm = this.fb.group({
    id: [],
    passport: [null, [Validators.maxLength(12)]],
    jshshir: [null, [Validators.maxLength(12)]],
    inn: [null, [Validators.maxLength(15)]],
    phone: [null, [Validators.minLength(12), Validators.maxLength(23)]],
    accessToken: [null, [Validators.required, Validators.minLength(32), Validators.maxLength(255)]],
    firstName: [null, [Validators.maxLength(50)]],
    lastName: [null, [Validators.maxLength(50)]],
    score: [null, [Validators.min(0), Validators.max(10)]],
    status: [null, [Validators.required]],
    createdBy: [null, [Validators.required, Validators.maxLength(50)]],
    createdDate: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
  });

  constructor(protected profileService: ProfileService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profile }) => {
      if (profile.id === undefined) {
        const today = dayjs().startOf('day');
        profile.createdDate = today;
        profile.lastModifiedDate = today;
      }

      this.updateForm(profile);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profile = this.createFromForm();
    if (profile.id !== undefined) {
      this.subscribeToSaveResponse(this.profileService.update(profile));
    } else {
      this.subscribeToSaveResponse(this.profileService.create(profile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfile>>): void {
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

  protected updateForm(profile: IProfile): void {
    this.editForm.patchValue({
      id: profile.id,
      passport: profile.passport,
      jshshir: profile.jshshir,
      inn: profile.inn,
      phone: profile.phone,
      accessToken: profile.accessToken,
      firstName: profile.firstName,
      lastName: profile.lastName,
      score: profile.score,
      status: profile.status,
      createdBy: profile.createdBy,
      createdDate: profile.createdDate ? profile.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: profile.lastModifiedBy,
      lastModifiedDate: profile.lastModifiedDate ? profile.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IProfile {
    return {
      ...new Profile(),
      id: this.editForm.get(['id'])!.value,
      passport: this.editForm.get(['passport'])!.value,
      jshshir: this.editForm.get(['jshshir'])!.value,
      inn: this.editForm.get(['inn'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      accessToken: this.editForm.get(['accessToken'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      score: this.editForm.get(['score'])!.value,
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
