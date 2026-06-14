import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastType } from '@core/ui/toast/models/toast.model';
import { ToastService } from '@core/ui/toast/service/toast.service';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor:
  HttpInterceptorFn = (
    req,
    next
  ) => {

    const toast =
      inject(ToastService);

    return next(req).pipe(

      catchError(
        (
          error: HttpErrorResponse
        ) => {

          switch (error.status) {

            case 0:

              toast.show(
                ToastType.Error,
                'Servidor indisponível.'
              );

              break;

            case 401:

              toast.show(
                ToastType.Error,
                'Sessão expirada.'
              );

              break;

            case 403:

              toast.show(
                ToastType.Error,
                'Acesso negado.'
              );

              break;

            case 500:

              toast.show(
                ToastType.Error,
                'Erro interno do servidor.'
              );

              break;

            default:

              toast.show(
                ToastType.Error,
                error.error?.message ||
                'Erro inesperado.'
              );
          }

          return throwError(
            () => error
          );
        }
      )
    );
  };
