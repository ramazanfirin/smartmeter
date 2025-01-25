(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('LorawanMessageDialogController', LorawanMessageDialogController);

    LorawanMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'LorawanMessage', 'Sensor'];

    function LorawanMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, LorawanMessage, Sensor) {
        var vm = this;

        vm.lorawanMessage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.lorawanMessage.id !== null) {
                LorawanMessage.update(vm.lorawanMessage, onSaveSuccess, onSaveError);
            } else {
                LorawanMessage.save(vm.lorawanMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:lorawanMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        vm.setImage = function ($file, lorawanMessage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        lorawanMessage.image = base64Data;
                        lorawanMessage.imageContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
