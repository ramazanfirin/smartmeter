(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageController', M2mMessageController);

    M2mMessageController.$inject = ['$state', 'DataUtils', 'M2mMessage', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Sensor', '$scope'];

    function M2mMessageController($state, DataUtils, M2mMessage, ParseLinks, AlertService, paginationConstants, pagingParams, Sensor, $scope) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.sensors = [];
        vm.selectedSensor = '';
        vm.selectedTimeRange = '24';

        loadAll();

        function loadAll () {
            M2mMessage.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.m2mMessages = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.loadSensors = function() {
            Sensor.query(function(result) {
                vm.sensors = result.filter(function(sensor) {
                    return sensor.type === 'WATER_METER';
                });
            });
        };

        vm.loadSensors();

        function onFilterSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.m2mMessages = data;
            vm.page = pagingParams.page;
        }

        function onFilterError(error) {
            AlertService.error(error.data.message);
        }

        vm.filterMessages = function() {
            M2mMessage.filter({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sensorId: vm.selectedSensor,
                timeRange: vm.selectedTimeRange
            }, onFilterSuccess, onFilterError);
        };
    }
})();
