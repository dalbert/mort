(ns mort.scenario
  (:require [mort.util :as util])
  (:import [mort.util BankAccount]))

(def my-accounts [(util/BankAccount. -100000 5) ; mortgage
                  (util/BankAccount. 10000 0.25) ; checking
                  (util/BankAccount. 1000 2) ; savings
                  (util/BankAccount. 15000 6) ; investment, average return given as rate
                  ])
(def crap (util/accrue-all my-accounts))
(def shit (util/accrue-all crap))
(print "my-accounts")
(clojure.pprint/pprint my-accounts)
(print "crap")
(clojure.pprint/pprint crap)
(print "shit")
(clojure.pprint/pprint shit)


