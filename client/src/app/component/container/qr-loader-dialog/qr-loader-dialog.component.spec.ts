import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrLoaderDialogComponent } from './qr-loader-dialog.component';

describe('QrLoaderDialogComponent', () => {
  let component: QrLoaderDialogComponent;
  let fixture: ComponentFixture<QrLoaderDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [QrLoaderDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QrLoaderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
