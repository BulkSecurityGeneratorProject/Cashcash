import angular from "angular";
import _ from "lodash";

export default function CashSplitCumulative($resource, DateUtils, CacheFactory) {
    "ngInject";
    var resourceUrl = 'api/cash-split-cumulative/:id';

    if (!CacheFactory.get('cashSplitCumulativeCache')) {
        CacheFactory.createCache('cashSplitCumulativeCache', {
            deleteOnExpire: 'aggressive',
            recycleFreq: 60000
        });
    }

    const cashSplitCumulativeCache = CacheFactory.get('cashSplitCumulativeCache');

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
            cache: cashSplitCumulativeCache
        }
    });
};
