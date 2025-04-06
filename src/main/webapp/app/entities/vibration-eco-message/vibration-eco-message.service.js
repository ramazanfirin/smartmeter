(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('VibrationEcoMessage', VibrationEcoMessage);

    VibrationEcoMessage.$inject = ['$resource', 'DateUtils'];

    function VibrationEcoMessage ($resource, DateUtils) {
        var resourceUrl =  'api/vibration-eco-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.loraMessage.insertDate = DateUtils.convertDateTimeFromServer(data.loraMessage.insertDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'search': {
                method: 'GET',
                url: 'api/vibration-eco-messages/search',
                isArray: true,
                params: {
                    sensorId: null,
                    startDate: null,
                    endDate: null
                }
            }
        });
    }
})();