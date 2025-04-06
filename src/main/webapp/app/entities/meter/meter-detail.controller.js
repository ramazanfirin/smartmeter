(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('MeterDetailController', MeterDetailController);

    MeterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meter'];

    function MeterDetailController($scope, $rootScope, $stateParams, previousState, entity, Meter) {
        var vm = this;

        vm.meter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smartmeterApp:meterUpdate', function(event, result) {
            vm.meter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
