(ns mort.scenario
  (:require [mort.util :as util]))


(def savings-init {:balance 1000 :rate 0.75})
(def mortgage-init {:balance -200000 :rate 5})
(def investment-init {:balance 10000 :rate 6})
(def salary-init {:balance 4500})

(defn salary [salary-init] (:balance salary-init))
(defn investment-balance [investment-init]
  (+ (:balance investment-init)
     (* (util/one-month-of-interest (:rate investment-init) (:balance investment-init)))))

(investment-balance investment-init)


