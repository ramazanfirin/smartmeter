'use strict';

describe('Controller Tests', function() {

    describe('LorawanMessage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLorawanMessage, MockSensor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLorawanMessage = jasmine.createSpy('MockLorawanMessage');
            MockSensor = jasmine.createSpy('MockSensor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LorawanMessage': MockLorawanMessage,
                'Sensor': MockSensor
            };
            createController = function() {
                $injector.get('$controller')("LorawanMessageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartmeterApp:lorawanMessageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
