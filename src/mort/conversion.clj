(ns mort.conversion)

(defn annual-rate-to-monthly-rate
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