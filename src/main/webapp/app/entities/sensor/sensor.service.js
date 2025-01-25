(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('Sensor', Sensor);

    Sensor.$inject = ['$resource', 'DateUtils'];

    function Sensor ($resource, DateUtils) {
        var resourceUrl =  'api/sensors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastSeenDate = DateUtils.convertDateTimeFromServer(data.lastSeenDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
