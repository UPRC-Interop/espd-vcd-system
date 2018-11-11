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
import {FormControl} from '@angular/forms';
import {UtilitiesService} from '../services/utilities.service';
import {CodeList} from '../model/codeList.model';

@Component({
  selector: 'app-selection-eo',
  templateUrl: './selection-eo.component.html',
  styleUrls: ['./selection-eo.component.css']
})
export class SelectionEoComponent implements OnInit {
  // isSatisfiedALL = true;
  // isAtoD = false;
  weightingType: CodeList[] = null;

  constructor(public dataService: DataService, public utilities: UtilitiesService) {
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
  }

  handleALL(radio: FormControl) {
    if (radio.value === 'YES') {
      this.utilities.isSatisfiedALL = false;
      this.utilities.isAtoD = true;
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


  onSelectionEOSubmit() {
    this.dataService.selectionEOSubmit(this.utilities.isSatisfiedALL);
  }
}
