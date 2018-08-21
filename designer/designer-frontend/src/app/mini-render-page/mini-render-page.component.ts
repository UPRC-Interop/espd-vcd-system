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
    this.exclusionACriteria = [{
      'typeCode': 'CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION',
      'name': 'Participation in a criminal organisation',
      'description': 'Has the economic operator itself or any person who is a member of its Administrative, etc...',
      'legislationReference': {
        'title': 'DIRECTIVE 2014/24/EU OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL of 26 February 2014 etc...',
        'jurisdictionLevelCode': 'EU_DIRECTIVE',
        'article': '57(1)',
        'description': 'Directive 2014/24/EU',
        'uri': 'http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=celex:32014L0024'
      },
      'requirementGroups': [
        {
          'requirements': [
            {
              'responseDataType': 'INDICATOR',
              'description': 'Your answer?',
              'typeCode': 'QUESTION',
              'response': {
                'validatedCriterionPropertyID': '20e90057-43a6-42a6-9769-d3d6222a60e0',
                'confidentialityLevelCode': 'PUBLIC',
                'responseType': 'INDICATOR',
                'indicator': false,
                'id': null,
                'uuid': null
              },
              'id': '20e90057-43a6-42a6-9769-d3d6222a60e0',
              'uuid': '20e90057-43a6-42a6-9769-d3d6222a60e0-5'
            }
          ],
          'requirementGroups': [
            {
              'requirements': [
                {
                  'responseDataType': 'DATE',
                  'description': 'Date of conviction',
                  'typeCode': 'QUESTION',
                  'response': null,
                  'id': '4760ea36-4480-4440-b1e3-f0baa9106d7c',
                  'uuid': '4760ea36-4480-4440-b1e3-f0baa9106d7c-5'
                },
                {
                  'responseDataType': 'DESCRIPTION',
                  'description': 'Reason',
                  'typeCode': 'QUESTION',
                  'response': null,
                  'id': '7f8f9fb0-d405-42ca-a270-6b58d9cde696',
                  'uuid': '7f8f9fb0-d405-42ca-a270-6b58d9cde696-5'
                },
                {
                  'responseDataType': 'DESCRIPTION',
                  'description': 'Who has been convicted',
                  'typeCode': 'QUESTION',
                  'response': null,
                  'id': '3b7462a7-5aaa-4f65-96f0-5a7ffe5cb8e3',
                  'uuid': '3b7462a7-5aaa-4f65-96f0-5a7ffe5cb8e3-5'
                },
                {
                  'responseDataType': 'DESCRIPTION',
                  'description': 'Length of the period of exclusion',
                  'typeCode': 'QUESTION',
                  'response': null,
                  'id': '655cc87c-48a3-4921-be67-9a6b7e8076ac',
                  'uuid': '655cc87c-48a3-4921-be67-9a6b7e8076ac-5'
                }
              ],
              'requirementGroups': [
                {
                  'requirements': [
                    {
                      'responseDataType': 'INDICATOR',
                      'description': 'Have you taken measures to demonstrate your reliability ("Self-Cleaning")?',
                      'typeCode': 'QUESTION',
                      'response': null,
                      'id': 'fb833952-b629-4577-9a8a-7b429b98f943',
                      'uuid': 'fb833952-b629-4577-9a8a-7b429b98f943-5'
                    }
                  ],
                  'requirementGroups': [
                    {
                      'requirements': [
                        {
                          'responseDataType': 'DESCRIPTION',
                          'description': 'Please describe them',
                          'typeCode': 'QUESTION',
                          'response': null,
                          'id': '1610fb0a-74d6-4527-be99-97664fdfd159',
                          'uuid': '1610fb0a-74d6-4527-be99-97664fdfd159-5'
                        }
                      ],
                      'requirementGroups': [],
                      'condition': 'ONTRUE',
                      'id': '74e6c7b4-757b-4b40-ada6-fad6a997c310',
                      'uuid': '74e6c7b4-757b-4b40-ada6-fad6a997c310-4'
                    }
                  ],
                  'condition': 'ONTRUE',
                  'id': 'f4978772-3126-4ded-bc30-f50da8c3a038',
                  'uuid': 'f4978772-3126-4ded-bc30-f50da8c3a038-3'
                }
              ],
              'condition': 'ONTRUE',
              'id': 'f5276600-a2b6-4ff6-a90e-b31fe19dae41',
              'uuid': 'f5276600-a2b6-4ff6-a90e-b31fe19dae41-2'
            }
          ],
          'condition': 'ON*',
          'id': '7c637c0c-7703-4389-ba52-02997a055bd7',
          'uuid': '7c637c0c-7703-4389-ba52-02997a055bd7-1'
        },
        {
          'requirements': [
            {
              'responseDataType': 'INDICATOR',
              'description': 'Is this information available electronically?',
              'typeCode': 'QUESTION',
              'response': {
                'validatedCriterionPropertyID': '4bada04a-933d-436b-8f46-70c8c07ac90c',
                'confidentialityLevelCode': 'PUBLIC',
                'responseType': 'INDICATOR',
                'indicator': false,
                'id': null,
                'uuid': null
              },
              'id': '4bada04a-933d-436b-8f46-70c8c07ac90c',
              'uuid': '4bada04a-933d-436b-8f46-70c8c07ac90c-7'
            }
          ],
          'requirementGroups': [
            {
              'requirements': [
                {
                  'responseDataType': 'EVIDENCE_IDENTIFIER',
                  'description': 'Evidence supplied',
                  'typeCode': 'QUESTION',
                  'response': null,
                  'id': 'b618722d-7661-4770-bc03-f0e5bb43393b',
                  'uuid': 'b618722d-7661-4770-bc03-f0e5bb43393b-7'
                }
              ],
              'requirementGroups': [],
              'condition': 'ONTRUE',
              'id': '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b',
              'uuid': '41dd2e9b-1bfd-44c7-93ee-56bd74a4334b-6'
            }
          ],
          'condition': 'ON*',
          'id': '7458d42a-e581-4640-9283-34ceb3ad4345',
          'uuid': '7458d42a-e581-4640-9283-34ceb3ad4345-5'
        }
      ],
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
  }

}
