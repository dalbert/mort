(ns mort.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [mort.core :refer :all :as core]))

(fact "calculate-tax-credit returns a percentage of interest-paid or the maximum amount"
      (core/calculate-tax-credit 10000 {:rate 30 :max 2000}) => 2000
      (core/calculate-tax-credit 10000 {:rate 30 :max 2999}) => 2999
      (core/calculate-tax-credit 10000 {:rate 30 :max 3001}) => 3000
      (core/calculate-tax-credit 5000 {:rate 30 :max 2000}) => 1500)

(fact "new-balance-after-one-month subtracts total-payment from the balance plus 1 month's accrued interest."
      (new-balance-after-one-month 5 1000 100000) => 298250/3
      (new-balance-after-one-month 5 2000 1000) => 0
      (new-balance-after-one-month 5 1250/3 100000) => 100000)

;(fact "new-balance-after-one-year: paying 600/mo on a 100000 loan w/ 5% interest brings the balance down to $97748.875"
;      (new-balance-after-one-year 100000 5 600 {}) => 892455732753996026284328070709/9130086859014144000000000)

;(fact "new-balance-after-one-year: paying 400/mo on a 100000 loan w/ 5% interest brings the balance up to $100204.65"
;      (new-balance-after-one-year 100000 5 400 {}) => 914877136187543343065061084481/9130086859014144000000000)

;(fact "new-balance-after-one-year: stops at 0 if you pay it off completely"
;      (new-balance-after-one-year 100000 5 25000 {}) => 0)

(fact "net-worth-after-one-year is basically the same as new-balance-after-one-year but negated"
      (net-worth-after-one-year 5 600 100000) => -892455732753996026284328070709/9130086859014144000000000)

(fact "net-worth-after-one-year will continue into positive balances even after the mortgage is paid in full"
      (net-worth-after-one-year 5 25000 100000) => 73718219445551064838603984379/365203474360565760000000)

(fact "starting balance: $100K, int rate: 5%, monthly payment: $1K - first five monthly balances as floats"
      (take 5 (core/account-balance-monthly 5 1000 100000)) => '(100000 298250/3 7115825/72 339526765/3456 16199302073/165888 ))

(fact "starting balance: $100K, int rate: 5%, monthly payment: $25K - balance drops to zero and does not continue into negative values"
      (nth (core/account-balance-monthly 5 25000 100000) 12) => 0)

(fact "net-worth-monthly is the same as monthly balances with each element negated"
      (take 5 (core/net-worth-monthly 5 1000 100000)) => '(-100000 -298250/3 -7115825/72 -339526765/3456 -16199302073/165888 ))

