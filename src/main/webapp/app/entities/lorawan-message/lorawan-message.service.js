(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('LorawanMessage', LorawanMessage);

    LorawanMessage.$inject = ['$resource', 'DateUtils'];

    function LorawanMessage ($resource, DateUtils) {
        var resourceUrl =  'api/lorawan-messages/:id';

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
