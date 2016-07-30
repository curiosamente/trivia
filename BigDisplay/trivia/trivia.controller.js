(function () {
    'use strict';

    angular
        .module('app')
        .controller('TriviaController', TriviaController);

    TriviaController.$inject = ['GameManagerService', '$interval', '$scope'];
    function TriviaController(GameManagerService, $interval, $scope) {

        var vm = this;
        var elapsedTimeInterval = null;

        var actualTimeOut = null;

        var checkingTrivia = null;

        vm.progressBarValue = 100;

        var triviaStarted = false;

        var MAX_ELAPSED_TIME = 5;

        vm.elapsedTime = MAX_ELAPSED_TIME;

        vm.correctAnswer = null;

        vm.trivia = null;
        vm.statusTrivia = null;

        vm.currentQuestion = null;

        vm.options = null;

        vm.questionPosition = 0;

        vm.description = null;

        vm.elapsedTimeToShow = MAX_ELAPSED_TIME;

        function getTrivia() {
            GameManagerService.GetCurrentTrivia()
                .then(function (response) {
                    if (response != "") {
                        vm.dataLoading = true;
                        vm.trivia = response;
                    } else {
                        vm.trivia = null;
                        vm.dataLoading = false;
                    }
                });
        }

        function triviaStart() {
            triviaStarted = true;
            vm.statusTrivia = 'STARTING_TRIVIA';
            actualTimeOut = setTimeout(nextQuestion, 500);
        }

        function nextQuestion() {
            vm.urlBanner = null;
            vm.statusTrivia = 'SHOWING_QUESTION';
            vm.questionPosition++;
            vm.currentQuestion = vm.trivia.questions[vm.questionPosition - 1];
            vm.currentQuestion.currentPosition = vm.questionPosition;
            GameManagerService.SetCurrentQuestion(vm.currentQuestion);
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(showingOptions, 5000);
        }

        function showingOptions() {
            vm.statusTrivia = 'SHOWING_OPTIONS';
            GameManagerService.SetStatus(vm.statusTrivia);
            vm.progressBarValue = 100;

            vm.options = vm.currentQuestion.options;

            actualTimeOut = setTimeout(countdown, 1000);
        }

        function countdown() {
            elapsedTimeInterval = $interval(function () {
                GameManagerService.SetElapsedTime(vm.elapsedTime);
                if (vm.elapsedTime > 0) {
                    vm.elapsedTimeToShow = vm.elapsedTime;
                    vm.elapsedTime = vm.elapsedTime - 1;
                    vm.progressBarValue = (vm.elapsedTime/MAX_ELAPSED_TIME)*100;

                } else {
                    vm.elapsedTimeToShow = MAX_ELAPSED_TIME;
                    $interval.cancel(elapsedTimeInterval);
                }
            }, 1000);

            actualTimeOut = setTimeout(showingCorrectAnswer, MAX_ELAPSED_TIME*1000 + 1000);
        }

        function showingCorrectAnswer() {
            vm.elapsedTime = MAX_ELAPSED_TIME;
            vm.progressBarValue = 100;
            vm.statusTrivia = 'SHOWING_CORRECT_ANSWER';
            GameManagerService.SetStatus(vm.statusTrivia);
            vm.options = [];
            vm.options.push(vm.currentQuestion.correctAnswer);
            actualTimeOut = setTimeout(showingDescription, 5000);
        }

        function showingDescription() {
            vm.statusTrivia = 'SHOWING_DESCRIPTION';
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(showingPartialWinners, 5000);
        }

        function showingPartialWinners() {
            vm.options = null;
            vm.statusTrivia = 'SHOWING_PARTIAL_WINNERS';
            GameManagerService.SetStatus(vm.statusTrivia);
            if (vm.questionPosition == 5 || vm.questionPosition == 10 || vm.questionPosition == 15) {
                actualTimeOut = setTimeout(showingBanners, 5000);
            } else {
                actualTimeOut = setTimeout(nextQuestion, 5000);
            }
        }

        function showingBanners() {

            GameManagerService.SetStatus(vm.statusTrivia);
            if (vm.questionPosition == 5) {
                vm.urlBanner = vm.trivia.bar.banners[0].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(nextQuestion, 5000);
            } else if (vm.questionPosition == 10) {
                vm.urlBanner = vm.trivia.bar.banners[1].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(nextQuestion, 5000);
            } else if (vm.questionPosition == 15) {
                vm.urlBanner = vm.trivia.bar.banners[2].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(showingFinalWinners, 5000);
            }
        }

        function showingFinalWinners() {
            vm.questionPosition = 0;
            vm.currentQuestion = null;
            vm.statusTrivia = 'SHOWING_FINAL_WINNERS';
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(finishTrivia, 5000);
        }

        function finishTrivia() {

            vm.statusTrivia = 'TERMINATED';

            triviaStarted = false;

            GameManagerService.FinishTrivia();

            vm.correctAnswer = null;

            vm.trivia = null;

            vm.statusTrivia = null;

            vm.currentQuestion.question = null;

            vm.currentQuestion = null;

            vm.options = [];

            vm.questionPosition = 0;

            vm.currentQuestion.currentPosition = 0;

            vm.description = null;

        }

        $(function () {
            checkingTrivia = $interval(function () {
                getTrivia();
                if (vm.trivia == null) {
                    vm.statusTrivia = 'WAITING_TRIVIA';
                    triviaStarted = false;
                } else if(!triviaStarted){
                    triviaStart();
                }
            }, 1000);
        });

        $scope.$on('$destroy', function () {
            $interval.cancel(checkingTrivia);
            $interval.cancel(elapsedTimeInterval);
            $timeout.cancel(actualTimeOut);
        });

    }

})();