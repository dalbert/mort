(ns mort.core
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; below: reusable, component functions

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

(defn months-to-years
  "Divides by 12"
  [months]
  (/ months 12))

(defn one-month-of-interest
  "Calculates the amount of interest that would accrue in one month."
  [balance interest-rate]
  (if (> balance 0)
    (* balance (rate-to-percent (monthly-interest-rate interest-rate)))
    0))

(defn new-balance-after-one-month-payment
  "Calculates the new loan balance after one month's payment is made."
  [balance interest-rate required principle bonus]
  (let [new-balance
         (- balance
            (principle-payment-left-after-interest required (one-month-of-interest balance interest-rate))
            principle
            bonus)]
     (if (< new-balance 0) 0 new-balance)))

(defn new-balance-after-one-year-of-payments
  "Calculates the new loan balance after 12 monthly payments are made."
  [starting-balance interest-rate required principle bonus]
  (loop [iteration 0 balance starting-balance accrual 0]
    (if (and (< iteration 12) (> balance 0))
      (recur
       (inc iteration)
       (- balance (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate)))
       (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

;; below: one-off functions

(defn one-year-of-compounded-accrual-with-no-payments
  "Iteratively calculates the total interest accrued in 12 compounding months."
  [starting-balance interest-rate]
  (loop [iteration 12 balance starting-balance accrual 0]
    (if (> iteration 0)
      (recur
       (dec iteration)
       (+ balance (one-month-of-interest balance interest-rate))
       (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn one-year-of-accrual-with-interest-only-payments
  "Iteratively calculates the total interest accrued in 12 months with a static
  balance, as if only the iterest were being paid each month."
  [starting-balance interest-rate]
  (loop [iteration 12 balance starting-balance accrual 0]
    (if (> iteration 0)
      (recur (dec iteration) balance (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn principle-payment-left-after-interest
  "Subtracts the interest from the payment and returns the amount left that may
  be applied to principle"
  [payment interest]
  (- payment interest))

(defn one-year-of-accrual-with-required-payments
  "Iteratively calculates the total interest accrued in 12 months with principle+interest
  payments being made as required by the bank."
  [starting-balance interest-rate monthly-payment]
  (loop [iteration 12 balance starting-balance accrual 0]
    (if (> iteration 0)
      (recur
       (dec iteration)
       (- balance (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate)))
       (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn years-of-accrual-with-required-payments
  "Iteratively calculates the total interest accrued in the given number of years
  with principle+interest payments being made as required by the bank."
  [years starting-balance interest-rate monthly-payment]
  (loop [iteration 0 balance starting-balance accrual 0]
    (if (< iteration (years-to-months years))
      (recur
       (inc iteration)
       (- balance (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate)))
       (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn years-of-accrual-with-scheduled-principle-payments
  "Iteratively calculates the total interest accrued in the given number of years
  with additional principle payments made each month."
  [years starting-balance interest-rate monthly-payment monthly-principle-payment]
  (loop [iteration 0 balance starting-balance accrual 0]
    (if (< iteration (years-to-months years))
      (recur
       (inc iteration)
       (- balance (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate)) monthly-principle-payment)
       (+ accrual (one-month-of-interest balance interest-rate)))
      accrual
    )))

(defn years-of-accrual-with-scheduled-principle-payments-and-bonus-payments
  "Iteratively calculates the total interest accrued in the given number of years
  with additional principle payments made each month and also a map of one-time
  payments made in specific months during the course of the repayment period."
  [years starting-balance interest-rate monthly-payment monthly-principle-payment bonus-payments-map]
  (loop [iteration 0 balance starting-balance accrual 0]
    (if (and (< iteration (years-to-months years)) (> balance 0))
      (recur
       (inc iteration)
       (let [new-balance
             (- balance
              (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate))
              monthly-principle-payment
              (get bonus-payments-map iteration 0))]
         (if (< new-balance 0) 0 new-balance))
       (+ accrual (one-month-of-interest balance interest-rate)))
      {:ending-balance balance :interest-paid accrual :years (float (months-to-years iteration))}
    )))

(defn years-of-accrual-with-scheduled-principle-payments-and-bonus-payments
  "Iteratively calculates the total interest accrued in the given number of years
  with additional principle payments made each month and also a map of one-time
  payments made in specific months during the course of the repayment period. Then
  it also accounts for special tax breaks on mortgage interest."
  [years starting-balance interest-rate monthly-payment monthly-principle-payment bonus-payments-map tax-refund-settings]
  (loop [iteration 0 balance starting-balance accrual 0]
    ;; TODO: make this use funcs that do a year's-worth at a time. Will need a new func that calcs new balance after a year of payments
    (if (and (< iteration years) (> balance 0))
      (if (= (mod iteration 12) 0))
      (recur
       (inc iteration)
       (let [new-balance
             (- balance
              (principle-payment-left-after-interest monthly-payment (one-month-of-interest balance interest-rate))
              monthly-principle-payment
              (get bonus-payments-map iteration 0))]
         (if (< new-balance 0) 0 new-balance))
       (+ accrual (one-month-of-interest balance interest-rate)))
      {:ending-balance balance :interest-paid accrual :years (float (months-to-years iteration))}
    )))
