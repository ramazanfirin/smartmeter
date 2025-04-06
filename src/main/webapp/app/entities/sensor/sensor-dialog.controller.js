(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('SensorDialogController', SensorDialogController);

    SensorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Sensor', 'Meter'];

    function SensorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Sensor, Meter) {
        var vm = this;

        vm.sensor = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.meters = Meter.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sensor.id !== null) {
                Sensor.update(vm.sensor, onSaveSuccess, onSaveError);
            } else {
                Sensor.save(vm.sensor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartmeterApp:sensorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastSeenDate = false;

        vm.setLastImage = function ($file, sensor) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        sensor.lastImage = base64Data;
                        sensor.lastImageContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
