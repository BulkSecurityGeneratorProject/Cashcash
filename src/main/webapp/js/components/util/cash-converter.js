export default class CashConverter {
    constructor(CacheFactory) {
        "ngInject";
        this.CacheFactory = CacheFactory;
    }

    getAmount(cashSplit, expectedCurrencyCode) {
        const currency = cashSplit.currency;

        if (currency.code === expectedCurrencyCode) {
            return cashSplit.amount;
        } else {
            const convertionRate = this.getConvertionRate(expectedCurrencyCode, currency.code);
            return cashSplit.amount * convertionRate;
        }
    }

    getConvertionRate(expectedCurrencyCode, actualCurrencyCode) {
        let cashRateCache = this.CacheFactory.get('cashRateCache');
        let cashRateList = JSON.parse(cashRateCache.get('api/cash-rates/' + expectedCurrencyCode)[1]);

        const actualToExpectedRate = cashRateList.rates[actualCurrencyCode];
        if (!actualToExpectedRate) {
            return 1;
        }
        return 1 / actualToExpectedRate;
    }
}