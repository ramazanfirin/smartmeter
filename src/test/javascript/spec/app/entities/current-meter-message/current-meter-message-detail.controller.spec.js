'use strict';

describe('Controller Tests', function() {

    describe('CurrentMeterMessage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCurrentMeterMessage, MockLorawanMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCurrentMeterMessage = jasmine.createSpy('MockCurrentMeterMessage');
            MockLorawanMessage = jasmine.createSpy('MockLorawanMessage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CurrentMeterMessage': MockCurrentMeterMessage,
                'LorawanMessage': MockLorawanMessage
            };
            createController = function() {
                $injector.get('$controller')("CurrentMeterMessageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartmeterApp:currentMeterMessageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
