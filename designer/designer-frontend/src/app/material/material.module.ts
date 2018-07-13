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
  MatSnackBarModule
} from '@angular/material';
import {MatMomentDateModule} from '@angular/material-moment-adapter';

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
    MatSnackBarModule
  ],
  exports: [MatButtonModule, MatToolbarModule, MatIconModule, MatStepperModule, MatCardModule, MatRadioModule, MatInputModule,
    MatSelectModule, MatExpansionModule, MatCheckboxModule, MatGridListModule, MatDatepickerModule, MatMomentDateModule, MatSnackBarModule],
  declarations: []
})
export class MaterialModule {
}
