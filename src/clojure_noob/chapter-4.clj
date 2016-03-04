(ns clojure-noob.chapter-4)

;; Core Functions in Depth ;;

;; `first`, `rest`, `cons` are functions which define the _sequence abstraction_
;; _seq_ representation of a Map is a list of vectors `([:key "value"])`, and thats whay some functions deals with Map like they deal with Vectors

;;;;
;; Examples
;;;;

;; `map` ;;

;; simple usage
(map inc [1 2 3])

;; with multiple collection
(map str ["a" "b" "c"] ["A" "B" "C"])

; funny example of this (we just need to make sure our function supports the number of arguments similar to the number of colls
(def human-consumption [8.1 7.3 6.6 5.5])
(def critter-consumption [0.0 0.2 0.3 1.1])

(defn intake-total
  [human critter]
  {:human human
   :critter critter})

(map intake-total human-consumption critter-consumption)

;; with multiple functions
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))

(defn stats
  [numbers]
  (map (fn [arg] (arg numbers)) [count sum avg]))
  ;; shorthand #(% numbers)

(stats [1 2 3])

;; `reduce` ;;

; transform Map values
(reduce (fn [new-map [key value]]
          (assoc new-map key (inc value)))
        {}
        {:max 20 :min 10})

; map implementation in terms of `reduce` (yep, it's missing some `map`
; features)
(defn map-by-reduce
  [function collection]
  (reduce (fn [new-collection value]
            (conj new-collection (function value)))
          []
          collection))
(map-by-reduce inc [1 2 3])

;; `take` and `drop`

(take 2 [1 2 3 4])
(drop 2 [1 2 3 4])

;; `take-while` and `drop-while`

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)
(drop-while #(< (:month %) 3) food-journal)

; all together now
(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))

;; `filter` and `some`

(filter #(< (:human %) 5) food-journal)
(some #(< (:human %) 4) food-journal)

 ; a way to make some return the value itself
(some #(and (< (:human %) 4) %) food-journal)

;; `sort` and `sort-by` ;;

(sort [3 1 2])
(sort-by count (map str [4567 "a" "bdc"]))

