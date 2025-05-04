(function() {
    'use strict';

    angular
        .module('smartmeterApp')
        .controller('M2mMessageController', M2mMessageController);

    M2mMessageController.$inject = ['$state', 'DataUtils', 'M2mMessage', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Sensor', '$scope', '$window'];

    function M2mMessageController($state, DataUtils, M2mMessage, ParseLinks, AlertService, paginationConstants, pagingParams, Sensor, $scope, $window) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.sensors = [];
        vm.selectedSensor = '';
        vm.selectedTimeRange = '24';
        vm.loading = false;

        loadAll();

        function loadAll () {
            vm.loading = true;
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
                vm.loading = false;
            }
            function onError(error) {
                AlertService.error(error.data.message);
                vm.loading = false;
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
            vm.loading = false;
        }

        function onFilterError(error) {
            AlertService.error(error.data.message);
            vm.loading = false;
        }

        vm.filterMessages = function() {
            vm.loading = true;
            M2mMessage.filter({
                page: pagingParams.page - 1,
                size: 300,
                sensorId: vm.selectedSensor,
                timeRange: vm.selectedTimeRange
            }, onFilterSuccess, onFilterError);
        };

        function openFile(type, data) {
            // Type kontrolü
            if (!type) {
                type = 'image/jpeg'; // Varsayılan type
            }
            
            // Data kontrolü
            if (!data) {
                AlertService.error('Görüntü verisi bulunamadı');
                return;
            }
            
            // Base64 verisini temizle
            if (data.startsWith('data:')) {
                data = data.split(',')[1];
            }
            
            try {
                // Yeni pencere aç
                const win = $window.open('', '_blank', 'height=300,width=400');
                
                // HTML içeriğini oluştur
                const html = `
                    <html>
                        <head>
                            <title>Image Viewer</title>
                            <style>
                                body { 
                                    margin: 0; 
                                    display: flex; 
                                    justify-content: center; 
                                    align-items: center; 
                                    height: 100vh;
                                    background-color: #f5f5f5;
                                }
                                img { 
                                    max-width: 100%; 
                                    max-height: 100%; 
                                    object-fit: contain;
                                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                                }
                            </style>
                        </head>
                        <body>
                            <img src="data:${type};base64,${data}" alt="Image">
                        </body>
                    </html>
                `;
                
                // HTML içeriğini yaz
                win.document.write(html);
                win.document.close();
            } catch (error) {
                AlertService.error('Görüntü açılırken bir hata oluştu');
                console.error('Error opening file:', error);
            }
        }
    }
})();
