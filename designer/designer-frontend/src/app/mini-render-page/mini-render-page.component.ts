import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {ExclusionCriteria} from '../model/exclusionCriteria.model';
import {FormGroup} from '@angular/forms';
import {ApicallService} from '../services/apicall.service';

@Component({
  selector: 'app-mini-render-page',
  templateUrl: './mini-render-page.component.html',
  styleUrls: ['./mini-render-page.component.css']
})
export class MiniRenderPageComponent implements OnInit {

  // exclusionACriteria: ExclusionCriteria[] = null;
  // public exclusionACriteriaForm: FormGroup = null;

  exclusionACriteria: any = null;
  exclusionACriteriaForm: FormGroup = null;


  constructor(public dataService: DataService, private APIService: ApicallService) {
  }

  ngOnInit() {
    this.dataService.isEO = true;

    this.exclusionACriteria = [{
      'typeCode': 'CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION',
      'name': 'Participation in a criminal organisation',
      'description': 'Participation in a criminal organisation, as defined in Article 2 of Council etc...',
      'legislationReference': {
        'title': 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL  etc..',
        'jurisdictionLevelCode': 'EU_DIRECTIVE',
        'article': '57(1)',
        'description': 'Directive 2014/24/EU',
        'uri': 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'
      },
      'requirementGroups': [{
        'requirements': [{
          'responseDataType': 'INDICATOR',
          'description': 'Your answer',
          'typeCode': 'QUESTION',
          'response': null,
          'id': '771869ed-e733-4bd0-b5ee-6ad81c50d823',
          'uuid': '771869ed-e733-4bd0-b5ee-6ad81c50d823-5'
        }],
        'requirementGroups': [{
          'requirements': [{
            'responseDataType': 'DATE',
            'description': 'Date of conviction',
            'typeCode': 'QUESTION',
            'response': null,
            'id': '5d289637-6f8a-4a19-98cc-0e6b9e9bc8ae',
            'uuid': '5d289637-6f8a-4a19-98cc-0e6b9e9bc8ae-5'
          }, {
            'responseDataType': 'DESCRIPTION',
            'description': 'Reason',
            'typeCode': 'QUESTION',
            'response': null,
            'id': '61839c98-87df-4bf1-b246-b0b86b191206',
            'uuid': '61839c98-87df-4bf1-b246-b0b86b191206-5'
          }, {
            'responseDataType': 'DESCRIPTION',
            'description': 'Who has been convicted',
            'typeCode': 'QUESTION',
            'response': null,
            'id': '313603f8-4392-40ec-92bc-3a0d899de85e',
            'uuid': '313603f8-4392-40ec-92bc-3a0d899de85e-5'
          }, {
            'responseDataType': 'DESCRIPTION',
            'description': 'Length of the period of exclusion',
            'typeCode': 'QUESTION',
            'response': null,
            'id': 'e1e5308f-f67a-4e52-8723-a5e2b19fe0a8',
            'uuid': 'e1e5308f-f67a-4e52-8723-a5e2b19fe0a8-5'
          }],
          'requirementGroups': [{
            'requirements': [{
              'responseDataType': 'INDICATOR',
              'description': 'Have you taken measures to demonstrate your reliability (Self-Cleaning)?',
              'typeCode': 'QUESTION',
              'response': null,
              'id': '397f7209-2efb-414a-b008-3f3c527a09fc',
              'uuid': '397f7209-2efb-414a-b008-3f3c527a09fc-5'
            }],
            'requirementGroups': [{
              'requirements': [{
                'responseDataType': 'DESCRIPTION',
                'description': 'Please describe them',
                'typeCode': 'QUESTION',
                'response': null,
                'id': '244aa3ab-ae24-4c56-8f3a-6626074de931',
                'uuid': '244aa3ab-ae24-4c56-8f3a-6626074de931-5'
              }],
              'requirementGroups': [],
              'ruleSet': null,
              'condition': 'ONTRUE',
              'id': '74e6c7b4-757b-4b40-ada6-fad6a997c310',
              'uuid': '74e6c7b4-757b-4b40-ada6-fad6a997c310-4'
            }],
            'ruleSet': null,
            'condition': 'ONTRUE',
            'id': 'f4978772-3126-4ded-bc30-f50da8c3a038',
            'uuid': 'f4978772-3126-4ded-bc30-f50da8c3a038-3'
          }],
          'ruleSet': null,
          'condition': 'ONTRUE',
          'id': 'f5276600-a2b6-4ff6-a90e-b31fe19dae41',
          'uuid': 'f5276600-a2b6-4ff6-a90e-b31fe19dae41-2'
        }],
        'ruleSet': null,
        'condition': 'ON*',
        'id': '7c637c0c-7703-4389-ba52-02997a055bd7',
        'uuid': '7c637c0c-7703-4389-ba52-02997a055bd7-1'
      }, {
        'requirements': [{
          'responseDataType': 'INDICATOR',
          'description': 'Is this information available electronically?',
          'typeCode': 'QUESTION',
          'response': null,
          'id': 'be3753e3-9273-4a68-9ad2-c19886d47576',
          'uuid': 'be3753e3-9273-4a68-9ad2-c19886d47576-7'
        }],
        'requirementGroups': [{
          'requirements': [{
            'responseDataType': 'EVIDENCE_IDENTIFIER',
            'description': 'Evidence supplied',
            'typeCode': 'QUESTION',
            'response': null,
            'id': 'ca0f12ca-f75d-42f0-92e9-c5c129c21b25',
            'uuid': 'ca0f12ca-f75d-42f0-92e9-c5c129c21b25-7'
          }],
          'requirementGroups': [],
          'ruleSet': null,
          'condition': 'ONTRUE',
          'id': '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b',
          'uuid': '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b-6'
        }],
        'ruleSet': null,
        'condition': 'ON*',
        'id': '7458d42a-e581-4640-9283-34ceb3ad4345',
        'uuid': '7458d42a-e581-4640-9283-34ceb3ad4345-5'
      }],
      'selected': true,
      'id': '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab',
      'uuid': '005eb9ed-1347-4ca3-bb29-9bc0db64e1ab',
      'criterionGroup': 'CRITERION.EXCLUSION.CONVICTIONS'
    }];
    // this.APIService.version = 'v2';


    // this.APIService.getExclusionCriteria_A().then(res => {
    //   this.dataService.exclusionACriteria = res;
    //   this.dataService.exclusionACriteriaForm = this.dataService.createExclusionCriterionForm(this.dataService.exclusionACriteria);
    //   console.log(this.dataService.exclusionACriteriaForm);
    // }).catch(err => {
    //   console.log(err);
    // });

    this.exclusionACriteriaForm = this.dataService.createExclusionCriterionForm(this.exclusionACriteria);


  }

  onMockSubmit() {
    const formValues = this.exclusionACriteriaForm.getRawValue();
    console.log(formValues);
    console.log(this.exclusionACriteriaForm);
  }

}
