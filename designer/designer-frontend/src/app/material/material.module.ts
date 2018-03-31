import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        MatStepperModule,
        MatCardModule,
        MatRadioModule,
        MatInputModule,
        MatSelectModule,
        MatExpansionModule,
        MatCheckboxModule,
        MatGridListModule} from '@angular/material';

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
    MatGridListModule
  ],
  exports:[MatButtonModule,MatToolbarModule,MatIconModule,MatStepperModule,MatCardModule,MatRadioModule,MatInputModule,
    MatSelectModule, MatExpansionModule,MatCheckboxModule,MatGridListModule],
  declarations: []
})
export class MaterialModule { }
