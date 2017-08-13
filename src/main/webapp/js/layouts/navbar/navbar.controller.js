export default function NavbarController($scope, $state, Auth, Principal, ProfileService, LoginService) {
    "ngInject";
    var vm = this;

    function collapseNavbar() {
        vm.isNavbarCollapsed = true;
    }

    function login() {
        collapseNavbar();
        LoginService.open();
    }

    function logout() {
        collapseNavbar();
        Auth.logout();
        $state.go('home');
    }

    function toggleNavbar() {
        vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
    }

    function getAccount() {
        Principal.identity().then(function (account) {
            vm.account = account;
            vm.isAuthenticated = Principal.isAuthenticated;
        });
    }

    vm.isNavbarCollapsed = true;

    ProfileService.getProfileInfo().then(function (response) {
        vm.inProduction = response.inProduction;
        vm.swaggerDisabled = response.swaggerDisabled;
    });

    vm.login = login;
    vm.logout = logout;
    vm.toggleNavbar = toggleNavbar;
    vm.collapseNavbar = collapseNavbar;
    vm.$state = $state;

    $scope.$on('authenticationSuccess', function () {
        getAccount();
    });

    getAccount();
};