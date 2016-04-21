(function() {
    'use strict';

    angular
        .module('spevnikApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });
    }
})();

$( document ).ready(function() {
    $(document).on('click', '.songtext-button-minus', function(){
        currentSize = parseInt($('pre').css('font-size'));
        $('pre').css('font-size', currentSize - 1);
    });
     $(document).on('click', '.songtext-button-plus', function(){
        currentSize = parseInt($('pre').css('font-size'));
        $('pre').css('font-size', currentSize + 1);
    });

    $(document).on('click', '.chords-button-minus', function(){
        currentSize = parseInt($('.chord').css('font-size'));
        $('.chord').css('font-size', currentSize - 1);
    });

    $(document).on('click', '.chords-button-plus', function(){
        currentSize = parseInt($('.chord').css('font-size'));
        $('.chord').css('font-size', currentSize + 1);
    });
});
