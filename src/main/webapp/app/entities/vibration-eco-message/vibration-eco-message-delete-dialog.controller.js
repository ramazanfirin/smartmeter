(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationEcoMessageDeleteController',VibrationEcoMessageDeleteController);

    VibrationEcoMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'VibrationEcoMessage'];

    function VibrationEcoMessageDeleteController($uibModalInstance, entity, VibrationEcoMessage) {
        var vm = this;

        vm.vibrationEcoMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VibrationEcoMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
