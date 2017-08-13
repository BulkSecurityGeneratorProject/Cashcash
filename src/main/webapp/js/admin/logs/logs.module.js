import modules from "../../modules";
import stateConfig from "./logs.state";
import LogsController from "./logs.controller";
import LogsService from "./logs.service";

export default modules
    .get('cashcash.admin.logs', [])
    .config(stateConfig)
    .controller('LogsController', LogsController)
    .factory('LogsService', LogsService)
    .name;

