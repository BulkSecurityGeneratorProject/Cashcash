import angular from "angular";

export default function ResetFinishController($stateParams, $timeout, Auth, LoginService) {
    "ngInject";
    var vm = this;

    function finishReset() {
        vm.doNotMatch = null;
        vm.error = null;
        if (vm.resetAccount.password !== vm.confirmPassword) {
            vm.doNotMatch = 'ERROR';
        } else {
            Auth.resetPasswordFinish({key: $stateParams.key, newPassword: vm.resetAccount.password}).then(function () {
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }

    vm.keyMissing = angular.isUndefined($stateParams.key);
    vm.confirmPassword = null;
    vm.doNotMatch = null;
    vm.error = null;
    vm.finishReset = finishReset;
    vm.login = LoginService.open;
    vm.resetAccount = {};
    vm.success = null;

    $timeout(function () {
        angular.element('#password').focus();
    });
};