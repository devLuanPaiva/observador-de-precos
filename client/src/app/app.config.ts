import { ApplicationConfig, provideBrowserGlobalErrorListeners, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors, withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { authInterceptor } from '@features/auth/interceptors/auth.interceptor';
import {
  provideLucideIcons, LucideBell, LucideLayoutDashboard,
  LucideChartColumn, LucidePackageSearch, LucideSettings
} from '@lucide/angular';
import { errorInterceptor } from '@core/api/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(
      withFetch(),
      withInterceptors([
        authInterceptor,
        errorInterceptor
      ])
    ),
    provideClientHydration(withEventReplay()),
    provideStore(),
    provideEffects(),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
    provideLucideIcons(
      LucideLayoutDashboard,
      LucideBell,
      LucideChartColumn,
      LucidePackageSearch,
      LucideSettings
    )
  ],
};
