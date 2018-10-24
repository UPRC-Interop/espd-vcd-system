import {QueryList} from "@angular/core";
import {NgForm} from "@angular/forms";
import {WizardSteps} from "./wizard-steps.enum";

export interface BaseStep {
  forms: QueryList<NgForm>

  areFormsValid(): boolean
  getWizardStep(): WizardSteps
}
