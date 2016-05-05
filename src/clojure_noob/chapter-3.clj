(ns clojure-noob.chapter-3)

;;;;
;; Snippets
;;;;

(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type & hits]
   (str "I " chop-type " chop " (reduce +  hits) " times " name "! Take that!"))
  ([name]
   (x-chop name "karate")))

(defn movie-category
  [{:keys [category name]}]
  (str name " is a " category " movie!"))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  "Matches right side of a hobbit left part"
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts-my-version
  "Here is a _smarter_ way that I found to do it...
  Add missing right sides to a given seq of maps with name and size"
  [asym-hobbit-body-parts]
  (distinct (into asym-hobbit-body-parts
                   (map matching-part asym-hobbit-body-parts))))


(defn symmetrize-body-parts-2
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  ;; Here `loop` starts out binding `asym-body-parts` to
  ;; `remaining-asym-parts` and `[]` to `final-body-parts`
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    ;; We check if there any remaining maps from starting seq
    (if (empty? remaining-asym-parts)
      ;; if empty it means that we already walked through all of them
      final-body-parts
      ;; if not we'll use `let` to bind head of `remaining-asym-parts`
      ;; to `part` and the rest to `remaining`
      (let [[part & remaining] remaining-asym-parts]
        ;; then we restart the loop passing `remaining` (which is
        ;; `remaining-asym-parts` without head) and merged `part` and
        ;; their `matching-part` into `final-body-parts`
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn symmetrize-body-parts-reduce-version
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn symmetrize-body-parts-reduce-version
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  [asym-hobbit-body-parts]
  (let [sym-parts (symmetrize-body-parts-reduce-version asym-hobbit-body-parts)
        body-parts-size-sum (reduce + (map :size sym-parts))
        target (rand body-parts-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(hit asym-hobbit-body-parts)

;; Own implmentation of `reduce` :-P
(defn my-reduce
  ([function initial collection]
   (loop [result initial remaining collection]
     (if (empty? remaining)
       result
       (recur (function result (first remaining)) (rest remaining)))))
  ([function [head & tail]]
   (my-reduce function head tail)))

;;;;
;; Exercises
;;;;

;; 1. Use the str, vector, list, hash-map, and hash-set functions.

(str "My name is Brenno Costa and it's "
     (re-find #"\d{2}:\d{2}" (str (java.util.Date.))))

;; 2. Write a function that takes a number and adds 100 to it.

(defn make-it-plus-100
  [number]
  (+ number 100))
(make-it-plus-100 1)

;; 3. Write a function, dec-maker, that works exactly like the
;; function inc-maker except with subtraction: 

(defn dec-maker
  [value]
  (fn [number] (- number value)))

(def dec9 (dec-maker 9))
(dec9 10)

;; 4. Write a function, mapset, that works like map except the
;; return value is a set:

(defn mapset
  [function collection]
  (set (map function collection)))
(mapset inc [1 1 2 2])

;; 5. Create a function that’s similar to symmetrize-body-parts except that it
;; has to work with weird space aliens with radial symmetry. Instead of two
;; eyes, arms, legs, and so on, they have five.
;; 6. Create a function that generalizes symmetrize-body-parts and the function
;; you created in Exercise 5. The new function should take a collection of body
;; parts and the number of matching body parts to add.

(def asymmetric-alien-parts [{:name "head" :size 3}
                             {:name "hand" :size 1}
                             {:name "neck" :size 1}
                             {:name "arm" :size 3}])

(defn match-missing-parts
  [part symmetry-factor]
  (loop [symmetric-parts [{:name (str (:name part) "-" 1)
                           :size (:size part)}]
         factor 1]
    (if (= factor symmetry-factor)
      symmetric-parts
      (recur (conj symmetric-parts {:name (str (:name part) "-" (inc factor)) :size (:size part)}) (inc factor)))))

(defn symmetrize-body-parts
  [asym-body-parts symmetry-factor]
  (reduce (fn [final-body-parts part]
            (conj final-body-parts (match-missing-parts part symmetry-factor)))
          []
          asym-body-parts))

(symmetrize-body-parts asymmetric-alien-parts 5)

(defn my-loop
  []
  (loop [acc 1]
    (if (= acc 5)
      (str "Yayyy")
      (recur (inc acc)))))

(my-loop)
(conj [2] 3)

(map (fn [[k v]] ({k (* v 2)})) [{:a 1 :b 2}])
