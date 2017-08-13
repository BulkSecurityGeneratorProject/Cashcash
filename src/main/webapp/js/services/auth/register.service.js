export default function Register($resource) {
    "ngInject";
    return $resource('api/register', {}, {});
};