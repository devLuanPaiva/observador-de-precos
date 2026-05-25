import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { provideLucideIcons, LucideLayoutDashboard, LucideBell, LucideChartColumn, LucidePackageSearch, LucideSettings } from '@lucide/angular';

import { SidebarComponent } from './sidebar';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [
        provideRouter([]),
        { provide: ActivatedRoute, useValue: { snapshot: {}, url: [] } },
        provideLucideIcons(
          LucideLayoutDashboard,
          LucideBell,
          LucideChartColumn,
          LucidePackageSearch,
          LucideSettings
        ),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize collapsed as false', () => {
    const initialCollapsed: boolean = component.collapsed();
    expect(initialCollapsed).toBe(false);
  });

  it('should toggle collapsed state when clicking the collapse button', async () => {
    const collapseButton: HTMLElement | null = fixture.nativeElement.querySelector('.sidebar__collapse') as HTMLElement | null;
    expect(collapseButton).not.toBeNull();

    (collapseButton as HTMLElement).click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.collapsed()).toBe(true);

    (collapseButton as HTMLElement).click();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.collapsed()).toBe(false);
  });

  it('should add the class sidebar--collapsed when the menu is collapsed', async () => {
    const asideEl: HTMLElement | null = fixture.nativeElement.querySelector('aside.sidebar') as HTMLElement | null;
    expect(asideEl).not.toBeNull();

    component.collapsed.set(true);
    fixture.detectChanges();
    await fixture.whenStable();

    expect((asideEl as HTMLElement).classList.contains('sidebar--collapsed')).toBe(true);
  });

  it('should hide the labels of the items when collapsed is true', async () => {
    const labelsBefore: NodeListOf<HTMLSpanElement> = fixture.nativeElement.querySelectorAll('.sidebar__nav .sidebar__item span') as NodeListOf<HTMLSpanElement>;
    expect(labelsBefore.length).toBeGreaterThan(0);

    component.collapsed.set(true);
    fixture.detectChanges();
    await fixture.whenStable();

    const labelsAfter: NodeListOf<HTMLSpanElement> = fixture.nativeElement.querySelectorAll('.sidebar__nav .sidebar__item span') as NodeListOf<HTMLSpanElement>;
    expect(labelsAfter.length).toBe(0);
  });
});
