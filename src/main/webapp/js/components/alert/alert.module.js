import modules from "../../modules";
import jhiAlertComponent from "./alert.component";
import AlertService from "./alert.service";
import jhiAlertError from "./alert-error.component";

export default modules
    .get('cashcash.components.alert', [])
    .component('jhiAlert', jhiAlertComponent)
    .factory('AlertService', AlertService.alertServiceFactory)
    .component('jhiAlertError', jhiAlertError)
    .name;

