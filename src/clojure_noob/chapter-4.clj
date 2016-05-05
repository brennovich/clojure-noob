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

;; Lazy seq ;;

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  "simulate database service"
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  "returns record if it's a vampire"
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  "tests database records againts vampire definition"
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

; Using non-lazy secs we'd be forced to wait for entire collection evaluation
; even if the vampire isn't the last record
;; (time (identify-vampire [0])) ; it takes ~1 sec
;; (time (identify-vampire [0 1])) ; it takes ~2 sec

; `range` returns a lazy seq, which means that actual values are realized by
; the time they are evaluated. Clojure is smart enough to realize that I'm
; using `first` over a result of a collection `filter` that a `map` is
; returning lazily, so instead of running all 10000 records it takes small
; chunks and evaluated them
;; (time (identify-vampire (range 1 10000))) ; it takes ~32 secs

;; Infinite seqs

(concat (take 8 (repeat "na")) ["Batman!"])
(take 3 (repeatedly #(rand-int 10)))

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(time (take 100 (even-numbers)))

(def result (+ 2 2 (* 3 3)))

