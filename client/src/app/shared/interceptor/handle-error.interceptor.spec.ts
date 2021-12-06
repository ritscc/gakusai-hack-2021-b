import { TestBed } from '@angular/core/testing';

import { HandleErrorInterceptor } from './handle-error.interceptor';

describe('HandleErrorInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [HandleErrorInterceptor],
    })
  );

  it('should be created', () => {
    const interceptor: HandleErrorInterceptor = TestBed.inject(HandleErrorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
