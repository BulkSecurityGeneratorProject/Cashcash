export default function PasswordController(Auth, Principal) {
    "ngInject";
    var vm = this;

    function changePassword() {
        if (vm.password !== vm.confirmPassword) {
            vm.error = null;
            vm.success = null;
            vm.doNotMatch = 'ERROR';
        } else {
            vm.doNotMatch = null;
            Auth.changePassword(vm.password).then(function () {
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }

    vm.changePassword = changePassword;
    vm.doNotMatch = null;
    vm.error = null;
    vm.success = null;

    Principal.identity().then(function (account) {
        vm.account = account;
    });
};
