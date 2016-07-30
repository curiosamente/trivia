(function () {
    'use strict';

    angular
        .module('app')
        .factory('QuestionService', QuestionService);

    QuestionService.$inject = ['$http', '$rootScope'];
    function QuestionService($http, $rootScope) {
        var url = $rootScope.url;
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get(url + '/question').then(handleSuccess, handleError('Error getting all questions'));
        }

        function GetById(id) {
            return $http.get(url + '/question', {params: {id: id}}).then(handleSuccess, handleError('Error getting question by id'));
        }

        function Create(question) {
            return $http.post(url + '/question', question).then(handleSuccess, handleError('Error creating question'));
        }

        function Update(question) {
            return $http.put(url + '/question', question).then(handleSuccess, handleError('Error updating question'));
        }

        function Delete(id) {
            return $http.delete(url + '/question', {params: {id: id}}).then(handleSuccess, handleError('Error deleting question'));
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();
