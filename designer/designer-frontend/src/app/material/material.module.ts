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
        MatCheckboxModule} from '@angular/material';

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
    MatCheckboxModule
  ],
  exports:[MatButtonModule,MatToolbarModule,MatIconModule,MatStepperModule,MatCardModule,MatRadioModule,MatInputModule,
    MatSelectModule, MatExpansionModule,MatCheckboxModule],
  declarations: []
})
export class MaterialModule { }
