import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from './material/material.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
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
import { RootComponent } from './root/root.component';
import { CriterionComponent } from './criterion/criterion.component';



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
    CriterionComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [ApicallService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
