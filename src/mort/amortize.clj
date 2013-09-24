
(defn monthlyInterestRate [interestRate] (/ interestRate 12))
(defn rateToPercent [rate] (/ rate 100))

(defn oneMonthOfInterest [balance interestRate]
  (* balance (rateToPercent (monthlyInterestRate interestRate))))

(oneMonthOfInterest 180000 4.875)
