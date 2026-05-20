import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormGroup, ReactiveFormsModule
} from '@angular/forms';

@Component({
  selector: 'app-auth-form',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './auth-form.html',
  styleUrl: './auth-form.scss',
})
export class AuthForm {
  @Input({ required: true })
  title!: string;

  @Input({ required: true })
  buttonLabel!: string;

  @Input({ required: true })
  form!: FormGroup;

  @Input()
  isRegister = false;

  @Input()
  loading = false;
  @Input()
  errorMessage: string | null = null;

  @Output()
  submitted = new EventEmitter<void>();


  onSubmit() {
    this.form.markAllAsTouched()
    if (this.form.invalid) {
      return
    }
    this.submitted.emit();
  }
}
