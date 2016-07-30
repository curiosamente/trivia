(function () {
    'use strict';

    angular
        .module('app')
        .controller('SelectionTriviaController', SelectionTriviaController);

    SelectionTriviaController.$inject = ['TriviaService', '$location', 'FlashService'];
    function SelectionTriviaController(TriviaService, $location, FlashService) {
        var vm = this;

        vm.allTrivias = [];
        vm.questions = questions;
        initController();
        vm.selectTrivia = selectTrivia;
        vm.checkedTrivia =  checkedTrivia;
        function initController() {
            loadAllTrivias();
        }

        function loadAllTrivias() {
            TriviaService.GetAll()
                .then(function (trivias) {
                    vm.allTrivias = trivias;
                });
        }

        function selectTrivia() {
            vm.dataLoading = true;
            TriviaService.SetCurrentTrivia(vm.trivia.idTrivia)
                .then(function (response) {
                    if (response.status === 200) {
                        FlashService.Success(response.data.message, true);
                        vm.dataLoading = true;
                    } else if (response.status === 202) {
                        FlashService.Error(response.data.message);
                        vm.dataLoading = false;
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                    $location.path('/settings');
                });
        }

        function questions(questions) {
            var questionsStrings = "";
            questions.forEach(function (question, index) {
                questionsStrings += index+1 + '. ' + question.question + '   ';
            });
            return questionsStrings;
        }

        function checkedTrivia() {
            vm.trivia = JSON.parse($('input[name=radioTrivia]:checked').val());
        }
    }

})();