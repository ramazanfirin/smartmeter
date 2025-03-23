(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('VibrationProMessage', VibrationProMessage);

    VibrationProMessage.$inject = ['$resource'];

    function VibrationProMessage ($resource) {
        var resourceUrl =  'api/vibration-pro-messages/:id';

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
