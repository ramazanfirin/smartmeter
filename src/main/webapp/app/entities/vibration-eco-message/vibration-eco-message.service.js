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
                        data.insertDate = DateUtils.convertDateTimeFromServer(data.insertDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
