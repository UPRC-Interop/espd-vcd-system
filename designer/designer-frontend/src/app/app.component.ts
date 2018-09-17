import {Component} from '@angular/core';
import {DataService} from './services/data.service';
import {TranslateService} from '@ngx-translate/core';

// import {NgForm, FormControl} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isLinear = true;

  constructor(public dataService: DataService, public translate: TranslateService) {
    translate.setDefaultLang('ESPD_en');
    // translate.setDefaultLang('ESPD_de');
  }

  switchLanguage(language: string) {
    this.translate.use(language);
  }
}
