(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies'])
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider'];
    function config($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'home/home.view.html',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'login/login.view.html',
                controllerAs: 'vm'
            })

            .when('/user', {
                controller: 'UserController',
                templateUrl: 'user/user.view.html',
                controllerAs: 'vm'
            })


            .when('/user/modify/:id', {
                controller: 'ModifyUserController',
                templateUrl: 'user/modify/modify.user.view.html',
                controllerAs: 'vm'
            })

            .when('/user/register', {
                controller: 'UserRegisterController',
                templateUrl: 'user/register/register.user.view.html',
                controllerAs: 'vm'
            })

            .when('/category', {
                controller: 'CategoryController',
                templateUrl: 'category/category.view.html',
                controllerAs: 'vm'
            })

            .when('/category/register', {
                controller: 'CategoryRegisterController',
                templateUrl: 'category/register/register.category.view.html',
                controllerAs: 'vm'
            })

            .when('/category/modify/:id', {
                controller: 'ModifyCategoryController',
                templateUrl: 'category/modify/modify.category.view.html',
                controllerAs: 'vm'
            })

            .when('/subcategory', {
                controller: 'SubcategoryController',
                templateUrl: 'subcategory/subcategory.view.html',
                controllerAs: 'vm'
            })

            .when('/subcategory/register', {
                controller: 'SubcategoryRegisterController',
                templateUrl: 'subcategory/register/register.subcategory.view.html',
                controllerAs: 'vm'
            })

            .when('/subcategory/modify/:id', {
                controller: 'ModifySubcategoryController',
                templateUrl: 'subcategory/modify/modify.subcategory.view.html',
                controllerAs: 'vm'
            })

            .when('/question', {
                controller: 'QuestionController',
                templateUrl: 'question/question.view.html',
                controllerAs: 'vm'
            })

            .when('/question/modify/:id', {
                controller: 'ModifyQuestionController',
                templateUrl: 'question/modify/modify.question.view.html',
                controllerAs: 'vm'
            })

            .when('/question/register', {
                controller: 'QuestionRegisterController',
                templateUrl: 'question/register/register.question.view.html',
                controllerAs: 'vm'
            })

            .when('/trivia', {
                controller: 'TriviaController',
                templateUrl: 'trivia/trivia.view.html',
                controllerAs: 'vm'
            })

            .when('/trivia/register', {
                controller: 'RegisterTriviaController',
                templateUrl: 'trivia/register/register.trivia.view.html',
                controllerAs: 'vm'
            })

            .when('/trivia/modify/:id', {
                controller: 'ModifyTriviaController',
                templateUrl: 'trivia/modify/modify.trivia.view.html',
                controllerAs: 'vm'
            })

            .when('/setting/trivia/selection', {
                controller: 'SelectionTriviaController',
                templateUrl: 'control-panel/selection/selection.trivia.view.html',
                controllerAs: 'vm'
            })

            .when('/setting', {
                controller: 'ControlPanelController',
                templateUrl: 'control-panel/control-panel.view.html',
                controllerAs: 'vm'
            })

            .when('/banner/register', {
                controller: 'RegisterBannerController',
                templateUrl: 'banner/register/register.banner.view.html',
                controllerAs: 'vm'
            })

            .when('/banner', {
                controller: 'BannerController',
                templateUrl: 'banner/banner.view.html',
                controllerAs: 'vm'
            })

            .when('/bar', {
                controller: 'BarController',
                templateUrl: 'bar/bar.view.html',
                controllerAs: 'vm'
            })

            .when('/bar/register', {
                controller: 'RegisterBarController',
                templateUrl: 'bar/register/register.bar.view.html',
                controllerAs: 'vm'
            })
            .when('/bar/modify/:id', {
                controller: 'ModifyBarController',
                templateUrl: 'bar/modify/modify.bar.view.html',
                controllerAs: 'vm'
            })
            .otherwise({ redirectTo: '/' });
    }

    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        $rootScope.url = 'http://curiosamente-prod.sa-east-1.elasticbeanstalk.com';

        //$rootScope.url = 'http://localhost:8080';
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

})();