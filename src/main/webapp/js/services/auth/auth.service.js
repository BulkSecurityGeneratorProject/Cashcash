import angular from "angular";

export default function Auth($rootScope, $state, $sessionStorage, $q, Principal, AuthServerProvider, Account, LoginService, Register, Activate, Password, PasswordResetInit, PasswordResetFinish) {
    "ngInject";

    function activateAccount(key, callback) {
        var cb = callback || angular.noop;

        return Activate.get(key,
            function (response) {
                return cb(response);
            },
            function (err) {
                return cb(err);
            }.bind(this)).$promise;
    }

    function getPreviousState() {
        var previousState = $sessionStorage.previousState;
        return previousState;
    }


    function resetPreviousState() {
        delete $sessionStorage.previousState;
    }


    function storePreviousState(previousStateName, previousStateParams) {
        var previousState = {"name": previousStateName, "params": previousStateParams};
        $sessionStorage.previousState = previousState;
    }

    function authorize(force) {
        function authThen() {
            var isAuthenticated = Principal.isAuthenticated();

            // an authenticated user can't access to login and register pages
            if (isAuthenticated && $rootScope.toState.parent === 'account' && ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register' || $rootScope.toState.name === 'social-auth')) {
                $state.go('home');
            }

            // recover and clear previousState after external login redirect (e.g. oauth2)
            if (isAuthenticated && !$rootScope.fromState.name && getPreviousState()) {
                var previousState = getPreviousState();
                resetPreviousState();
                $state.go(previousState.name, previousState.params);
            }

            if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
                if (isAuthenticated) {
                    // user is signed in but not authorized for desired state
                    $state.go('accessdenied');
                } else {
                    // user is not authenticated. stow the state they wanted before you
                    // send them to the login service, so you can return them when you're done
                    storePreviousState($rootScope.toState.name, $rootScope.toStateParams);

                    // now, send them to the signin state so they can log in
                    $state.go('accessdenied').then(function () {
                        LoginService.open();
                    });
                }
            }
        }

        var authReturn = Principal.identity(force).then(authThen);

        return authReturn;
    }

    function changePassword(newPassword, callback) {
        var cb = callback || angular.noop;

        return Password.save(newPassword, function () {
            return cb();
        }, function (err) {
            return cb(err);
        }).$promise;
    }

    function logout() {
        AuthServerProvider.logout();
        Principal.authenticate(null);
    }

    function createAccount(account, callback) {
        var cb = callback || angular.noop;

        return Register.save(account,
            function () {
                return cb(account);
            },
            function (err) {
                logout();
                return cb(err);
            }.bind(this)).$promise;
    }

    function login(credentials, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        function loginThen(data) {
            Principal.identity(true).then(function (account) {
                deferred.resolve(data);
            });
            return cb();
        }

        AuthServerProvider.login(credentials)
            .then(loginThen)
            .catch(function (err) {
                logout();
                deferred.reject(err);
                console.log(err);
                return cb(err);
            }.bind(this));

        return deferred.promise;
    }

    function loginWithToken(jwt, rememberMe) {
        return AuthServerProvider.loginWithToken(jwt, rememberMe);
    }

    function resetPasswordFinish(keyAndPassword, callback) {
        var cb = callback || angular.noop;

        return PasswordResetFinish.save(keyAndPassword, function () {
            return cb();
        }, function (err) {
            return cb(err);
        }).$promise;
    }

    function resetPasswordInit(mail, callback) {
        var cb = callback || angular.noop;

        return PasswordResetInit.save(mail, function () {
            return cb();
        }, function (err) {
            return cb(err);
        }).$promise;
    }

    function updateAccount(account, callback) {
        var cb = callback || angular.noop;

        return Account.save(account,
            function () {
                return cb(account);
            },
            function (err) {
                return cb(err);
            }.bind(this)).$promise;
    }

    var service = {
        activateAccount: activateAccount,
        authorize: authorize,
        changePassword: changePassword,
        createAccount: createAccount,
        getPreviousState: getPreviousState,
        login: login,
        logout: logout,
        loginWithToken: loginWithToken,
        resetPasswordFinish: resetPasswordFinish,
        resetPasswordInit: resetPasswordInit,
        resetPreviousState: resetPreviousState,
        storePreviousState: storePreviousState,
        updateAccount: updateAccount
    };

    return service;
}
