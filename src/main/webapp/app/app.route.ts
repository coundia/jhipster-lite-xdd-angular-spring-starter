import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./login/login.component'),
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin-routing.module'),
  },
  // jhipster-needle-angular-route
];
