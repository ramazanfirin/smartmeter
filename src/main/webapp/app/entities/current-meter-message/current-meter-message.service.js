(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('CurrentMeterMessage', CurrentMeterMessage);

    CurrentMeterMessage.$inject = ['$resource', 'DateUtils'];

    function CurrentMeterMessage ($resource, DateUtils) {
        var resourceUrl =  'api/current-meter-messages/:id';

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
