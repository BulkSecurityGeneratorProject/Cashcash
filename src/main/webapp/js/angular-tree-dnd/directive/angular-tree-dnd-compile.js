import angular from "angular";

export default function compile($compile) {
    "ngInject";
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            scope.$watch(
                attrs.compile, function (new_val) {
                    if (new_val) {
                        if (angular.isFunction(element.empty)) {
                            element.empty();
                        } else {
                            element.html('');
                        }

                        element.append($compile(new_val)(scope));
                    }
                }
            );
        }
    };
};