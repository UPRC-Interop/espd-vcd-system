import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from '@angular/common/http';

import {DataService} from "./services/data.service";
import {ApicallService} from "./services/apicall.service"

import { AppComponent } from './app.component';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { StartComponent } from './start/start.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ProcedureComponent } from './procedure/procedure.component';
import { ExclusionComponent } from './exclusion/exclusion.component';
import { ProcedureEoComponent } from './procedure-eo/procedure-eo.component';
import { SelectionComponent } from './selection/selection.component';
import { FinishComponent } from './finish/finish.component';


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
    FinishComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [ApicallService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
