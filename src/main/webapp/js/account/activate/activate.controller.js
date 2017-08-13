export default function ActivationController($stateParams, Auth, LoginService) {
    "ngInject";
    var vm = this;

    Auth.activateAccount({key: $stateParams.key}).then(function () {
        vm.error = null;
        vm.success = 'OK';
    }).catch(function () {
        vm.success = null;
        vm.error = 'ERROR';
    });

    vm.login = LoginService.open;
};
