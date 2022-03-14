import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITextLocalization } from '../text-localization.model';

@Component({
  selector: 'jhi-text-localization-detail',
  templateUrl: './text-localization-detail.component.html',
})
export class TextLocalizationDetailComponent implements OnInit {
  textLocalization: ITextLocalization | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ textLocalization }) => {
      this.textLocalization = textLocalization;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
