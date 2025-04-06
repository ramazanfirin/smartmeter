(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('MeterDeleteController',MeterDeleteController);

    MeterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Meter'];

    function MeterDeleteController($uibModalInstance, entity, Meter) {
        var vm = this;

        vm.meter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Meter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
