import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';

import { UserMenuComponent } from './user-menu';
import { AuthService } from '@features/auth/services/auth.service';

class MockAuthService {
  logout(): void {
    void 0;
  }
}

class MockRouter {
  navigate(commands: string[]): Promise<boolean> {
    return Promise.resolve(true);
  }
}

describe('UserMenuComponent', () => {
  let fixture: ComponentFixture<UserMenuComponent>;
  let component: UserMenuComponent;
  let authService: MockAuthService;
  let router: MockRouter;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserMenuComponent],
      providers: [
        { provide: AuthService, useClass: MockAuthService },
        { provide: Router, useClass: MockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserMenuComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as MockAuthService;
    router = TestBed.inject(Router) as MockRouter;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with the menu closed (open === false)', () => {
    const isOpen: boolean = component.open();
    expect(isOpen).toBe(false);
  });

  it('should open the menu when clicking the trigger button', async () => {
    const triggerButton: HTMLButtonElement | null = fixture.nativeElement.querySelector('.user-menu__trigger') as HTMLButtonElement | null;
    expect(triggerButton).toBeTruthy();

    triggerButton!.click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.open()).toBe(true);
  });

  it('should close the menu when clicking the trigger button again', async () => {
    const triggerButton: HTMLButtonElement | null = fixture.nativeElement.querySelector('.user-menu__trigger') as HTMLButtonElement | null;
    expect(triggerButton).toBeTruthy();

    triggerButton!.click();
    fixture.detectChanges();
    await fixture.whenStable();

    triggerButton!.click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.open()).toBe(false);
  });

  it('should call AuthService.logout and navigate to /login when clicking "Sair"', async () => {
    const spyLogout = vi.spyOn(authService, 'logout');
    const spyNavigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);

    component.open.set(true);
    fixture.detectChanges();
    await fixture.whenStable();

    const buttonsNodeList: NodeListOf<HTMLButtonElement> = fixture.nativeElement.querySelectorAll('button');
    const logoutButton: HTMLButtonElement | undefined = Array.from(buttonsNodeList).find(
      (b: HTMLButtonElement) => b.textContent !== null && b.textContent.trim() === 'Sair'
    );

    expect(logoutButton).toBeDefined();

    logoutButton!.click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(spyLogout).toHaveBeenCalledTimes(1);
    expect(spyNavigate).toHaveBeenCalledWith(['/login']);
  });
});
