export default function LoginService($uibModal) {
    "ngInject";

    function open() {
        if (modalInstance !== null) {
            return;
        }
        modalInstance = $uibModal.open({
            animation: true,
            template: require('./login.html'),
            controller: 'LoginController',
            controllerAs: 'vm'
        });
        modalInstance.result.then(
            resetModal,
            resetModal
        );
    }

    var service = {
        open: open
    };

    var modalInstance = null;
    var resetModal = function () {
        modalInstance = null;
    };

    return service;
};