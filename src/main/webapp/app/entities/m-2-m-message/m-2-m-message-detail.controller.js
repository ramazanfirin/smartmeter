(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageDetailController', M2mMessageDetailController);

    M2mMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'M2mMessage', 'Sensor'];

    function M2mMessageDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, M2mMessage, Sensor) {
        var vm = this;

        vm.m2mMessage = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('smartmeterApp:m2mMessageUpdate', function(event, result) {
            vm.m2mMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
