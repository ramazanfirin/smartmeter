(function() {
    'use strict';
    angular
        .module('smartmeterApp')
        .factory('M2mMessage', M2mMessage);

    M2mMessage.$inject = ['$resource', 'DateUtils'];

    function M2mMessage ($resource, DateUtils) {
        var resourceUrl =  'api/m-2-m-messages/:id';

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
