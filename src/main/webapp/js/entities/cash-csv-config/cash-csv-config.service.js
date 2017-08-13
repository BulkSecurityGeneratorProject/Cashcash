import angular from "angular";
import _ from "lodash";

export default function CashCsvConfig($resource, DateUtils) {
    "ngInject";
    var resourceUrl = 'api/cash-csv-configs/:id';

    function transformResponse(data) {
        if (data) {
            data = angular.fromJson(data);
            data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
            data.modifiedDate = DateUtils.convertDateTimeFromServer(data.modifiedDate);
        }
        return data;
    }

    function transformResponseList(data) {
        if (data) {
            data = angular.fromJson(data);

            _.forEach(data, function (item) {
                transformResponse(item);
            });
        }
        return data;
    }

    return $resource(resourceUrl, {}, {
        'query': {
            method: 'GET',
            isArray: true,
            transformResponse: transformResponseList
        },
        'get': {
            method: 'GET',
            transformResponse: transformResponse
        },
        'update': {method: 'PUT'},
        'save': {method: 'POST'}
    });
};
