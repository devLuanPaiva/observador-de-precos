import { Component, inject } from "@angular/core";
import { LoadingService } from "./loading.service";

@Component({
    selector: 'app-loading-overlay',
    templateUrl: './loading-overlay.component.html',
    styleUrls: ['./loading-overlay.component.scss']
})
export class LoadingOverlayComponent {

    private readonly loadingService =
        inject(LoadingService);

    loading =
        this.loadingService.loading;

}