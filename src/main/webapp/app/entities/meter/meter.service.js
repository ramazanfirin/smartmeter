(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('Meter', Meter);

    Meter.$inject = ['$resource'];

    function Meter ($resource) {
        var resourceUrl =  'api/meters/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
