(function() {
    'use strict';

    angular
        .module('spevnikApp', [
            'ngStorage', 
            'tmh.dynamicLocale',
            'pascalprecht.translate', 
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    // disable html check for ng-bing-html
    angular.module("spevnikApp").config(['$sceProvider', function ($sceProvider) {
        $sceProvider.enabled(false);
    }]);

    // ChordPro.js filter
    angular.module("spevnikApp").filter('convertChords', function () {    
        return function (input) {
            return ChordPro.to_txt(input);
        };
    });

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
