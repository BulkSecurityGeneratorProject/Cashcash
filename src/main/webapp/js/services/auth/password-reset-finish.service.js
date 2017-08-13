export default function PasswordResetFinish($resource) {
    "ngInject";
    var service = $resource('api/account/reset_password/finish', {}, {});

    return service;
};
