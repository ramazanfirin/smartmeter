(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('CurrentMeterMessageDetailController', CurrentMeterMessageDetailController);

    CurrentMeterMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CurrentMeterMessage', 'Sensor'];

    function CurrentMeterMessageDetailController($scope, $rootScope, $stateParams, previousState, entity, CurrentMeterMessage, Sensor) {
        var vm = this;

        vm.currentMeterMessage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smartmeterApp:currentMeterMessageUpdate', function(event, result) {
            vm.currentMeterMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
