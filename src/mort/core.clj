(ns mort.core
  (:gen-class))


(defn -main  [& args]
  (println "Hello World"))


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

(defn sum-of-one-month-of-payments
  "Sums up all the payments made in a month."
  [required principle bonus]
  (+ required principle bonus))

(defn sum-of-one-year-of-payments
  "Sums up all the payments made in one year."
  [required principle bonus]
  (+ (* required 12) (* principle 12) (apply + (vals bonus))))

(defn principle-payment-left-after-interest
  "Subtracts the interest from the payment and returns the amount left that may
  be applied to principle"
  [payment interest]
  (- payment interest))

(defn calculate-tax-credit
  "Calculates the amount of money recouped by a mortgage interest tax credit given a year's interest payments."
  [interest-paid tax-credit]
  (if (> (* interest-paid (rate-to-percent (:rate tax-credit))) (:max tax-credit))
    (:max tax-credit)
    (* interest-paid (rate-to-percent (:rate tax-credit)))))

(defn new-balance-after-one-month
  [balance interest-rate total-payment]
  (- (+  balance (one-month-of-interest balance interest-rate)) total-payment))

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

(defn new-balance-after-one-year
  "Calculates the new loan balance after one year's payments are made."
  [{balance :starting-balance interest-rate :interest-rate required :required-payment principle :principle-payment bonus :bonus}]
  (loop [iteration 0 balance balance]
    (if (and (< iteration 12) (> balance 0))
      (recur
       (inc iteration)
       (let [new-balance (- (+ balance (one-month-of-interest balance interest-rate)) required principle (get bonus iteration 0))]
         (if (< new-balance 0) 0 new-balance)))
      balance)))

(defn principle-paid-in-one-year
  "Calculates the principle paid off after one year of payments."
  [balance interest-rate required principle bonus]
  (- balance (new-balance-after-one-year balance interest-rate required principle bonus)))

(defn interest-cost-in-one-year
  "Calculates the net expense of interest accrued during one year of payments."
  [{balance :starting-balance interest-rate :interest-rate required :required-payment principle :principle-payment bonus :bonus tax-credit :tax-credit}]
  (let [interest-paid (interest-paid-in-one-year balance interest-rate required principle bonus)
        credit (calculate-tax-credit (interest-paid-in-one-year balance interest-rate required principle bonus) tax-credit)]
    (- interest-paid credit)))
