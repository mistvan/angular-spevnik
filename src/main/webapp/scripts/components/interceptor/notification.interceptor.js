 'use strict';

angular.module('spevnikApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-spevnikApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-spevnikApp-params')});
                }
                return response;
            }
        };
    });
