(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('LorawanMessageDeleteController',LorawanMessageDeleteController);

    LorawanMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'LorawanMessage'];

    function LorawanMessageDeleteController($uibModalInstance, entity, LorawanMessage) {
        var vm = this;

        vm.lorawanMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LorawanMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
