(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('CurrentMeterMessageDialogController', CurrentMeterMessageDialogController);

    CurrentMeterMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CurrentMeterMessage', 'Sensor'];

    function CurrentMeterMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CurrentMeterMessage, Sensor) {
        var vm = this;

        vm.currentMeterMessage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sensors = Sensor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.currentMeterMessage.id !== null) {
                CurrentMeterMessage.update(vm.currentMeterMessage, onSaveSuccess, onSaveError);
            } else {
                CurrentMeterMessage.save(vm.currentMeterMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:currentMeterMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
