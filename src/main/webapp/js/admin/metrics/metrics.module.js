import modules from "../../modules";
import stateConfig from "./metrics.state";
import JhiMetricsMonitoringController from "./metrics.controller";
import JhiMetricsMonitoringModalController from "./metrics.modal.controller";
import JhiMetricsService from "./metrics.service";

export default modules
    .get('cashcash.admin.metrics', [])
    .config(stateConfig)
    .controller('JhiMetricsMonitoringController', JhiMetricsMonitoringController)
    .controller('JhiMetricsMonitoringModalController', JhiMetricsMonitoringModalController)
    .factory('JhiMetricsService', JhiMetricsService)
    .name;

