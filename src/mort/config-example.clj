(ns mort.config
  (:gen-class)
  (:require [mort.core :as core]))

(def interest-rate 5.5)
(def principle-balance 150000)
(def required-payment 1000)


(core/one-year-of-compounded-accrual-with-no-payments principle-balance interest-rate)
(core/one-month-of-interest principle-balance interest-rate)
(core/one-year-of-accrual-with-interest-only-payments principle-balance interest-rate)
(core/one-year-of-accrual-with-required-payments principle-balance interest-rate required-payment)
