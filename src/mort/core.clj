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

(defn new-balance-after-one-month
  [balance interest-rate total-payment]
  (- (+  balance (one-month-of-interest balance interest-rate)) total-payment))

(defn new-balance-after-one-year
  "Calculates the new loan balance after one year's payments are made."
  [balance interest-rate monthly-payment bonus-payments]
  (loop [iteration 0 balance balance]
    (if (and (< iteration 12) (> balance 0))
      (recur
       (inc iteration)
       (let [new-balance (- (+ balance (one-month-of-interest balance interest-rate)) monthly-payment (get bonus-payments iteration 0))]
         (if (< new-balance 0) 0 new-balance)))
      balance)))

(defn net-worth-after-one-year
  "Essentially the mortgage balance after a year of payments. Will be positive if the mortgage was fully paid during the year."
  [balance interest-rate monthly-payment bonus-payments]
  (- (new-balance-after-one-year balance interest-rate monthly-payment bonus-payments)))
