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
    MatListModule
  ],
  exports: [MatButtonModule, MatToolbarModule, MatIconModule, MatStepperModule, MatCardModule, MatRadioModule, MatInputModule,
    MatSelectModule, MatExpansionModule, MatCheckboxModule, MatGridListModule, MatDatepickerModule,
    MatMomentDateModule, MatDialogModule, MatListModule],
  declarations: []
})
export class MaterialModule {
}
