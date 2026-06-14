import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  theme = signal<'light' | 'dark'>('dark');

  setTheme(theme: 'light' | 'dark') {
    document.body.setAttribute('data-theme', theme);
    this.theme.set(theme);
  }
}
