(function() {
    'use strict';
    angular
        .module('spevnikApp')
        .factory('Song', Song);

    Song.$inject = ['$resource'];

    function Song ($resource) {
        var resourceUrl =  'api/songs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
