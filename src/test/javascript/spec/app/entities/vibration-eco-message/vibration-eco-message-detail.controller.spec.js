'use strict';

describe('Controller Tests', function() {

    describe('VibrationEcoMessage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVibrationEcoMessage, MockSensor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVibrationEcoMessage = jasmine.createSpy('MockVibrationEcoMessage');
            MockSensor = jasmine.createSpy('MockSensor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VibrationEcoMessage': MockVibrationEcoMessage,
                'Sensor': MockSensor
            };
            createController = function() {
                $injector.get('$controller')("VibrationEcoMessageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartmeterApp:vibrationEcoMessageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
