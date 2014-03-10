(ns mort.core
  (:gen-class)
  (:require [mort.conversion :as conversion])
  (:require [mort.scenario :as scenario])
  (:require [mort.util :as util]))


(defn -main  [& args]
  (println "Hello World"))

;; below: reusable, component functions

(defn calculate-tax-credit
  "Calculates the amount of money recouped by a mortgage interest tax credit given a year's interest payments."
  [interest-paid tax-credit]
  (if (> (* interest-paid (conversion/rate-to-percent (:rate tax-credit))) (:max tax-credit))
    (:max tax-credit)
    (* interest-paid (conversion/rate-to-percent (:rate tax-credit)))))

(defn net-worth-after-one-month
  "TODO: don't require callers to negate the result in order for this to make sense as a net worth value"
  [interest-rate payment balance]
  (- (+  balance (util/one-month-of-interest interest-rate balance)) payment))

(defn interest-paid-in-one-year
  "Calculates the gross expense of interest accrued during one year of payments."
  [balance interest-rate required principle bonus]
  (loop [iteration 0 balance balance interest-paid 0]
    (if (and (< iteration 12) (> balance 0))
      (recur
       (inc iteration)
       (let [new-balance (- (+ balance (util/one-month-of-interest interest-rate balance)) required principle (get bonus iteration 0))]
         (if (< new-balance 0) 0 new-balance))
       (+ interest-paid (util/one-month-of-interest interest-rate balance)))
      interest-paid)))

(defn new-balance-after-one-month
  [interest-rate payment balance]
  (let [new-balance (- (+  balance (util/one-month-of-interest interest-rate balance)) payment)]
    (if (< new-balance 0) 0 new-balance)))

(defn account-balance-monthly
  "Returns a seq whose elements are the balance of a mortgage each month after applied payment and accrued interest."
  [interest-rate payment balance]
  (iterate (partial new-balance-after-one-month interest-rate payment) balance))

(defn net-worth-monthly
  "Essentially the mortgage balance after a month of payments. Will be positive if the mortgage was fully paid during the month."
  [interest-rate payment balance]
  (map - (iterate (partial net-worth-after-one-month interest-rate payment) balance)))

(defn net-worth-after-one-year
  "Accounting only for mortgage balance and payments, what's my net worth after 12 months?"
  [interest-rate payment balance]
  (nth (net-worth-monthly interest-rate payment balance) 12))



(defn net-worth-monthly-v2
  "Takes a starting net worth, applies a collection of recurring debits/credits repeatedly."
  [savings debts]
  ())

(defn interest-func
  "monthly interest"
  [rate]
  (partial util/one-month-of-interest rate))

(defprotocol AccountProto
  (accrue [_ balance] "calculates interest accrued")
  (pay [_ balance payment] "calculates new balance after a payment")
  (balance? [_] "get the balance of the account"))


(defrecord Account [rate starting-balance]
  AccountProto
  (accrue [_ balance] (+ (util/one-month-of-interest rate balance) balance))
  (pay [_ balance payment] (- balance payment))
  (balance? [_] ))

(def mortgage (Account. 5 100000))
(type mortgage)
(accrue mortgage (:starting-balance mortgage))
(pay mortgage (:starting-balance mortgage) 1000)
(map float (account-balance-monthly 5 417 100000))


(net-worth-monthly-v2 (fn [] (3000)) ((interest-func 5) 100000))

(:balance scenario/mortgage)
(scenario/salary scenario/salary-init)
(scenario/investment-balance scenario/investment-init)
