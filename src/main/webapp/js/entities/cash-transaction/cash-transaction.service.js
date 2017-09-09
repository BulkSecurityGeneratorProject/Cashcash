import angular from "angular";
import _ from "lodash";

export default function CashTransaction($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-transactions/:id';

    function invalidateCache(data) {
        const cashSplitSumCache = CacheFactory.get('cashSplitSumCache');
        if (cashSplitSumCache) {
            cashSplitSumCache.removeAll();
        }

        const cashSplitCumulativeCache = CacheFactory.get('cashSplitCumulativeCache');
        if (cashSplitCumulativeCache) {
            cashSplitCumulativeCache.removeAll();
        }

        const cashSplitCache = CacheFactory.get('cashSplitCache');
        if (cashSplitCache) {
            cashSplitCache.removeAll();
        }
        return data;
    }

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
            transformResponse: transformResponseList
        },
        'get': {
            method: 'GET',
            transformResponse: transformResponse
        },
        'update': {
            method: 'PUT',
            transformResponse: invalidateCache
        },
        'save': {
            method: 'POST',
            isArray: true,
            transformResponse: invalidateCache
        },
        'delete': {
            method: 'DELETE',
            transformResponse: invalidateCache
        }
    });
};