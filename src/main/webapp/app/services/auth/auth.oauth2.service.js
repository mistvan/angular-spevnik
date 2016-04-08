(function() {
    /*jshint camelcase: false */
    'use strict';

    angular
        .module('spevnikApp')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', 'Base64'];

    function AuthServerProvider ($http, $localStorage, Base64) {
        var service = {
            getToken: getToken,
            getOauthToken: getOauthToken,
            hasValidToken: hasValidToken,
            login: login,
            logout: logout
        };

        return service;

        function getToken () {
            return $localStorage.authenticationToken;
        }

        function getOauthToken() {
            var data = 'username=' +  encodeURIComponent('admin') + '&password=' +
                encodeURIComponent('majakZ34,2ka1') + '&grant_type=password&' +
                'client_secret=SpGI40B1DjG0ozrNJNMtkMmpJsi2GW&client_id=nd9sqLLUNhnTcyTCL6ZtmP7o1IOPDD';

            return $http.post('http://mistvan.spolocenstvomajak.sk/spevnik?oauth=token', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json',
                    'Authorization': 'Basic ' + Base64.encode('nd9sqLLUNhnTcyTCL6ZtmP7o1IOPDD' + ':' + 'SpGI40B1DjG0ozrNJNMtkMmpJsi2GW')
                }
            });
        }

        function hasValidToken () {
            var token = this.getToken();
            return token && token.expires_at && token.expires_at > new Date().getTime();
        }

        function login (credentials) {
            var data = 'username=' +  encodeURIComponent(credentials.username) + '&password=' +
                encodeURIComponent(credentials.password) + '&grant_type=password&scope=read%20write&' +
                'client_secret=SpGI40B1DjG0ozrNJNMtkMmpJsi2GW&client_id=nd9sqLLUNhnTcyTCL6ZtmP7o1IOPDD';

            return $http.post('http://mistvan.spolocenstvomajak.sk/spevnik?oauth=token', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json',
                    'Authorization': 'Basic ' + Base64.encode('nd9sqLLUNhnTcyTCL6ZtmP7o1IOPDD' + ':' + 'SpGI40B1DjG0ozrNJNMtkMmpJsi2GW')
                }
            }).success(authSucess);

            function authSucess (response) {
                var expiredAt = new Date();
                expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
                response.expires_at = expiredAt.getTime();
                $localStorage.authenticationToken = response;
                return response;
            }
        }

        function logout () {
            $http.post('api/logout').then(function() {
                delete $localStorage.authenticationToken;
            });
        }
    }
})();
