export default function SocialRegisterController($filter, $stateParams) {
    "ngInject";
    var vm = this;

    vm.success = $stateParams.success;
    vm.error = !vm.success;
    vm.provider = $stateParams.provider;
    vm.providerLabel = $filter('capitalize')(vm.provider);
    vm.success = $stateParams.success;
}