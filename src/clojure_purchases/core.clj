(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(def file-name "purchases.csv")

(defn read-purchases []
    (let [purchases (str/split-lines (slurp file-name))
          purchases (map (fn [line]
                           (str/split line #","))
                      purchases)
          header (first purchases)
          purchases (rest purchases)
          purchases (map (fn [line]
                           (zipmap header line))
                      purchases)]
         purchases))

; customer_id,date,credit_card,cvv,category
(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (filter (fn [purchase]
                            (or (nil? category)
                                (= category (get purchase "category"))))
                    purchases)]
    [:ol
     (map (fn [purchase]
            [:li (str
                   (get purchase "customer_id")
                   " "
                   (get purchase "date")
                   " "
                   (get purchase "credit_card")
                   " "
                   (get purchase "cvv")
                   " "
                   (get purchase "category"))])
       purchases)]))
       
  
    
(c/defroutes app
  (c/GET "/" []
    (h/html [:html [:body (purchases-html nil)]]))
  (c/GET "/:category" [category]
    (h/html [:html
             [:body
              (purchases-html category)]]))


  (def server (atom nil)))

(defn dev []
  (let [s (deref server)]
    (if s(.stop s)))
  (reset! server (j/run-jetty app {:port 3000 :join? false})))

(defn -main [& args]
  (j/run-jetty app {:port 3000}))
    
              
       
        
  
                    

  

