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

import {Component, OnChanges, OnInit} from '@angular/core';
import {DataService} from '../services/data.service';
import {UtilitiesService} from '../services/utilities.service';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit, OnChanges {

  isLinear = true;
  elTitle = 'Προμηθεύς ESPDint – ηλεκτρονική υπηρεσία σύνταξης του Ενιαίου Ευρωπαϊκού Εγγράφου Σύμβασης (ΕΕΕΣ)';
  enTitle = 'Promitheus ESPDint – e-Service to fill out the European Single Procurement Document (ESPD)';

  // eoRelatedFormA = this.dataService.eoRelatedACriteriaForm;

  constructor(public dataService: DataService,
              public utilities: UtilitiesService, private titleService: Title) {
  }

  ngOnInit() {
  }

  ngOnChanges() {
  }

  onLanguageSelection(language: string) {
      // console.log(language);
      this.dataService.switchLanguage(language);
      this.utilities.initLanguage = false;
      this.utilities.start = true;
    this.setTitle(this.utilities.selectedLang);
  }

   setTitle( lang: string) {
     const title = ( lang === 'el' ? this.elTitle : this.enTitle);
    this.titleService.setTitle(title);
  }

}
