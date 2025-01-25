(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('SensorDetailController', SensorDetailController);

    SensorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Sensor'];

    function SensorDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Sensor) {
        var vm = this;

        vm.sensor = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('smartmeterApp:sensorUpdate', function(event, result) {
            vm.sensor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
