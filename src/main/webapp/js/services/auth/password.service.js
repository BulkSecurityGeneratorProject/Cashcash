export default function Password($resource) {
    "ngInject";
    var service = $resource('api/account/change_password', {}, {});

    return service;
};
