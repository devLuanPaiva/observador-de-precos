import { Component, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '@features/auth/services/auth.service';
import { finalize } from 'rxjs';

import { form, FormField, required, email } from '@angular/forms/signals';

@Component({
  selector: 'app-login-page',
  imports: [ ReactiveFormsModule, FormField],
  templateUrl: './login-page.html',
  styleUrl: './login-page.scss',
})
export class LoginPage {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  isSubmitting = signal(false);
  submitError = signal<string | null>(null);
  submitSuccess = signal(false);
  submitDisabled = signal(false);

  authModel = signal({
    email: '',
    password: '',
  })

  authForm = form(
    this.authModel,
    (schema) => {
      required(schema.email, {
        message: 'O email é obrigatório.'
      });

      email(schema.email, {
        message: 'O email deve ser válido.'
      })

      required(schema.password, {
        message: 'A senha é obrigatória.'
      });

    }
  )


  canSubmit = computed(() => {
    return this.authForm().valid();
  })

  onSubmit() {
    if (!this.canSubmit()) {
      this.authForm().markAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.submitError.set(null);
    this.submitDisabled.set(true);
    this.submitSuccess.set(false);

    this.authService.login(
      this.authModel()
    ).pipe(
      finalize(() => {
        this.isSubmitting.set(false);
        this.submitDisabled.set(false);
        
        this.authModel.set({
          email: '',
          password: '',
        })
        
        this.authForm().reset();
      })
    ).subscribe({
      next: () => {
        this.submitSuccess.set(true);
        this.router.navigate(['/dashboard']);
      },

      error: () => {
        this.submitError.set(
          'Credenciais inválidas. Por favor, verifique seu email e senha e tente novamente.'
        )
      }
    })
  }
}
