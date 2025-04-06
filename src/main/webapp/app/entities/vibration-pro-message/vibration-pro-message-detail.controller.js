(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationProMessageDetailController', VibrationProMessageDetailController);

    VibrationProMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VibrationProMessage', 'LorawanMessage'];

    function VibrationProMessageDetailController($scope, $rootScope, $stateParams, previousState, entity, VibrationProMessage, LorawanMessage) {
        var vm = this;

        vm.vibrationProMessage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smartmeterApp:vibrationProMessageUpdate', function(event, result) {
            vm.vibrationProMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
