export default function UserManagementDialogController($uibModalInstance, entity, User) {
    "ngInject";
    var vm = this;

    function clear() {
        $uibModalInstance.dismiss('cancel');
    }

    function onSaveSuccess(result) {
        vm.isSaving = false;
        $uibModalInstance.close(result);
    }

    function onSaveError() {
        vm.isSaving = false;
    }

    function save() {
        vm.isSaving = true;
        if (vm.user.id !== null) {
            User.update(vm.user, onSaveSuccess, onSaveError);
        } else {
            vm.user.langKey = 'en';
            User.save(vm.user, onSaveSuccess, onSaveError);
        }
    }

    vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    vm.clear = clear;
    vm.languages = null;
    vm.save = save;
    vm.user = entity;
};
