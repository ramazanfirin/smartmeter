(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('CurrentMeterMessageDeleteController',CurrentMeterMessageDeleteController);

    CurrentMeterMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'CurrentMeterMessage'];

    function CurrentMeterMessageDeleteController($uibModalInstance, entity, CurrentMeterMessage) {
        var vm = this;

        vm.currentMeterMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CurrentMeterMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
