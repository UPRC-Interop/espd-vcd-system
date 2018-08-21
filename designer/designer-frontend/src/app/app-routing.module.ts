import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {RootComponent} from './root/root.component';
import {MiniRenderPageComponent} from './mini-render-page/mini-render-page.component';

const routes: Routes = [
  {
    path: '',
    children: [],
    component: RootComponent
  }, {
    path: 'minipage',
    children: [],
    component: MiniRenderPageComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
