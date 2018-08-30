import {Component, Input, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {SelectionCriteria} from '../model/selectionCriteria.model';
import {RequirementResponse} from '../model/requirement-response.model';
import {RequirementGroup} from '../model/requirementGroup.model';
import {EoRelatedCriterion} from '../model/eoRelatedCriterion.model';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import * as moment from 'moment';
import {Moment} from 'moment';
import {ApicallService} from '../services/apicall.service';
import {EvidenceIssuer} from '../model/evidenceIssuer.model';
import {Evidence} from '../model/evidence.model';
import {FormUtilService} from '../services/form-util.service';

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit {
  // selectionALLCriteria: SelectionCriteria[] = null;


  @Input() selectionACriteria: SelectionCriteria[];
  @Input() selectionBCriteria: SelectionCriteria[];
  @Input() selectionCCriteria: SelectionCriteria[];
  @Input() selectionDCriteria: SelectionCriteria[];
  @Input() selectionALLCriteria: SelectionCriteria[];

  @Input() formA: FormGroup;
  @Input() formB: FormGroup;
  @Input() formC: FormGroup;
  @Input() formD: FormGroup;
  @Input() formALL: FormGroup;
  isSatisfiedALL = true;
  isAtoD = false;

  constructor(public dataService: DataService, private APIService: ApicallService, public formUtil: FormUtilService) {
  }

  ngOnInit() {
  }


  handleALL(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'YES') {
      this.isSatisfiedALL = false;
      this.isAtoD = true;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if (radio.value === 'NO') {
      this.isSatisfiedALL = true;
      this.isAtoD = false;
    }
  }



  onSelectionEOSubmit() {

    this.formUtil.extractFormValuesFromCriteria(this.selectionACriteria, this.formA, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionBCriteria, this.formB, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionCCriteria, this.formC, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.selectionDCriteria, this.formD, this.dataService.evidenceList);
    this.formUtil.extractFormValuesFromCriteria(this.dataService.selectionALLCriteria, this.dataService.selectionALLCriteriaForm, this.dataService.evidenceList);
    // this.selectionACriteria.forEach(cr => {
    //   let formValues = this.formA.getRawValue();
    //   formValues = formValues[cr.uuid.valueOf()];
    //   console.log(formValues);
    //
    //   // let testFormValues = formValues[cr.uuid.valueOf()];
    //   // console.log('cr loop: ' + cr.uuid);
    //
    //   let testFormValues = null;
    //
    //   cr.requirementGroups.forEach(rg => {
    //     // console.log('first rg loop: ' + rg.uuid);
    //
    //     if (testFormValues == null) {
    //       testFormValues = this.formA.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       // formValues = testFormValues;
    //     }
    //
    //     if (formValues[rg.uuid.valueOf()] == undefined) {
    //       // console.log('THIS IS undefined');
    //       testFormValues = testFormValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formA, testFormValues);
    //     } else if (formValues[rg.uuid.valueOf()] != undefined) {
    //       // console.log('THIS IS DEFINED');
    //       formValues = formValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formA, formValues);
    //     }
    //   });
    // });

    // this.selectionBCriteria.forEach(cr => {
    //   let formValues = this.formB.getRawValue();
    //   formValues = formValues[cr.uuid.valueOf()];
    //   console.log(formValues);
    //   console.log('THIS IS FORM B =================================');
    //
    //   // let testFormValues = formValues[cr.uuid.valueOf()];
    //   // console.log('cr loop: ' + cr.uuid);
    //
    //   let testFormValues = null;
    //
    //   cr.requirementGroups.forEach(rg => {
    //     // console.log('first rg loop: ' + rg.uuid);
    //     if (testFormValues == null) {
    //       testFormValues = this.formB.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       // formValues = testFormValues;
    //     }
    //
    //     if (formValues[rg.uuid.valueOf()] == undefined) {
    //       // console.log('THIS IS undefined');
    //
    //       // fix
    //       let testFormValues = null;
    //       testFormValues = this.formB.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       testFormValues = testFormValues[rg.uuid.valueOf()];
    //       // fix
    //
    //       this.reqGroupMatch(rg, cr, this.formB, testFormValues);
    //     } else if (formValues[rg.uuid.valueOf()] != undefined) {
    //       // console.log('THIS IS DEFINED');
    //       formValues = formValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formB, formValues);
    //     }
    //   });
    // });
    //
    // this.selectionCCriteria.forEach(cr => {
    //   let formValues = this.formC.getRawValue();
    //   formValues = formValues[cr.uuid.valueOf()];
    //   // console.log('THIS IS FORM C =================================');
    //   console.log(formValues);
    //
    //   // let testFormValues = formValues[cr.uuid.valueOf()];
    //   // console.log('cr loop: ' + cr.uuid);
    //
    //   let testFormValues = null;
    //
    //   cr.requirementGroups.forEach(rg => {
    //     // console.log('first rg loop: ' + rg.uuid);
    //
    //     if (testFormValues == null) {
    //       testFormValues = this.formC.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       // formValues = testFormValues;
    //     }
    //
    //     if (formValues[rg.uuid.valueOf()] == undefined) {
    //       // console.log('THIS IS undefined');
    //       // fix
    //       let testFormValues = null;
    //       testFormValues = this.formC.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       testFormValues = testFormValues[rg.uuid.valueOf()];
    //       // fix
    //
    //       this.reqGroupMatch(rg, cr, this.formC, testFormValues);
    //     } else if (formValues[rg.uuid.valueOf()] != undefined) {
    //       // console.log('THIS IS DEFINED');
    //       formValues = formValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formC, formValues);
    //     }
    //   });
    // });
    //
    // this.selectionDCriteria.forEach(cr => {
    //   let formValues = this.formD.getRawValue();
    //   formValues = formValues[cr.uuid.valueOf()];
    //   console.log(formValues);
    //   // console.log('THIS IS FORM D ==============================');
    //
    //   // let testFormValues = formValues[cr.uuid.valueOf()];
    //   // console.log('cr loop: ' + cr.uuid);
    //
    //   let testFormValues = null;
    //
    //   cr.requirementGroups.forEach(rg => {
    //     // console.log('first rg loop: ' + rg.uuid);
    //
    //     if (testFormValues == null) {
    //       testFormValues = this.formD.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       // formValues = testFormValues;
    //     }
    //
    //     if (formValues[rg.uuid.valueOf()] == undefined) {
    //       // console.log('THIS IS undefined');
    //       testFormValues = testFormValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formD, testFormValues);
    //     } else if (formValues[rg.uuid.valueOf()] != undefined) {
    //       // console.log('THIS IS DEFINED');
    //       formValues = formValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.formD, formValues);
    //     }
    //   });
    // });

    // this.dataService.selectionALLCriteria.forEach(cr => {
    //   let formValues = this.dataService.selectionALLCriteriaForm.getRawValue();
    //   formValues = formValues[cr.uuid.valueOf()];
    //   // console.log('THIS IS FORM C =================================');
    //   console.log(formValues);
    //
    //   // let testFormValues = formValues[cr.uuid.valueOf()];
    //   // console.log('cr loop: ' + cr.uuid);
    //
    //   let testFormValues = null;
    //
    //   cr.requirementGroups.forEach(rg => {
    //     // console.log('first rg loop: ' + rg.uuid);
    //
    //     if (testFormValues == null) {
    //       testFormValues = this.formC.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       // formValues = testFormValues;
    //     }
    //
    //     if (formValues[rg.uuid.valueOf()] == undefined) {
    //       // console.log('THIS IS undefined');
    //       // fix
    //       let testFormValues = null;
    //       testFormValues = this.dataService.selectionALLCriteriaForm.getRawValue();
    //       testFormValues = testFormValues[cr.uuid.valueOf()];
    //       testFormValues = testFormValues[rg.uuid.valueOf()];
    //       // fix
    //
    //       this.reqGroupMatch(rg, cr, this.dataService.selectionALLCriteriaForm, testFormValues);
    //     } else if (formValues[rg.uuid.valueOf()] != undefined) {
    //       // console.log('THIS IS DEFINED');
    //       formValues = formValues[rg.uuid.valueOf()];
    //       this.reqGroupMatch(rg, cr, this.dataService.selectionALLCriteriaForm, formValues);
    //     }
    //   });
    // });

    console.log(this.selectionACriteria);
    console.log(this.selectionBCriteria);
    console.log(this.selectionCCriteria);
    console.log(this.selectionDCriteria);
    console.log(this.isSatisfiedALL);
    this.dataService.selectionEOSubmit(this.selectionACriteria,
      this.selectionBCriteria,
      this.selectionCCriteria,
      this.selectionDCriteria,
      this.isSatisfiedALL);
  }
}
