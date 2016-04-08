'use strict';

describe('Controller Tests', function() {

    describe('Playlist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlaylist, MockSong;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlaylist = jasmine.createSpy('MockPlaylist');
            MockSong = jasmine.createSpy('MockSong');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Playlist': MockPlaylist,
                'Song': MockSong
            };
            createController = function() {
                $injector.get('$controller')("PlaylistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'spevnikApp:playlistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
