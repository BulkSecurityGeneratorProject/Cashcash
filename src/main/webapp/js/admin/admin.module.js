import modules from "../modules";
import stateConfig from "./admin.state";
import userManagement from "./user-management/userManagement.module";
import metrics from "./metrics/metrics.module";
import logs from "./logs/logs.module";
import health from "./health/health.module";
import docs from "./docs/docs.module";
import configuration from "./configuration/configuration.module";
import audits from "./audits/audits.module";

export default modules
    .get('cashcash.admin', [
        userManagement,
        metrics,
        logs,
        health,
        docs,
        configuration,
        audits
    ])
    .config(stateConfig)
    .name;

