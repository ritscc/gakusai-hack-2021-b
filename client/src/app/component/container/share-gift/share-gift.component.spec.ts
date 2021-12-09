import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareGiftComponent } from './share-gift.component';

describe('ShareGiftComponent', () => {
  let component: ShareGiftComponent;
  let fixture: ComponentFixture<ShareGiftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShareGiftComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShareGiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
