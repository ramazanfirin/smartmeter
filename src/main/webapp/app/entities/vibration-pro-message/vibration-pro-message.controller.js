(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationProMessageController', VibrationProMessageController);

    VibrationProMessageController.$inject = ['$state', 'VibrationProMessage', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Sensor'];

    function VibrationProMessageController($state, VibrationProMessage, ParseLinks, AlertService, paginationConstants, pagingParams, Sensor) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.search = search;
        
        // Arama için değişkenler
        vm.selectedSensor = '';
        vm.selectedDateFilter = '';
        vm.sensors = []; // Daha sonra doldurulacak
        vm.dateFilters = [
            { id: '', name: 'Tümü' },
            { id: '24h', name: 'Son 24 Saat' },
            { id: '7d', name: 'Son 1 Hafta' },
            { id: '30d', name: 'Son 1 Ay' },
            { id: '365d', name: 'Son 1 Yıl' }
        ];

        loadAll();
        loadSensors();

        function loadAll() {
            var params = {
                page: pagingParams.page - 1,
                size: vm.itemsPerPage
            };

            // Sıralama parametrelerini ekle
            params.sort = vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc');
            if (vm.predicate !== 'id') {
                params.sort = [params.sort, 'id'].join(',');
            }

            VibrationProMessage.query(params, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.vibrationProMessages = data;
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

        function search() {
            var params = {
                page: 0,
                size: vm.itemsPerPage
            };

            // Sıralama parametrelerini ekle
            params.sort = vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc');
            if (vm.predicate !== 'id') {
                params.sort = [params.sort, 'id'].join(',');
            }

            // Tarih filtresi ekle
            if (vm.selectedDateFilter) {
                var endDate = new Date();
                var startDate = new Date();
                
                switch(vm.selectedDateFilter) {
                    case '24h':
                        startDate.setHours(startDate.getHours() - 24);
                        break;
                    case '7d':
                        startDate.setDate(startDate.getDate() - 7);
                        break;
                    case '30d':
                        startDate.setDate(startDate.getDate() - 30);
                        break;
                    case '365d':
                        startDate.setDate(startDate.getDate() - 365);
                        break;
                }

                params.startDate = startDate.toISOString();
                params.endDate = endDate.toISOString();
            }

            // Sensor filtresi ekle
            if (vm.selectedSensor) {
                params.sensorId = vm.selectedSensor;
            }

            // Arama servisini çağır
            VibrationProMessage.search(params, onSearchSuccess, onSearchError);

            function onSearchSuccess(data) {
                vm.vibrationProMessages = data;
                vm.page = 1;
                vm.totalItems = data.length;
                vm.queryCount = data.length;
            }

            function onSearchError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadSensors() {
            Sensor.query({}, function(data) {
                vm.sensors = data;
            }, function(error) {
                AlertService.error(error.data.message);
            });
        }
    }
})();
