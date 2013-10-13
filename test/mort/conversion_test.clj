(ns mort.conversion-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [mort.conversion :refer :all :as conversion]))

(fact "months-to-years just divides any number by 12"
      (conversion/months-to-years 12) => 1
      (conversion/months-to-years 6) => 1/2
      (conversion/months-to-years 30) => 5/2
      (conversion/months-to-years 60) => 5)

(fact "annual-rate-to-monthly-percent divides the given rate by 100 and then by 12, ie x/1200"
      (annual-rate-to-monthly-percent 5) => 5/1200
      (* 100000 (annual-rate-to-monthly-percent 5)) => 1250/3)
