(ns mort.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [mort.core :refer :all :as core]))

(fact "calculate-tax-credit returns a percentage of interest-paid or the maximum amount"
      (core/calculate-tax-credit 10000 {:rate 30 :max 2000}) => 2000
      (core/calculate-tax-credit 10000 {:rate 30 :max 2999}) => 2999
      (core/calculate-tax-credit 10000 {:rate 30 :max 3001}) => 3000
      (core/calculate-tax-credit 5000 {:rate 30 :max 2000}) => 1500)

(fact "monts-to-years just divides any number by 12"
      (core/months-to-years 12) => 1
      (core/months-to-years 6) => 1/2
      (core/months-to-years 30) => 5/2
      (core/months-to-years 60) => 5)

(fact "new-balance-after-one-month subtracts total-payment from the balance
      plus 1 month's accrued interest. The balance may go negative, users
      of this function should account for this."
      (new-balance-after-one-month 100000 5 1000) => 298250/3
      (new-balance-after-one-month 1000 5 2000) => -5975/6
      (new-balance-after-one-month 100000 5 1250/3) => 100000)
