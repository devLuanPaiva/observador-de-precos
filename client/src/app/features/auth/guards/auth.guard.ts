import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthSessionService } from '../services/auth-session.service';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  const session =
    inject(AuthSessionService);

  if (
    !session.authenticated()
  ) {

    router.navigate(['/login']);

    return false;
  }

  return true;
};
