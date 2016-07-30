(function () {
    'use strict';

    angular
        .module('app')
        .factory('TriviaService', TriviaService);

    TriviaService.$inject = ['$http', '$rootScope'];
    function TriviaService($http, $rootScope) {

        var url = $rootScope.url;
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get(url + '/trivia').then(handleSuccess, handleError('Error getting all trivia'));
        }

        function GetById(id) {
            return $http.get(url + '/trivia', {params: {id: id}}).then(handleSuccess, handleError('Error getting trivia by id'));
        }

        function Create(trivia) {
            return $http.post(url + '/trivia', trivia).then(handleSuccess, handleError('Error creating trivia'));
        }

        function Update(trivia) {
            return $http.put(url + '/trivia', trivia).then(handleSuccess, handleError('Error updating trivia'));
        }

        function Delete(id) {
            return $http.delete(url + '/trivia', {params: {id: id}}).then(handleSuccess, handleError('Error deleting trivia'));
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return {success: false, message: error};
            };
        }
    }

})();
