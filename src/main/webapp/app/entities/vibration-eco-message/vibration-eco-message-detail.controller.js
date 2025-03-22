(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationEcoMessageDetailController', VibrationEcoMessageDetailController);

    VibrationEcoMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VibrationEcoMessage', 'Sensor'];

    function VibrationEcoMessageDetailController($scope, $rootScope, $stateParams, previousState, entity, VibrationEcoMessage, Sensor) {
        var vm = this;

        vm.vibrationEcoMessage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smartmeterApp:vibrationEcoMessageUpdate', function(event, result) {
            vm.vibrationEcoMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
