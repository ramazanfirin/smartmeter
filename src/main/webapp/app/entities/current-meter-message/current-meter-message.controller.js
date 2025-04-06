(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('CurrentMeterMessageController', CurrentMeterMessageController);

    CurrentMeterMessageController.$inject = ['$state', 'CurrentMeterMessage', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Sensor', '$scope'];

    function CurrentMeterMessageController($state, CurrentMeterMessage, ParseLinks, AlertService, paginationConstants, pagingParams, Sensor, $scope) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.showGraph = showGraph;
        vm.drawGraph = drawGraph;
        vm.loadSensors = loadSensors;
        vm.isGraphVisible = false;
        vm.currentMeterMessages = [];
        vm.sensors = [];
        vm.selectedSensor = null;
        vm.selectedDateFilter = null;
        vm.dateFilters = [
            { id: 'all', name: 'Tümü' },
            { id: '24h', name: 'Son 24 Saat' },
            { id: '7d', name: 'Son 1 Hafta' },
            { id: '30d', name: 'Son 1 Ay' },
            { id: '365d', name: 'Son 1 Yıl' }
        ];

        vm.loadAll();
        vm.loadSensors();

        function loadAll() {
            CurrentMeterMessage.query({
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
                vm.currentMeterMessages = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadSensors() {
            Sensor.query(function(result) {
                vm.sensors = result;
            });
        }

        function search() {
            if (!vm.selectedDateFilter) {
                return;
            }

            var now = new Date();
            var startDate = new Date();
            var endDate = new Date();

            switch(vm.selectedDateFilter) {
                case '24h':
                    startDate.setHours(now.getHours() - 24);
                    break;
                case '7d':
                    startDate.setDate(now.getDate() - 7);
                    break;
                case '30d':
                    startDate.setDate(now.getDate() - 30);
                    break;
                case '365d':
                    startDate.setDate(now.getDate() - 365);
                    break;
                default:
                    startDate = null;
                    endDate = null;
            }

            CurrentMeterMessage.search({
                sensorId: vm.selectedSensor,
                startDate: startDate ? startDate.toISOString() : null,
                endDate: endDate ? endDate.toISOString() : null
            }, function(result) {
                vm.currentMeterMessages = result;
                vm.isGraphVisible = false;
            });
        }

        function clear() {
            vm.currentMeterMessages = [];
            vm.selectedSensor = null;
            vm.selectedDateFilter = null;
            vm.isGraphVisible = false;
            vm.loadAll();
        }

        function showGraph() {
            vm.isGraphVisible = true;
            $('#graphModal').modal('show');
            $scope.$applyAsync(function() {
                vm.drawGraph();
            });
        }

        function drawGraph() {
            if (!vm.currentMeterMessages || vm.currentMeterMessages.length === 0) {
                AlertService.error("Grafik için veri bulunamadı");
                return;
            }

            var ctx = document.getElementById('currentMeterGraph').getContext('2d');
            var labels = [];
            var currentData = [];
            var energyData = [];

            vm.currentMeterMessages.forEach(function(message) {
                if (message.loraMessage && message.loraMessage.insertDate) {
                    labels.push(new Date(message.loraMessage.insertDate).toLocaleString());
                    currentData.push(message.current);
                    energyData.push(message.totalEnergy);
                }
            });

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Akım',
                            data: currentData,
                            borderColor: 'rgb(75, 192, 192)',
                            tension: 0.1
                        },
                        {
                            label: 'Toplam Enerji',
                            data: energyData,
                            borderColor: 'rgb(255, 99, 132)',
                            tension: 0.1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
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
    }
})();
