import angular from "angular";
import _ from "lodash";

export default function CashCurrency($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-currencies/:id';

    if (!CacheFactory.get('cashCurrencyCache')) {
        CacheFactory.createCache('cashCurrencyCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashCurrencyCache = CacheFactory.get('cashCurrencyCache');

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
            transformResponse: transformResponseList,
            cache: cashCurrencyCache
        },
        'get': {
            method: 'GET',
            transformResponse: transformResponse,
            cache: cashCurrencyCache
        }
    });
};
