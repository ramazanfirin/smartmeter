(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationProMessageDialogController', VibrationProMessageDialogController);

    VibrationProMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VibrationProMessage', 'LorawanMessage'];

    function VibrationProMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VibrationProMessage, LorawanMessage) {
        var vm = this;

        vm.vibrationProMessage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lorawanmessages = LorawanMessage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vibrationProMessage.id !== null) {
                VibrationProMessage.update(vm.vibrationProMessage, onSaveSuccess, onSaveError);
            } else {
                VibrationProMessage.save(vm.vibrationProMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:vibrationProMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
