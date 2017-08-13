export default function UserManagementDetailController($stateParams, User) {
    "ngInject";
    var vm = this;

    function load(login) {
        User.get({login: login}, function (result) {
            vm.user = result;
        });
    }

    vm.load = load;
    vm.user = {};

    vm.load($stateParams.login);
};