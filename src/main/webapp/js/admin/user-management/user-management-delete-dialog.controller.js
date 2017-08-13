export default function UserManagementDeleteController($uibModalInstance, entity, User) {
    "ngInject";
    var vm = this;

    function clear() {
        $uibModalInstance.dismiss('cancel');
    }

    function confirmDelete(login) {
        User.delete({login: login},
            function () {
                $uibModalInstance.close(true);
            });
    }

    vm.user = entity;
    vm.clear = clear;
    vm.confirmDelete = confirmDelete;
};