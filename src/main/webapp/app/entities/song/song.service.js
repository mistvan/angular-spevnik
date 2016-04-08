(function() {
    'use strict';
    angular
        .module('spevnikApp')
        .factory('Song', Song);

    Song.$inject = ['$resource'];

    function Song ($resource) {
        var resourceUrl =  'http://mistvan.spolocenstvomajak.sk/spevnik?rest_route=/wp/v2/song/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'POST' }
        });
    }
})();
