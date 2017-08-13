export default function PasswordResetInit($resource) {
    "ngInject";
    var service = $resource('api/account/reset_password/init', {}, {});

    return service;
};
