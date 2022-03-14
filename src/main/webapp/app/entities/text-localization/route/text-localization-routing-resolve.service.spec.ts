import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITextLocalization, TextLocalization } from '../text-localization.model';
import { TextLocalizationService } from '../service/text-localization.service';

import { TextLocalizationRoutingResolveService } from './text-localization-routing-resolve.service';

describe('TextLocalization routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TextLocalizationRoutingResolveService;
  let service: TextLocalizationService;
  let resultTextLocalization: ITextLocalization | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TextLocalizationRoutingResolveService);
    service = TestBed.inject(TextLocalizationService);
    resultTextLocalization = undefined;
  });

  describe('resolve', () => {
    it('should return ITextLocalization returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTextLocalization = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTextLocalization).toEqual({ id: 123 });
    });

    it('should return new ITextLocalization if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTextLocalization = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTextLocalization).toEqual(new TextLocalization());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TextLocalization })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTextLocalization = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTextLocalization).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
