(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationEcoMessageDialogController', VibrationEcoMessageDialogController);

    VibrationEcoMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VibrationEcoMessage', 'LorawanMessage'];

    function VibrationEcoMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VibrationEcoMessage, LorawanMessage) {
        var vm = this;

        vm.vibrationEcoMessage = entity;
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
            if (vm.vibrationEcoMessage.id !== null) {
                VibrationEcoMessage.update(vm.vibrationEcoMessage, onSaveSuccess, onSaveError);
            } else {
                VibrationEcoMessage.save(vm.vibrationEcoMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:vibrationEcoMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
