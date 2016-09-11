(function () {
    'use strict';

    angular
        .module('app')
        .controller('TriviaController', TriviaController);

    TriviaController.$inject = ['GameManagerService', '$interval', '$scope', '$timeout'];
    function TriviaController(GameManagerService, $interval, $scope, $timeout) {

        var vm = this;
        var elapsedTimeInterval = null;

        var actualTimeOut = null;

        var checkingTrivia = null;

        vm.progressBarValue = 100;

        var triviaStarted = false;

        var MAX_ELAPSED_TIME = 15;

        vm.elapsedTime = MAX_ELAPSED_TIME;

        vm.correctAnswer = null;

        vm.trivia = null;
        vm.statusTrivia = null;

        vm.currentQuestion = null;

        vm.options = null;

        vm.questionPosition = 0;

        vm.scores = [];

        vm.description = null;

        vm.elapsedTimeToShow = MAX_ELAPSED_TIME;

        var startingTriviaTime = 1000;

        var showingQuestionTime = 5000;
        var showingOptionTime = 1000;

        var waitingCorrectAnswerTime = 2000;
        var showingCorrectAnswerTime = 5000;

        var showingDescriptionTime = 10000;
        var showingPartialWinnerTime = 10000;
        var showingBannerTime = 15000;
        var showingFinalWinnerTime = 30000;

        function getTrivia() {
            GameManagerService.GetCurrentTrivia()
                .then(function (response) {
                    if (response != "" && response.success != false) {
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
            vm.questionPosition = 0;
            vm.scores = [];

            vm.statusTrivia = 'STARTING_TRIVIA';
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(nextQuestion, startingTriviaTime);
        }

        function getScores() {
            GameManagerService.GetScores()
                .then(function (response) {
                    if (response != "" && response.success != false) {
                        vm.dataLoading = true;
                        vm.scores = response;
                    } else {
                        vm.scores = [];
                    }
                });
        }

        function nextQuestion() {

            vm.urlBanner = null;
            vm.statusTrivia = 'SHOWING_QUESTION';
            vm.questionPosition++;
            vm.currentQuestion = vm.trivia.questions[vm.questionPosition - 1];
            vm.currentQuestion.currentPosition = vm.questionPosition;
            GameManagerService.SetCurrentQuestion(vm.currentQuestion);
            GameManagerService.SetStatus(vm.statusTrivia);
            vm.elapsedTimeToShow = MAX_ELAPSED_TIME;
            actualTimeOut = setTimeout(showingOptions, showingQuestionTime);
        }

        function showingOptions() {
            vm.statusTrivia = 'SHOWING_OPTIONS';
            GameManagerService.SetStatus(vm.statusTrivia);
            vm.progressBarValue = 100;

            vm.options = vm.currentQuestion.options;

            actualTimeOut = setTimeout(countdown, showingOptionTime);
        }

        function countdown() {
            elapsedTimeInterval = $interval(function () {
                if (vm.elapsedTime > 0) {
                    vm.elapsedTimeToShow = vm.elapsedTime;
                    vm.elapsedTime = vm.elapsedTime - 1;
                    vm.progressBarValue = (vm.elapsedTime / MAX_ELAPSED_TIME) * 100;

                } else {
                    vm.elapsedTimeToShow = 0;
                    $interval.cancel(elapsedTimeInterval);
                }
            }, 1000);

            actualTimeOut = setTimeout(waitingCorrectAnswer, MAX_ELAPSED_TIME * 1000 + 1000);
        }

        function waitingCorrectAnswer() {
            vm.statusTrivia = 'WAITING_CORRECT_ANSWER';
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(showingCorrectAnswer, waitingCorrectAnswerTime);

        }

        function showingCorrectAnswer() {
            vm.elapsedTime = MAX_ELAPSED_TIME;
            vm.progressBarValue = 100;
            vm.statusTrivia = 'SHOWING_CORRECT_ANSWER';
            GameManagerService.SetStatus(vm.statusTrivia);
            vm.options = [];
            vm.options.push(vm.currentQuestion.correctAnswer);
            getScores();
            actualTimeOut = setTimeout(showingDescription, showingCorrectAnswerTime);
        }

        function showingDescription() {
            vm.statusTrivia = 'SHOWING_DESCRIPTION';
            GameManagerService.SetStatus(vm.statusTrivia);
            actualTimeOut = setTimeout(showingPartialWinners, showingDescriptionTime);
        }

        function showingPartialWinners() {
            vm.options = null;
            vm.statusTrivia = 'SHOWING_PARTIAL_WINNERS';
            GameManagerService.SetStatus(vm.statusTrivia);

            if (vm.questionPosition == 5 || vm.questionPosition == 10 || vm.questionPosition == 15) {
                actualTimeOut = setTimeout(showingBanners, showingPartialWinnerTime);
            } else {
                actualTimeOut = setTimeout(nextQuestion, showingPartialWinnerTime);
            }
        }

        function showingBanners() {

            GameManagerService.SetStatus(vm.statusTrivia);
            if (vm.questionPosition == 5) {
                vm.urlBanner = vm.trivia.bar.banners[0].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(nextQuestion, showingBannerTime);
            } else if (vm.questionPosition == 10) {
                vm.urlBanner = vm.trivia.bar.banners[1].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(nextQuestion, showingBannerTime);
            } else if (vm.questionPosition == 15) {
                vm.urlBanner = vm.trivia.bar.banners[2].url;
                vm.statusTrivia = 'SHOWING_BANNER';
                actualTimeOut = setTimeout(showingFinalWinners, showingBannerTime);
            }
            GameManagerService.SetStatus(vm.statusTrivia);
        }

        function showingFinalWinners() {

            vm.statusTrivia = 'SHOWING_FINAL_WINNERS';
            // document.getElementById("score").firstElementChild.style.fontSize = "xx-large";
            GameManagerService.SetStatus(vm.statusTrivia);

            actualTimeOut = setTimeout(finishTrivia, showingFinalWinnerTime);
        }

        function finishTrivia() {
            vm.statusTrivia = 'WAITING_TRIVIA';
            GameManagerService.SetStatus('TERMINATED');
            GameManagerService.FinishTrivia();

            triviaStarted = false;

            clearData();
            actualTimeOut = setTimeout(endDelay, 3000);
        }

        function endDelay() {

        }

        function clearData() {

            vm.correctAnswer = null;

            vm.trivia = null;

            vm.statusTrivia = null;

            vm.currentQuestion.question = null;

            vm.currentQuestion.currentPosition = 0;

            vm.currentQuestion = null;

            vm.options = [];

            vm.questionPosition = 0;

            vm.description = null;

        }

        $(function () {
            checkingTrivia = $interval(function () {
                getTrivia();
                if (vm.trivia == null) {
                    vm.statusTrivia = 'WAITING_TRIVIA';
                    triviaStarted = false;
                    if (actualTimeOut) {
                        $timeout.cancel(actualTimeOut);
                    }
                } else if (!triviaStarted) {
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