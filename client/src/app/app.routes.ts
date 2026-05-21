import { Routes } from '@angular/router';
import { AuthLayout } from '@features/auth/layouts/auth-layout/auth-layout';
import { LoginPage } from '@features/auth/pages/login-page/login-page';
import { RegisterPage } from '@features/auth/pages/register-page/register-page';

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
    }
];
