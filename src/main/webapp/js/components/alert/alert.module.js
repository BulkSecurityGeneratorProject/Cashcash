import modules from "../../modules";
import AlertService from "./alert.service";
import jhiAlertError from "./alert-error.component";

export default modules
    .get('cashcash.components.alert', [])
    .factory('AlertService', AlertService.alertServiceFactory)
    .component('jhiAlertError', jhiAlertError)
    .name;

