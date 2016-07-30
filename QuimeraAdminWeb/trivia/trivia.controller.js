(function () {
    'use strict';

    angular
        .module('app')
        .controller('TriviaController', TriviaController);

    TriviaController.$inject = ['TriviaService', '$location', 'FlashService'];
    function TriviaController(TriviaService, $location, FlashService) {
        var vm = this;

        vm.allTrivias = [];
        vm.deleteTrivia = deleteTrivia;
        vm.questions = questions;
        initController();

        function initController() {
            loadAllTrivias();
        }

        function loadAllTrivias() {
            TriviaService.GetAll()
                .then(function (trivias) {
                    vm.allTrivias = trivias;
                });
        }

        function deleteTrivia(id) {
            TriviaService.Delete(id)
                .then(function (response) {
                    if (response === "") {
                        FlashService.Success('Trivia eliminada.', false);
                        loadAllTrivias();
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/trivia');
                });
        }

        function questions(questions) {
            var questionsStrings = "";
            questions.forEach(function (question, index) {
                questionsStrings += index+1 + '. ' + question.question + '   ';
            });
            return questionsStrings;
        }

    }

})();