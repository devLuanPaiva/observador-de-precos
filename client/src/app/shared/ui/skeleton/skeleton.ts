import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-skeleton',
  imports: [],
  templateUrl: './skeleton.html',
  styleUrl: './skeleton.scss',
})
export class Skeleton {
  @Input()
  height = '20px';

  @Input()
  width = '100%';

  @Input()
  borderRadius = '14px';
}
