export default class AlertService {
    constructor(toastr) {
        "ngInject";
        this.toastr = toastr;
    }

    success(msg) {
        this.toastr.success(msg, 'Success');
    }

    error(msg) {
        this.toastr.error(msg, 'Error');
    }

    warning(msg) {
        this.toastr.warning(msg, 'Warning');
    }

    info(msg) {
        this.toastr.info(msg, 'Info');
    }

    static alertServiceFactory(toastr) {
        "ngInject";
        return new AlertService(toastr);
    }
}
