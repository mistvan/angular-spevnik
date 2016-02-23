'use strict';

angular.module('spevnikApp')
    .factory('Account', function Account($resource) {
        //return JSON.parse('{"login":"admin","password":null,"firstName":"Administrator","lastName":"Administrator","email":"admin@localhost","activated":true,"langKey":"en","authorities":["ROLE_USER","ROLE_ADMIN"]}');
         return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    });
