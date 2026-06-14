import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router } from "@angular/router";
import { AuthSessionService } from "@features/auth/services/auth-session.service";

export const roleGuard: CanActivateFn =
(route: ActivatedRouteSnapshot) => {

  const session =
    inject(AuthSessionService);

  const router =
    inject(Router);

  const expectedRole =
    route.data['role'];

  const user =
    session.user();

  if (!user) {

    router.navigate(['/login']);

    return false;
  }

  if (user.role !== expectedRole) {

    router.navigate(['/dashboard']);

    return false;
  }

  return true;
};