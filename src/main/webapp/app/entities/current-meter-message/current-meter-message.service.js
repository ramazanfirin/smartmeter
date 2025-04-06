(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('CurrentMeterMessage', CurrentMeterMessage);

    CurrentMeterMessage.$inject = ['$resource'];

    function CurrentMeterMessage ($resource) {
        var resourceUrl =  'api/current-meter-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'search': {
                method: 'GET',
                url: 'api/current-meter-messages/search',
                isArray: true
            }
        });
    }
})();
