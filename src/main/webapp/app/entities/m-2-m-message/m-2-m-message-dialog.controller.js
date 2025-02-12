(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageDialogController', M2mMessageDialogController);

    M2mMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'M2mMessage', 'Sensor'];

    function M2mMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, M2mMessage, Sensor) {
        var vm = this;

        vm.m2mMessage = entity;
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
            if (vm.m2mMessage.id !== null) {
                M2mMessage.update(vm.m2mMessage, onSaveSuccess, onSaveError);
            } else {
                M2mMessage.save(vm.m2mMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:m2mMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        vm.setImage = function ($file, m2mMessage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        m2mMessage.image = base64Data;
                        m2mMessage.imageContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
