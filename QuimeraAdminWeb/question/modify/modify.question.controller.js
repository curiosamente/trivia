(function () {
    'use strict';

    angular
        .module('app')
        .controller('ModifyQuestionController', ModifyQuestionController);

    ModifyQuestionController.$inject = ['QuestionService', '$location', '$routeParams', 'FlashService', '$scope', 'SubcategoryService', 'CategoryService'];
    function ModifyQuestionController(QuestionService, $location, $routeParams, FlashService, $scope, SubcategoryService, CategoryService) {
        var vm = this;
        vm.question = null;

        vm.modifyQuestion = modifyQuestion;

        initController();

        function initController() {
            loadCurrentQuestion();
            loadAllCategories();
            loadAllSubcategories();
        }

        function loadCurrentQuestion() {
            QuestionService.GetById($routeParams.id)
                .then(function (question) {
                    vm.question = question;
                });
        }

        function modifyQuestion() {
            vm.dataLoading = true;
            QuestionService.Update(vm.question)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Pregunta modificada.', true);
                        $location.path('/question');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

        $scope.addItem = function(item){
            $scope.vm.question.options.push(item);
            $scope.newItem = null;
        };

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

    }



})();