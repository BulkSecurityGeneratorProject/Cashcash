import angular from "angular";
import _ from "lodash";

export default function CashRate($resource, DateUtils, CacheFactory) {
    "ngInject";
    const resourceUrl = 'api/cash-rates/:code';

    if (!CacheFactory.get('cashRateCache')) {
        CacheFactory.createCache('cashRateCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashRateCache = CacheFactory.get('cashRateCache');

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
        'get': {
            method: 'GET',
            transformResponse: transformResponse,
            cache: cashRateCache
        }
    });
};
