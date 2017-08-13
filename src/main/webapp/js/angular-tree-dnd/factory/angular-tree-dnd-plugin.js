import angular from "angular";

export default function TreeDnDPlugin($injector) {
    "ngInject";
    var _fnget = function (name) {
        if (angular.isDefined($injector) && $injector.has(name)) {
            return $injector.get(name);
        }
        return null;
    };
    return _fnget;
};