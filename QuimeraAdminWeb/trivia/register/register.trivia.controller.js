(function () {
    'use strict';

    angular
        .module('app')
        .controller('RegisterTriviaController', RegisterTriviaController);

    RegisterTriviaController.$inject = ['QuestionService', 'TriviaService', '$location', 'FlashService', 'SubcategoryService', 'CategoryService', 'BarService'];
    function RegisterTriviaController(QuestionService, TriviaService, $location, FlashService, SubcategoryService, CategoryService, BarService) {
        var vm = this;

        vm.trivia = {
            rounds: 1
        };
        vm.allBars = [];
        vm.allQuestions = [];

        vm.registerTrivia = registerTrivia;
        vm.checkedQuestions = checkedQuestions;
        vm.loadAllCategories = loadAllCategories;
        vm.loadAllSubcategories = loadAllSubcategories;
        vm.loadAllBars = loadAllBars;
        initController();

        function initController() {
            loadAllQuestions();
            loadAllCategories();
            loadAllSubcategories();
            loadAllBars();
        }

        function loadAllQuestions() {
            QuestionService.GetAll()
                .then(function (questions) {
                    vm.allQuestions = questions;
                });
        }

        function registerTrivia() {
            vm.dataLoading = true;

            TriviaService.Create(vm.trivia)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Trivia registrada.', false);
                        $location.path('/trivia');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        function checkedQuestions() {
            //To reset selected question
            vm.trivia.questions = [];

            var aux = $('.questionSelected:checked').toArray();

            aux.forEach(function(option){
                vm.trivia.questions.push(JSON.parse(option.value));
            });

        }
        function loadAllCategories() {
            CategoryService.GetAll()
                .then(function (categories) {
                    vm.allCategories = categories;
                });
        }

        function loadAllSubcategories() {
            SubcategoryService.GetAll()
                .then(function (subcategories) {
                    vm.allSubcategories = subcategories;
                });
        }

        function loadAllBars() {
            BarService.GetAll()
                .then(function (bars) {
                    vm.allBars = bars;
                });
        }

    }

})();