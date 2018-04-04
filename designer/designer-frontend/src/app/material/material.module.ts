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
  MatNativeDateModule
} from '@angular/material';

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
    MatNativeDateModule
  ],
  exports: [MatButtonModule, MatToolbarModule, MatIconModule, MatStepperModule, MatCardModule, MatRadioModule, MatInputModule,
    MatSelectModule, MatExpansionModule, MatCheckboxModule, MatGridListModule, MatDatepickerModule,MatNativeDateModule],
  declarations: []
})
export class MaterialModule {
}
