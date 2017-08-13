import angular from "angular";

export default function RequestResetController($timeout, Auth) {
    "ngInject";
    var vm = this;

    function requestReset() {

        vm.error = null;
        vm.errorEmailNotExists = null;

        Auth.resetPasswordInit(vm.resetAccount.email).then(function () {
            vm.success = 'OK';
        }).catch(function (response) {
            vm.success = null;
            if (response.status === 400 && response.data === 'e-mail address not registered') {
                vm.errorEmailNotExists = 'ERROR';
            } else {
                vm.error = 'ERROR';
            }
        });
    }

    vm.error = null;
    vm.errorEmailNotExists = null;
    vm.requestReset = requestReset;
    vm.resetAccount = {};
    vm.success = null;

    $timeout(function () {
        angular.element('#email').focus();
    });
};