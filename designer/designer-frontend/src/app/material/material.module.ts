import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        MatStepperModule,
        MatCardModule,
        MatRadioModule,
        MatInputModule,
        MatSelectModule} from '@angular/material';

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
    MatSelectModule
  ],
  exports:[MatButtonModule,MatToolbarModule,MatIconModule,MatStepperModule,MatCardModule,MatRadioModule,MatInputModule, MatSelectModule],
  declarations: []
})
export class MaterialModule { }
