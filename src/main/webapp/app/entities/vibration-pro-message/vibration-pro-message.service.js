(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('VibrationProMessage', VibrationProMessage);

    VibrationProMessage.$inject = ['$resource', 'DateUtils'];

    function VibrationProMessage ($resource, DateUtils) {
        var resourceUrl =  'api/vibration-pro-messages/:id';

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
            'update': { method:'PUT' },
            'search': {
                method: 'GET',
                url: 'api/vibration-pro-messages/search',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.forEach(function(item) {
                            item.insertDate = DateUtils.convertDateTimeFromServer(item.insertDate);
                        });
                    }
                    return data;
                }
            }
        });
    }
})();
