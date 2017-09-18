import _ from "lodash";

export default class CashTransactionUtils {

    constructor() {
        "ngInject";
    }


    getPositiveSplit(cashTransaction) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount > 0;
        });
    };

    getPositiveSplitWithThisAccount(cashTransaction, expectedAccountId) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount > 0 && split.account.id === expectedAccountId;
        });
    };

    getPositiveSplitWithoutThisAccount(cashTransaction, refusedAccountId) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount > 0 && split.account.id !== refusedAccountId;
        });
    };

    getNegativeSplit(cashTransaction) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount < 0;
        });
    };

    getNegativeSplitWithThisAccount(cashTransaction, expectedAccountId) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount < 0 && split.account.id === expectedAccountId;
        });
    };

    getNegativeSplitWithoutThisAccount(cashTransaction, refusedAccountId) {
        return _.find(cashTransaction.splits, function (split) {
            return split.amount < 0 && split.account.id !== refusedAccountId;
        });
    };

    /**
     * The exchangeAccount is the one that belongs to two of the splits
     * @param cashTransaction
     * @returns int
     */
    getExchangeAccount(cashTransaction) {
        const account = _.map(cashTransaction.splits, 'account');

        const exchangeIds = _.filter(account, function (x, i, array) {
            return _.includes(array, x, i + 1)
        });

        return exchangeIds[0];
    };

    flattenTransactionInfo(cashTransaction) {
        let outSplit;
        let inSplit;
        if (cashTransaction.splits.length === 2) {
            // Basic transaction
            inSplit = this.getPositiveSplit(cashTransaction);
            outSplit = this.getNegativeSplit(cashTransaction);

            cashTransaction.outAccount = outSplit.account;
            if (outSplit.account) {
                cashTransaction.outAccountId = outSplit.account.id;
            }
            cashTransaction.inAccount = inSplit.account;
            if (inSplit.account) {
                cashTransaction.inAccountId = inSplit.account.id;
            }
            cashTransaction.amount = inSplit.amount;
            cashTransaction.currency = inSplit.currency;
            if (inSplit.currency) {
                cashTransaction.currencyId = inSplit.currency.id;
            }
            cashTransaction.convertedAmount = null;
            cashTransaction.convertedCurrency = null;
            cashTransaction.exchangeAccount = null;
            cashTransaction.inSplitId = inSplit.id;
            cashTransaction.outSplitId = outSplit.id;
            cashTransaction.outToExchangeSplitId = null;
            cashTransaction.exchangeToInSplitId = null;

        } else if (cashTransaction.splits.length === 4) {
            // Exchange transaction
            const exchangeAccount = this.getExchangeAccount(cashTransaction);
            inSplit = this.getPositiveSplitWithoutThisAccount(cashTransaction, exchangeAccount.id);
            outSplit = this.getNegativeSplitWithoutThisAccount(cashTransaction, exchangeAccount.id);
            const outToExchangeSplit = this.getPositiveSplitWithThisAccount(cashTransaction, exchangeAccount.id);
            const exchangeToInSplit = this.getNegativeSplitWithThisAccount(cashTransaction, exchangeAccount.id);

            cashTransaction.outAccount = outSplit.account;
            if (outSplit.account) {
                cashTransaction.outAccountId = outSplit.account.id;
            }
            cashTransaction.inAccount = inSplit.account;
            if (inSplit.account) {
                cashTransaction.inAccountId = inSplit.account.id;
            }
            cashTransaction.amount = -outSplit.amount;
            cashTransaction.currency = outSplit.currency;
            if (outSplit.currency) {
                cashTransaction.currencyId = outSplit.currency.id;
            }
            cashTransaction.convertedAmount = inSplit.amount;
            cashTransaction.convertedCurrency = inSplit.currency;
            if (inSplit.currency) {
                cashTransaction.convertedCurrencyId = inSplit.currency.id;
            }
            cashTransaction.exchangeAccount = exchangeAccount;
            cashTransaction.inSplitId = inSplit.id;
            cashTransaction.outSplitId = outSplit.id;
            cashTransaction.outToExchangeSplitId = outToExchangeSplit.id;
            cashTransaction.exchangeToInSplitId = exchangeToInSplit.id;
        }
    };

    createSplits(cashTransaction, cashAccounts, cashCurrencies) {
        let inSplit;
        let outSplit;
        let currency = _.find(cashCurrencies, {'id': +cashTransaction.currencyId});
        if (cashTransaction.multicurrency) {
            // Exchange transaction
            // Basic transaction
            outSplit = {
                id: cashTransaction.outSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.outAccountId}),
                amount: -cashTransaction.amount,
                currency: currency

            };
            const outToExchangeSplit = {
                id: cashTransaction.outToExchangeSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.exchangeAccountId}),
                amount: cashTransaction.amount,
                currency: currency

            };
            let convertedCurrency = _.find(cashCurrencies, {'id': +cashTransaction.convertedCurrencyId});
            const exchangeToInSplit = {
                id: cashTransaction.exchangeToInSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.exchangeAccountId}),
                amount: -cashTransaction.convertedAmount,
                currency: convertedCurrency

            };
            inSplit = {
                id: cashTransaction.inSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.inAccountId}),
                amount: cashTransaction.convertedAmount,
                currency: convertedCurrency
            };
            cashTransaction.splits = [outSplit, outToExchangeSplit, exchangeToInSplit, inSplit]
        } else {
            // Basic transaction
            outSplit = {
                id: cashTransaction.outSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.outAccountId}),
                amount: -cashTransaction.amount,
                currency: currency

            };
            inSplit = {
                id: cashTransaction.inSplitId,
                user: cashTransaction.user,
                account: _.find(cashAccounts, {'id': +cashTransaction.inAccountId}),
                amount: cashTransaction.amount,
                currency: currency
            };
            cashTransaction.splits = [outSplit, inSplit]
        }
    };
};