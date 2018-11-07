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
import {ExportType} from "../export/export-type.enum";

@Component({
  selector: 'app-finish-eo',
  templateUrl: './finish-eo.component.html',
  styleUrls: ['./finish-eo.component.css']
})
export class FinishEoComponent implements OnInit {

  constructor(public dataService: DataService) {
  }

  ngOnInit() {

  }

  onHtmlExport() {
    this.dataService.finishEOSubmit(ExportType.HTML);
  }

  onPdfExport() {
    this.dataService.finishEOSubmit(ExportType.PDF);
  }

  onXmlExport() {
    this.dataService.finishEOSubmit(ExportType.XML);
  }

}
