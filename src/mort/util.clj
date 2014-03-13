(ns mort.util
  (:require [mort.conversion :as conversion]))

(defn one-month-of-interest
  "Calculates the amount of interest that would accrue in one month."
  [balance rate]
  (* balance (conversion/rate-to-percent (conversion/annual-rate-to-monthly-rate rate))))

(defn add-one-month-of-interest
  "Increases the given balance by one month's-worth of interest as calculated using the given rate."
  [balance rate]
  (+ balance (one-month-of-interest balance rate)))

(defprotocol InterestBearing
  "A protocol for accounts holding balances."
  (accrue [this] "Returns a new instance of the Account with an updated balance
              calculated using the current balance and the interest rate.")
  (debit-credit [this amount] "Adds the amount to the balance. Use negeative amounts for debits."))


(defrecord BankAccount [balance rate]
  InterestBearing
  (accrue [this]
              (update-in this [:balance] add-one-month-of-interest rate))
  (debit-credit [this amount]
                (update-in this [balance] + balance amount)))

(defn accrue-all [accounts] (map accrue accounts))

