export default function LogsController(LogsService) {
    "ngInject";
    var vm = this;

    function changeLevel(name, level) {
        LogsService.changeLevel({name: name, level: level}, function () {
            vm.loggers = LogsService.findAll();
        });
    }

    vm.changeLevel = changeLevel;
    vm.loggers = LogsService.findAll();
};