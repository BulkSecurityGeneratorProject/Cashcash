export default function JhiConfigurationController(JhiConfigurationService) {
    "ngInject";
    var vm = this;

    vm.allConfiguration = null;
    vm.configuration = null;

    JhiConfigurationService.get().then(function (configuration) {
        vm.configuration = configuration;
    });
    JhiConfigurationService.getEnv().then(function (configuration) {
        vm.allConfiguration = configuration;
    });
};