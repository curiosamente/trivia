/**
 * Created by Manu on 29/06/2016.
 */
(function () {
    'use strict';

    angular
        .module('app')
        .factory('GameManagerService', GameManagerService);

    GameManagerService.$inject = ['$http', '$rootScope'];

    function GameManagerService($http, $rootScope) {
        var service = {};
        var url = $rootScope.url;
        service.FinishTrivia = FinishTrivia;
        service.GetGames = GetGames;

        return service;

        function GetGames() {
            return $http.get(url + '/game').then(handleSuccess, handleError('Error getting games'));
        }

        function FinishTrivia(idBar) {
            return $http.patch(url + '/game/finishTrivia?idBar=' + idBar).then(handleSuccess, handleError('Error finishing trivia'));
        }

        function handleSuccess(res) {
            return res;
        }

        function handleError(error) {
            return function () {
                return {success: false, message: error};
            };
        }
    }

})();
