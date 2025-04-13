(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('MeterDialogController', MeterDialogController);

    MeterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meter'];

    function MeterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meter) {
        var vm = this;

        vm.meter = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meter.id !== null) {
                Meter.update(vm.meter, onSaveSuccess, onSaveError);
            } else {
                Meter.save(vm.meter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:meterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
