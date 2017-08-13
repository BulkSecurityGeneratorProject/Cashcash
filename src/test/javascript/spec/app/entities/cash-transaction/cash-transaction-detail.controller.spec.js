'use strict';

describe('Controller Tests', function () {

    describe('CashTransaction Management Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockCashTransaction, MockUser, MockCashSplit;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCashTransaction = jasmine.createSpy('MockCashTransaction');
            MockUser = jasmine.createSpy('MockUser');
            MockCashSplit = jasmine.createSpy('MockCashSplit');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'CashTransaction': MockCashTransaction,
                'User': MockUser,
                'CashSplit': MockCashSplit
            };
            createController = function () {
                $injector.get('$controller')("CashTransactionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'cashcashApp:cashTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
