'use strict';

describe('Controller Tests', function() {

    describe('Song Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSong, MockPlaylist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSong = jasmine.createSpy('MockSong');
            MockPlaylist = jasmine.createSpy('MockPlaylist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Song': MockSong,
                'Playlist': MockPlaylist
            };
            createController = function() {
                $injector.get('$controller')("SongDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'spevnikApp:songUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
