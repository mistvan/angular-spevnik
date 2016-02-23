'use strict';

angular.module('spevnikApp')
    .factory('Song', function ($resource, DateUtils) {
        return $resource('http://mistvan.spolocenstvomajak.sk/spevnik?rest_route=/wp/v2/song/:id', {}, {
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
    });
