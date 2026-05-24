import { Component, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { email, form, FormField, maxLength, minLength, pattern, required } from '@angular/forms/signals';
import { Router } from '@angular/router';
import { AuthService } from '@features/auth/services/auth.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-register-page',
  imports: [
    ReactiveFormsModule,
    FormField
  ],
  templateUrl: './register-page.html',
  styleUrl: './register-page.scss',
})
export class RegisterPage {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  isSubmitting = signal(false);
  submitError = signal<string | null>(null);
  submitSuccess = signal(false);
  submitDisabled = signal(false);

  registerModel = signal({
    name: '',
    email: '',
    password: '',
  })

  registerForm = form(
    this.registerModel,
    (schema) => {
      required(schema.name, {
        message: 'O nome é obrigatório.'
      });

      required(schema.email, {
        message: 'O email é obrigatório.'
      });

      email(schema.email, {
        message: 'O email deve ser válido.'
      })

      required(schema.password, {
        message: 'A senha é obrigatória.'
      });

      minLength(schema.password, 6, {
        message: 'A senha deve conter no mínimo 6 caracteres.'
      });

      pattern(schema.password, /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]+$/, {
        message: 'A senha deve conter letras e números.'
      })

      maxLength(schema.password, 20, {
        message: 'A senha deve conter no máximo 20 caracteres.'
      });
    }
  )

  passwordStrength = computed(() => {
    const password = this.registerModel().password;

    if (!password) {
      return 'Vazia';
    }

    if (password.length < 4) {
      return 'Fraca';
    }

    if (password.length < 6) {
      return 'Média';
    }

    if (/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]+$/.test(password)) {
      return 'Forte';
    }

    return 'Média';
  })

  canSubmit = computed(() => {
    return this.registerForm().valid();
  })


  onSubmit() {
    if (!this.canSubmit()) {
      this.registerForm().markAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.submitError.set(null);

    this.authService.register(this.registerModel()).pipe(
      finalize(() => {
        this.isSubmitting.set(false);
        this.submitDisabled.set(false);
        
        this.registerModel.set({
          name: '',
          email: '',
          password: '',
        })
        
        this.registerForm().reset();
      })
      
    ).subscribe({
      next: () => {
        this.submitSuccess.set(true);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.submitError.set(err.error?.message || 'Erro ao registrar conta.');
      }
    });
  }
}
