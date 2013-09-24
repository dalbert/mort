(ns mort.core
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn monthly-interest-rate
  "Divides by 12. Pass in the annual interest rate."
  [interest-rate]
  (/ interest-rate 12))

(defn rate-to-percent
  "Divides by 100. Turns a human readble rate into a mathable decimal."
  [rate]
  (/ rate 100))

(defn years-to-months
  "Multiples by 12."
  [years]
  (* years 12))

(defn one-month-of-interest
  "Calculates the amount of interest that would accrue in one month."
  [balance interest-rate]
  (* balance (rate-to-percent (monthly-interest-rate interest-rate))))

(defn one-year-of-compounded-accrual-with-no-payments
  "Iteratively calculates the total interest accrued in 12 compounding months."
  [starting-balance interest-rate]
  (loop [iteration 12 balance starting-balance accrual 0]
    (if (> iteration 0)
      (recur (dec iteration) (+ balance (one-month-of-interest balance interest-rate))(+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn one-year-of-accrual-with-interest-only-payments
  "Iteratively calculates the total interest accrued in 12 months with a static balance, as if only the iterest were being paid each month."
  [starting-balance interest-rate]
  (loop [iteration 12 balance starting-balance accrual 0]
    (if (> iteration 0)
      (recur (dec iteration) balance (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn principle-payment-left-after-interest
  "Subtracts the interest from the payment and returns the amount left that may be applied to principle"
  [payment interest]
  (- payment interest))

(defn one-year-of-accrual-with-required-payments
  "Iteratively calculates the total interest accrued in 12 months with principle+interest payments being made as required by the bank."
  [starting-balance interest-rate monthly-payment]
  (loop [iteration 12 balance starting-balance accrual 0 payment monthly-payment]
    (if (> iteration 0)
      (recur
       (dec iteration)
       (- balance (principle-payment-left-after-interest payment (one-month-of-interest balance interest-rate)))
       (+ accrual (one-month-of-interest balance interest-rate))
       payment)
      accrual
    )))

(defn years-of-accrual-with-required-payments
  "Iteratively calculates the total interest accrued in the given number of years with principle+interest payments being made as required by the bank."
  [years starting-balance interest-rate monthly-payment]
  (loop [iteration (years-to-months years) balance starting-balance accrual 0 payment monthly-payment]
    (if (> iteration 0)
      (recur
       (dec iteration)
       (- balance (principle-payment-left-after-interest payment (one-month-of-interest balance interest-rate)))
       (+ accrual (one-month-of-interest balance interest-rate))
       payment)
      accrual
    )))

