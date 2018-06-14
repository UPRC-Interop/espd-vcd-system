import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';

import {DataService} from './services/data.service';
import {ApicallService} from './services/apicall.service';

import {AppComponent} from './app.component';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {StartComponent} from './start/start.component';
import {WelcomeComponent} from './welcome/welcome.component';
import {ProcedureComponent} from './procedure/procedure.component';
import {ExclusionComponent} from './exclusion/exclusion.component';
import {ProcedureEoComponent} from './procedure-eo/procedure-eo.component';
import {SelectionComponent} from './selection/selection.component';
import {FinishComponent} from './finish/finish.component';
import {ExclusionEoComponent} from './exclusion-eo/exclusion-eo.component';
import {SelectionEoComponent} from './selection-eo/selection-eo.component';
import {RequirementGroupComponent} from './requirement-group/requirement-group.component';
import {RequirementComponent} from './requirement/requirement.component';
import {RootComponent} from './root/root.component';
import {CriterionComponent} from './criterion/criterion.component';
import {FinishEoComponent} from './finish-eo/finish-eo.component';

/* date and locale */
import {MAT_MOMENT_DATE_FORMATS, MatMomentDateModule, MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material';

/* translations */

import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';


@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    StartComponent,
    WelcomeComponent,
    ProcedureComponent,
    ExclusionComponent,
    ProcedureEoComponent,
    SelectionComponent,
    FinishComponent,
    ExclusionEoComponent,
    SelectionEoComponent,
    RequirementGroupComponent,
    RequirementComponent,
    RootComponent,
    CriterionComponent,
    FinishEoComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatMomentDateModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [ApicallService, DataService,
    {provide: MAT_DATE_LOCALE, useValue: 'en-GB'},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS}],
  bootstrap: [AppComponent]
})
export class AppModule {
}

/* required for AOT */
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
