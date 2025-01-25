(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('LorawanMessageDetailController', LorawanMessageDetailController);

    LorawanMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'LorawanMessage', 'Sensor'];

    function LorawanMessageDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, LorawanMessage, Sensor) {
        var vm = this;

        vm.lorawanMessage = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('smartmeterApp:lorawanMessageUpdate', function(event, result) {
            vm.lorawanMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
