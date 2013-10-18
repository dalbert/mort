(ns mort.time)

(defn months-vector
  "Returns a vector of 12 months, represented by numbers, in world order"
  []
  (vector 1 2 3 4 5 6 7 8 9 10 11 12))

(defn months-seq
  "A sequence of repeating month numbers, starting with the given month"
  [start-month]
  (cycle (drop (- start-month 1) (months-vector))))
