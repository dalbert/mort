(ns mort.util
  (:require [mort.conversion :as conversion]))

(defn one-month-of-interest
  "Calculates the amount of interest that would accrue in one month."
  [interest-rate balance]
  (if (> balance 0)
    (* balance (conversion/rate-to-percent (conversion/annual-rate-to-monthly-rate interest-rate)))
    0))
