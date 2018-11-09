///
/// Copyright 2016-2018 University of Piraeus Research Center
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import {Component, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {FormControl, NgForm} from '@angular/forms/forms';
import {ApicallService} from '../services/apicall.service';
import {EoIDType} from '../model/eoIDType.model';
import {WeightingType} from '../model/weightingType.model';
import {UtilitiesService} from '../services/utilities.service';

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {

  // isSatisfiedALL = true;
  // isAtoD = false;
  weightingType: WeightingType[] = null;


  constructor(public dataService: DataService,
              public APIService: ApicallService,
              public utilities: UtilitiesService) {
  }

  ngOnInit() {

    if (this.dataService.isReadOnly() || this.utilities.qualificationApplicationType === 'selfcontained') {
      this.utilities.isAtoD = true;
      this.utilities.isSatisfiedALL = false;
    }

    this.dataService.getWeightingType()
      .then(res => {
        this.weightingType = res;
        // console.log(res);
      })
      .catch(err => {
        console.log(err);
      });

    console.log(this.dataService.CADetails);


  }


  handleALL(radio: FormControl) {
    // console.dir(typeof(radio.value));
    if (radio.value === 'YES') {
      this.utilities.isSatisfiedALL = false;
      this.utilities.isAtoD = true;
      // console.log("This is CA: "+this.isCA);
      // console.log("This is EO: "+this.isEO);
    } else if (radio.value === 'NO') {
      this.utilities.isSatisfiedALL = true;
      this.utilities.isAtoD = false;
    }
  }

  handleGlobalWeight(radio: FormControl) {
    if (radio.value === 'YES') {
      this.utilities.isGloballyWeighted = true;
    } else if (radio.value === 'NO') {
      this.utilities.isGloballyWeighted = false;
    }
  }

  onSelectionSubmit(form: NgForm) {
    console.log(form.value);
    this.dataService.selectionSubmit(
      this.utilities.isSatisfiedALL);
  }

}
