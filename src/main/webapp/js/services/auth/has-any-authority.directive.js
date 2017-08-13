export default function hasAnyAuthority(Principal) {
    "ngInject";

    function linkFunc(scope, element, attrs) {
        var authorities = attrs.hasAnyAuthority.replace(/\s+/g, '').split(',');

        var setVisible = function () {
            element.removeClass('hidden');
        };
        var setHidden = function () {
            element.addClass('hidden');
        };
        var defineVisibility = function (reset) {
            var result;
            if (reset) {
                setVisible();
            }

            result = Principal.hasAnyAuthority(authorities);
            if (result) {
                setVisible();
            } else {
                setHidden();
            }
        };

        if (authorities.length > 0) {
            defineVisibility(true);

            scope.$watch(function () {
                return Principal.isAuthenticated();
            }, function () {
                defineVisibility(true);
            });
        }
    }

    var directive = {
        restrict: 'A',
        link: linkFunc
    };

    return directive;
};
