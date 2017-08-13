import angular from "angular";
import _ from "lodash";

export default function CashSplit($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-splits/:id';

    if (!CacheFactory.get('cashSplitCache')) {
        CacheFactory.createCache('cashSplitCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashSplitCache = CacheFactory.get('cashSplitCache');

    function transformResponse(data) {
        if (data) {
            data = angular.fromJson(data);
            data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
            data.modifiedDate = DateUtils.convertDateTimeFromServer(data.modifiedDate);
            data.transactionDate = DateUtils.convertDateTimeFromServer(data.transactionDate);
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
            cache: cashSplitCache
        },
        'get': {
            method: 'GET',
            transformResponse: transformResponse
        }
    });
};
