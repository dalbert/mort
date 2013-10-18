(ns mort.time-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [mort.time :refer :all :as time]))

(fact "month-vector returns an ordered vector of month symbols starting with the given month"
      (time/months-vector) => '(1 2 3 4 5 6 7 8 9 10 11 12))

(fact "month-seq returns an ordered cycle of months started with the given month"
      (seq? (time/months-seq 1)) => true
      (first (time/months-seq 3)) => 3
      (nth (time/months-seq 4) 7) => 11)
