import {Component} from '@angular/core';
import {DataService} from './services/data.service';
import {TranslateService} from '@ngx-translate/core';
import {Language} from './model/language.model';
import {Currency} from './model/currency.model';

// import {NgForm, FormControl} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isLinear = true;
  language: Language[];

  constructor(public dataService: DataService, public translate: TranslateService) {
    // translate.setDefaultLang('ESPD_en');
    // translate.setDefaultLang('ESPD_de');
    // console.log(this.translate.getLangs());

  }

  // switchLanguage(language: string) {
  //   this.translate.use(language);
  // }
}
