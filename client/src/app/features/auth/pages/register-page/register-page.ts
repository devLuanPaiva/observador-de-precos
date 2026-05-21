import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthForm } from '@features/auth/components/auth-form/auth-form';
import { AuthService } from '@features/auth/services/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-register-page',
  imports: [
    ReactiveFormsModule,
    AuthForm
  ],
  templateUrl: './register-page.html',
  styleUrl: './register-page.scss',
})
export class RegisterPage {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loading = signal(false);

  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  })

  onSubmit() {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.register(this.form.getRawValue()).pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage.set(err.error?.message || 'Erro ao registrar conta.');
      }
    });
  }
}
