///
/// Copyright 2016-2019 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  MatButtonModule,
  MatToolbarModule,
  MatIconModule,
  MatStepperModule,
  MatCardModule,
  MatRadioModule,
  MatInputModule,
  MatSelectModule,
  MatExpansionModule,
  MatCheckboxModule,
  MatGridListModule,
  MatDatepickerModule,
  MatProgressSpinnerModule,
  MatSnackBarModule,
  MatChipsModule,
  MatListModule
} from '@angular/material';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {MatDialogModule} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatStepperModule,
    MatCardModule,
    MatRadioModule,
    MatInputModule,
    MatSelectModule,
    MatExpansionModule,
    MatCheckboxModule,
    MatGridListModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatMomentDateModule,
    MatSnackBarModule,
    MatChipsModule,
    MatListModule
  ],
  exports: [MatButtonModule, MatToolbarModule, MatIconModule, MatStepperModule, MatCardModule, MatRadioModule, MatInputModule,
    MatSelectModule, MatExpansionModule, MatCheckboxModule, MatGridListModule, MatDatepickerModule,
    MatMomentDateModule, MatDialogModule, MatListModule, MatProgressSpinnerModule,
    MatSnackBarModule, MatChipsModule],
  declarations: []
})
export class MaterialModule {
}
