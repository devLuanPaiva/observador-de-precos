import { Component, inject } from '@angular/core';
import { ToastService } from '../../service/toast.service';

@Component({
  selector: 'app-toast-container',
  imports: [],
  templateUrl: './toast-container.html',
  styleUrl: './toast-container.scss',
})
export class ToastContainer {

  readonly toastService = inject(ToastService);
}
