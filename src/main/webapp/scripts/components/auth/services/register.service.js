'use strict';

angular.module('spevnikApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


