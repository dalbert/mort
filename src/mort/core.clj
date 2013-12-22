(ns mort.core
  (:gen-class)
  (:require [mort.conversion :as conversion]))


(defn -main  [& args]
  (println "Hello World"))

;; below: reusable, component functions

(defn one-month-of-interest
  "Calculates the amount of interest that would accrue in one month."
  [balance interest-rate]
  (if (> balance 0)
    (* balance (conversion/rate-to-percent (conversion/annual-rate-to-monthly-rate interest-rate)))
    0))

(defn sum-of-one-month-of-payments
  "Sums up all the payments made in a month."
  [required principle bonus]
  (+ required principle bonus))

(defn sum-of-one-year-of-payments
  "Sums up all the payments made in one year."
  [required principle bonus]
  (+ (* required 12) (* principle 12) (apply + (vals bonus))))

(defn calculate-tax-credit
  "Calculates the amount of money recouped by a mortgage interest tax credit given a year's interest payments."
  [interest-paid tax-credit]
  (if (> (* interest-paid (conversion/rate-to-percent (:rate tax-credit))) (:max tax-credit))
    (:max tax-credit)
    (* interest-paid (conversion/rate-to-percent (:rate tax-credit)))))

(defn net-worth-after-one-month
  "TODO: don't require callers to negate the result in order for this to make sense as a net worth value"
  [interest-rate payment balance]
  (- (+  balance (one-month-of-interest balance interest-rate)) payment))

(defn interest-paid-in-one-year
  "Calculates the gross expense of interest accrued during one year of payments."
  [balance interest-rate required principle bonus]
  (loop [iteration 0 balance balance interest-paid 0]
    (if (and (< iteration 12) (> balance 0))
      (recur
       (inc iteration)
       (let [new-balance (- (+ balance (one-month-of-interest balance interest-rate)) required principle (get bonus iteration 0))]
         (if (< new-balance 0) 0 new-balance))
       (+ interest-paid (one-month-of-interest balance interest-rate)))
      interest-paid)))

(defn new-balance-after-one-month
  [interest-rate payment balance]
  (let [new-balance (- (+  balance (one-month-of-interest balance interest-rate)) payment)]
    (if (< new-balance 0) 0 new-balance)))

(defn mortgage-balance-monthly
  "Returns a seq whose elements are the balance of a mortgage each month after applied payment and accrued interest."
  [interest-rate payment balance]
  (iterate (partial new-balance-after-one-month interest-rate payment) balance))

(defn net-worth-monthly
  "Essentially the mortgage balance after a year of payments. Will be positive if the mortgage was fully paid during the year."
  [interest-rate payment balance]
  (map - (iterate (partial net-worth-after-one-month interest-rate payment) balance)))

(defn net-worth-after-one-year
  "Accounting only for mortgage balance and payments, what's my net worth after 12 months?"
  [interest-rate payment balance]
  (nth (net-worth-monthly interest-rate payment balance) 12))
