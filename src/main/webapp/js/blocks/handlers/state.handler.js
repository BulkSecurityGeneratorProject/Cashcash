export default function stateHandler($rootScope, $window, Auth, Principal, VERSION, $transitions) {
    "ngInject";

    function initialize() {
        $rootScope.VERSION = VERSION;

        $transitions.onStart({to: '**'}, function (transition) {
            $rootScope.toState = transition.to();
            $rootScope.toStateParams = transition.params("to");
            $rootScope.fromState = transition.from();

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }
        });
    }

    return {
        initialize: initialize
    };
};