import angular from "angular";
import _ from "lodash";

export default function CashAccount($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-accounts/:id';

    if (!CacheFactory.get('cashAccountCache')) {
        CacheFactory.createCache('cashAccountCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashAccountCache = CacheFactory.get('cashAccountCache');

    function invalidateCache(data) {
        if(cashAccountCache){
            cashAccountCache.removeAll();
        }
        return data;
    }

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
            cache: cashAccountCache
        },
        'get': {
            method: 'GET',
            transformResponse: transformResponse,
            cache: cashAccountCache
        },
        'update': {
            method: 'PUT',
            isArray: true,
            transformResponse: invalidateCache
        },
        'save': {
            method: 'POST',
            transformResponse: invalidateCache
        }
    });
};
