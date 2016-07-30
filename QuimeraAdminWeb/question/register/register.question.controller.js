(function () {
    'use strict';

    angular
        .module('app')
        .controller('QuestionRegisterController', QuestionRegisterController);

    QuestionRegisterController.$inject = ['QuestionService', '$location', 'FlashService', '$scope', 'SubcategoryService', 'CategoryService'];
    function QuestionRegisterController(QuestionService, $location, FlashService, $scope, SubcategoryService, CategoryService) {
        var vm = this;
        vm.question = {};
        vm.question.options = [];
        vm.registerQuestion = registerQuestion;

        initController();

        function initController() {
            loadAllCategories();
            loadAllSubcategories();
        }

        function registerQuestion() {
            vm.dataLoading = true;
            QuestionService.Create(vm.question)
                .then(function (response) {
                    if (response==="") {
                        FlashService.Success('Pregunta registrada.', true);
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