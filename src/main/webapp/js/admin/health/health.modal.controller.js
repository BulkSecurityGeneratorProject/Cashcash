export default function HealthModalController($uibModalInstance, currentHealth, baseName, subSystemName) {
    "ngInject";
    var vm = this;

    function cancel() {
        $uibModalInstance.dismiss('cancel');
    }

    vm.cancel = cancel;
    vm.currentHealth = currentHealth;
    vm.baseName = baseName;
    vm.subSystemName = subSystemName;
};
