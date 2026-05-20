import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthForm } from '@features/auth/components/auth-form/auth-form';
import { AuthService } from '@features/auth/services/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login-page',
  imports: [AuthForm, ReactiveFormsModule],
  templateUrl: './login-page.html',
  styleUrl: './login-page.scss',
})
export class LoginPage {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loading = signal(false);

  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    email: [
      '',
      [
        Validators.required,
        Validators.email,
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(6),
      ]
    ]
  })

  onSubmit() {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.login(
      this.form.getRawValue()
    ).pipe(
      finalize(() => {
        this.loading.set(false);
      })
    ).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },

      error: () => {
        this.errorMessage.set(
          'Credenciais inválidas. Por favor, verifique seu email e senha e tente novamente.'
        )
      }
    })
  }

}
