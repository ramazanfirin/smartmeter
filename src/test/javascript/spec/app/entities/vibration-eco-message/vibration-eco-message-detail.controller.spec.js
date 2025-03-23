'use strict';

describe('Controller Tests', function() {

    describe('VibrationEcoMessage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVibrationEcoMessage, MockLorawanMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVibrationEcoMessage = jasmine.createSpy('MockVibrationEcoMessage');
            MockLorawanMessage = jasmine.createSpy('MockLorawanMessage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VibrationEcoMessage': MockVibrationEcoMessage,
                'LorawanMessage': MockLorawanMessage
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
