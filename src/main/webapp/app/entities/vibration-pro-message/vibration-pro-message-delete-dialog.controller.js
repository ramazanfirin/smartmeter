(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationProMessageDeleteController',VibrationProMessageDeleteController);

    VibrationProMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'VibrationProMessage'];

    function VibrationProMessageDeleteController($uibModalInstance, entity, VibrationProMessage) {
        var vm = this;

        vm.vibrationProMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VibrationProMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
