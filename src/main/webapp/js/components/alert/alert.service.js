export default class AlertService {
    constructor($timeout, $sce) {
        "ngInject";
        this.$timeout = $timeout;
        this.$sce = $sce;
        this.toast = false;
        this.alertId = 0; // unique id for each alert. Starts from 0.
        this.alerts = [];
        this.timeout = 5000; // default timeout
    }

    showAsToast(isToast) {
        this.toast = isToast;
    }

    closeAlertByIndex(index, thisAlerts) {
        return thisAlerts.splice(index, 1);
    }


    closeAlert(id, extAlerts) {
        var thisAlerts = extAlerts ? extAlerts : this.alerts;
        return this.closeAlertByIndex(thisAlerts.map(function (e) {
            return e.id;
        }).indexOf(id), thisAlerts);
    }

    isToast() {
        return this.toast;
    }

    clear() {
        this.alerts = [];
    }

    get() {
        return this.alerts;
    }

    addAlert(alertOptions, extAlerts) {
        alertOptions.alertId = this.alertId++;
        var alert = this.factory(alertOptions);
        if (alertOptions.timeout && alertOptions.timeout > 0) {
            this.$timeout(function () {
                this.closeAlert(alertOptions.alertId, extAlerts);
            }.bind(this), alertOptions.timeout);
        }
        return alert;
    }

    add(alertOptions, extAlerts) {
        return this.addAlert(alertOptions, extAlerts);
    }

    success(msg, params, position) {
        return this.addAlert({
            type: 'success',
            msg: msg,
            params: params,
            timeout: this.timeout,
            toast: this.toast,
            position: position
        });
    }

    error(msg, params, position) {
        return this.addAlert({
            type: 'danger',
            msg: msg,
            params: params,
            timeout: this.timeout,
            toast: this.toast,
            position: position
        });
    }

    warning(msg, params, position) {
        return this.addAlert({
            type: 'warning',
            msg: msg,
            params: params,
            timeout: this.timeout,
            toast: this.toast,
            position: position
        });
    }

    info(msg, params, position) {
        return this.addAlert({
            type: 'info',
            msg: msg,
            params: params,
            timeout: this.timeout,
            toast: this.toast,
            position: position
        });
    }

    factory(alertOptions) {
        var alert = {
            type: alertOptions.type,
            msg: this.$sce.trustAsHtml(alertOptions.msg),
            id: alertOptions.alertId,
            timeout: alertOptions.timeout,
            toast: alertOptions.toast,
            position: alertOptions.position ? alertOptions.position : 'top right',
            scoped: alertOptions.scoped,
            close: function (alerts) {
                return this.closeAlert(this.id, alerts);
            }.bind(this)
        };
        if (!alert.scoped) {
            this.alerts.push(alert);
        }
        return alert;
    }

    static alertServiceFactory($timeout, $sce) {
        "ngInject";
        return new AlertService($timeout, $sce);
    }
}
