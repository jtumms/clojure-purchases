(ns clojure-purchases.core
  (:require [clojure.string :as str])
  (:gen-class))

(def file-name "purchases.csv")

(defn -main [& args]
    (let [purchases (str/split-lines (slurp file-name))
          purchases (map (fn [line]
                           (str/split line #","))
                      purchases)
          header (first purchases)
          purchases (rest purchases)
          purchases (map (fn [line]
                           (zipmap header line))
                      purchases)
          purchases (filter (fn [line]
                              (= (get line "category")
                                 "Alcohol"))
                      purchases)
          purchases (pr-str purchases)]
      (spit "purchases.edn" purchases)))
    
              
       
        
  
                    

  

