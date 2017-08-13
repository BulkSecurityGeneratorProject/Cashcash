import angular from "angular";

export default function RegisterController($timeout, Auth, LoginService) {
    "ngInject";
    var vm = this;

    function register() {
        if (vm.registerAccount.password !== vm.confirmPassword) {
            vm.doNotMatch = 'ERROR';
        } else {
            vm.registerAccount.langKey = 'en';
            vm.doNotMatch = null;
            vm.error = null;
            vm.errorUserExists = null;
            vm.errorEmailExists = null;

            Auth.createAccount(vm.registerAccount).then(function () {
                vm.success = 'OK';
            }).catch(function (response) {
                vm.success = null;
                if (response.status === 400 && response.data === 'login already in use') {
                    vm.errorUserExists = 'ERROR';
                } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                    vm.errorEmailExists = 'ERROR';
                } else {
                    vm.error = 'ERROR';
                }
            });
        }
    }

    vm.doNotMatch = null;
    vm.error = null;
    vm.errorUserExists = null;
    vm.login = LoginService.open;
    vm.register = register;
    vm.registerAccount = {};
    vm.success = null;

    $timeout(function () {
        angular.element('#login').focus();
    });
};