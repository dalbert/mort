(ns mort.conversion)

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

(defn annual-rate-to-monthly-rate
  "Divides by 12. Pass in the annual interest rate."
  [interest-rate]
  (/ interest-rate 12))

(defn annual-rate-to-monthly-percent
  "Divides by 1200. Pass in the annual interest rate, receive a decimal to multiple against your balance."
  [interest-rate]
  (/ interest-rate 1200))
