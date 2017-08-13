import angular from "angular";
import _ from "lodash";

export default function CashSplitSum($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-split-sums/:id';

    if (!CacheFactory.get('cashSplitSumCache')) {
        CacheFactory.createCache('cashSplitSumCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashSplitSumCache = CacheFactory.get('cashSplitSumCache');

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
            cache: cashSplitSumCache
        }
    });
}
