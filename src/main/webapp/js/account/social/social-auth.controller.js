export default function SocialAuthController($state, $cookies, Auth) {
    "ngInject";
    var token = $cookies.get('social-authentication');

    Auth.loginWithToken(token, false).then(function () {
        $cookies.remove('social-authentication');
        Auth.authorize(true);
    }, function () {
        $state.go('social-register', {'success': 'false'});
    });
}