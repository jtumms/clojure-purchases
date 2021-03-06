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

(defn header []
  [:h2
               [:a {:href "/Alcohol"} "Alcohol"]
               " "
               [:a {:href "/Shoes"} "Shoes"]
               " "
               [:a {:href "/Furniture"} "Furniture"]
               " "
               [:a {:href "/Jewelry"} "Jewelry"]
               " "
               [:a {:href "/Food"} "Food"]])
            
            
  

; customer_id,date,credit_card,cvv,category
(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (filter (fn [purchase]
                            (or (nil? category)
                                (= category (get purchase "category"))))
                    purchases)]
    [:table
     [:tr
      [:th "ID"]
      [:th "Date"]
      [:th "Credit Card"]
      [:th "CVV"]
      [:th "Category"]]
     (map (fn [purchase]
            [:tr 
             [:td (get purchase "customer_id")]
             [:td (get purchase "date")]
             [:td (get purchase "credit_card")]
             [:td (get purchase "cvv")]
             [:td (get purchase "category")]])
       purchases)]))
       
  
    
(c/defroutes app
  (c/GET "/" []
    (h/html [:html
             [:body (header)
              (purchases-html nil)]]))
  (c/GET "/:category" [category]
    (h/html [:html
             [:body (header)
              (purchases-html category)]])))


(def server (atom nil))

(defn dev []
  (let [s (deref server)]
    (if s(.stop s)))
  (reset! server (j/run-jetty app {:port 3000 :join? false})))

(defn -main [& args]
  (j/run-jetty app {:port 3000}))
    
              
       
        
  
                    

  

