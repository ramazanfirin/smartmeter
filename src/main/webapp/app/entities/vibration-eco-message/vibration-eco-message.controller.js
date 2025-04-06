(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('VibrationEcoMessageController', VibrationEcoMessageController);

    VibrationEcoMessageController.$inject = ['$state', 'VibrationEcoMessage', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Sensor', '$scope'];

    function VibrationEcoMessageController($state, VibrationEcoMessage, ParseLinks, AlertService, paginationConstants, pagingParams, Sensor, $scope) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.showGraph = showGraph;
        vm.isGraphVisible = false;
        
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

            VibrationEcoMessage.query(params, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.vibrationEcoMessages = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadSensors() {
            Sensor.query({type: 'VIBRATION_ECO'}, function(data) {
                vm.sensors = data;
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
            VibrationEcoMessage.search(params, onSearchSuccess, onSearchError);

            function onSearchSuccess(data) {
                vm.vibrationEcoMessages = data;
                vm.page = 1;
                vm.totalItems = data.length;
                vm.queryCount = data.length;
            }

            function onSearchError(error) {
                AlertService.error(error.data.message);
            }
        }

        function showGraph() {
            drawGraph();
            $('#graphModal').modal('show');
        }

        function drawGraph() {
            if (vm.chart) {
                vm.chart.destroy();
            }

            var canvas = document.getElementById('vibrationGraph');
            if (!canvas) {
                console.error('Canvas element not found');
                return;
            }

            var ctx = canvas.getContext('2d');
            
            // Verileri hazırla
            var labels = vm.vibrationEcoMessages.map(function(item) {
                return item.loraMessage.insertDate;
            });
            
            var xAxisData = vm.vibrationEcoMessages.map(function(item) {
                return item.xAxisValue;
            });
            
            var yAxisData = vm.vibrationEcoMessages.map(function(item) {
                return item.yAxisValue;
            });
            
            var zAxisData = vm.vibrationEcoMessages.map(function(item) {
                return item.zAxisValue;
            });

            // Grafik oluştur
            vm.chart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'X Ekseni',
                            data: xAxisData,
                            borderColor: 'rgb(255, 99, 132)',
                            tension: 0.1
                        },
                        {
                            label: 'Y Ekseni',
                            data: yAxisData,
                            borderColor: 'rgb(54, 162, 235)',
                            tension: 0.1
                        },
                        {
                            label: 'Z Ekseni',
                            data: zAxisData,
                            borderColor: 'rgb(75, 192, 192)',
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
    }
})();