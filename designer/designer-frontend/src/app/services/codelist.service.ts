import {Injectable} from '@angular/core';
import {CodeList} from '../model/codeList.model';
import {ApicallService} from './apicall.service';
import {DataService} from './data.service';
import {UtilitiesService} from './utilities.service';

@Injectable({
  providedIn: 'root'
})
export class CodelistService {

  currency: CodeList[] = null;
  countries: CodeList[] = null;
  procedureTypes: CodeList[] = null;
  projectTypes: CodeList[] = null;
  bidTypes: CodeList[] = null;
  eoRoleTypes: CodeList[] = null;
  eoIDType: CodeList[] = null;
  financialRatioTypes: CodeList[] = null;
  weightingType: CodeList[] = null;
  evaluationMethodType: CodeList[] = null;


  constructor(private APIService: ApicallService, public utilities: UtilitiesService) {

    if (this.currency === null) {
      this.getCurrency()
        .then(res => {
          this.currency = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.countries === null) {
      this.getCountries()
        .then(res => {
          this.countries = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.eoIDType === null) {
      this.getEoIDTypes()
        .then(res => {
          this.eoIDType = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.bidTypes === null) {
      this.getBidTypes()
        .then(res => {
          this.bidTypes = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.financialRatioTypes === null) {
      this.getFinancialRatioTypes()
        .then(res => {
          this.financialRatioTypes = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.procedureTypes === null) {
      this.getProcedureTypes()
        .then(res => {
          this.procedureTypes = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.projectTypes === null) {
      this.getProjectTypes()
        .then(res => {
          this.projectTypes = res;
          // console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    }

    if (this.eoRoleTypes === null) {
      this.getEORoleTypes()
        .then(res => {
          this.eoRoleTypes = res;
        })
        .catch(err => {
            console.log(err);
          }
        );
    }



    // if (this.evaluationMethodType === null) {
    //   this.getEvalutationMethodTypes()
    //     .then(res => {
    //       this.evaluationMethodTypes = res;
    //       // console.log(res);
    //     })
    //     .catch(err => {
    //       console.log(err);
    //     });
    // }


  }


  getCurrency(): Promise<CodeList[]> {
    if (this.currency !== null && this.currency !== undefined) {
      return Promise.resolve(this.currency);
    } else {
      return this.APIService.getCurr()
        .then(res => {
          this.currency = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getCountries(): Promise<CodeList[]> {
    if (this.countries !== null && this.countries !== undefined) {
      return Promise.resolve(this.countries);
    } else {
      return this.APIService.getCountryList()
        .then(res => {
          this.countries = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  /* ============================= SELF-CONTAINED: codelists ========================*/

  getProcedureTypes(): Promise<CodeList[]> {
    if (this.procedureTypes !== null) {
      return Promise.resolve(this.procedureTypes);
    } else {
      return this.APIService.getProcedureType()
        .then(res => {
          this.procedureTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEoIDTypes(): Promise<CodeList[]> {
    if (this.eoIDType !== null && this.eoIDType !== undefined) {
      return Promise.resolve(this.eoIDType);
    } else {
      return this.APIService.get_eoIDTypes()
        .then(res => {
          this.eoIDType = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEvalutationMethodTypes(): Promise<CodeList[]> {
    if (this.evaluationMethodType != null) {
      return Promise.resolve(this.evaluationMethodType);
    } else {
      return this.APIService.get_EvaluationMethodType()
        .then(res => {
          this.evaluationMethodType = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getProjectTypes(): Promise<CodeList[]> {
    if (this.projectTypes != null) {
      return Promise.resolve(this.projectTypes);
    } else {
      return this.APIService.get_ProjectType()
        .then(res => {
          this.projectTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getBidTypes(): Promise<CodeList[]> {
    if (this.bidTypes != null) {
      return Promise.resolve(this.bidTypes);
    } else {
      return this.APIService.get_BidType()
        .then(res => {
          this.bidTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getFinancialRatioTypes(): Promise<CodeList[]> {
    if (this.financialRatioTypes != null) {
      return Promise.resolve(this.financialRatioTypes);
    } else {
      return this.APIService.get_financialRatioType()
        .then(res => {
          this.financialRatioTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getWeightingType(): Promise<CodeList[]> {
    if (this.weightingType != null) {
      return Promise.resolve(this.weightingType);
    } else {
      return this.APIService.get_WeightingType()
        .then(res => {
          this.weightingType = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }

  getEORoleTypes(): Promise<CodeList[]> {
    if (this.eoRoleTypes != null) {
      return Promise.resolve(this.eoRoleTypes);
    } else {
      return this.APIService.get_eoRoleType()
        .then(res => {
          this.eoRoleTypes = res;
          return Promise.resolve(res);
        })
        .catch(err => {
          console.log(err);
          const message: string = err.error +
            ' ' + err.message;
          const action = 'close';
          this.utilities.openSnackBar(message, action);
          return Promise.reject(err);
        });
    }
  }


}

