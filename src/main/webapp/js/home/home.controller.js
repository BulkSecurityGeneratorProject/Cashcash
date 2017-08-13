export default function HomeController($scope, Principal, LoginService, $state) {
    "ngInject";
    var vm = this;

    function getAccount() {
        Principal.identity().then(function (account) {
            vm.account = account;
            vm.isAuthenticated = Principal.isAuthenticated;
        });
    }

    function register() {
        $state.go('register');
    }

    vm.account = null;
    vm.isAuthenticated = null;
    vm.login = LoginService.open;
    vm.register = register;
    $scope.$on('authenticationSuccess', function () {
        getAccount();
    });

    getAccount();
};
