(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageDeleteController',M2mMessageDeleteController);

    M2mMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'M2mMessage'];

    function M2mMessageDeleteController($uibModalInstance, entity, M2mMessage) {
        var vm = this;

        vm.m2mMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            M2mMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
