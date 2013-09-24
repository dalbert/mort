(ns mort.core
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math])
  )

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

(one-year-of-compounded-accrual-with-no-payments 183000 4.875)

; example math stuff from the numeric-tower lib
(defn- sqr
  "Uses the numeric tower expt to square a number"
  [x]
  (math/expt x 2))

(defn euclidean-squared-distance
  "Computes the Euclidean squared distance between two sequences"
  [a b]
  (reduce + (map (comp sqr -) a b)))

(defn euclidean-distance
  "Computes the Euclidean distance between two sequences"
  [a b]
  (math/sqrt (euclidean-squared-distance a b)))

(let [a [1 2 3 5 8 13 21]
      b [0 2 4 6 8 10 12]]
  (euclidean-distance a b))


