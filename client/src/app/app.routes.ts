import { Routes } from '@angular/router';
import { authGuard } from '@features/auth/guards/auth.guard';
import { AuthLayout } from '@features/auth/layouts/auth-layout/auth-layout';
import { LoginPage } from '@features/auth/pages/login-page/login-page';
import { RegisterPage } from '@features/auth/pages/register-page/register-page';
import { DashboardPage } from '@features/dashboard/pages/dashboard-page/dashboard-page';
import { AppShellLayout } from '@layouts/app-shell/app-shell-layout/app-shell-layout';

export const routes: Routes = [
    {
        path: '',
        component: AuthLayout,
        children: [
            {
                path: 'login',
                component: LoginPage
            },
            {
                path: 'registrar-usuario',
                component: RegisterPage
            }
        ]
    },
    {
        path: '',
        component: AppShellLayout,
        canActivate: [
            authGuard
        ],
        children: [

            {
                path: 'dashboard',
                component: DashboardPage
            }
        ]
    }
];
