(ns helloreagent.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [ajax.core :refer [GET POST]]))

;; -------------------------
;; Views

(def things-that-suck 
  [{:name "array_split" 
    :description "seriously, who made this, someone with bipolar disease?" 
    :likes 0}
   {:name "magic_quotes"
    :description "it's like gencide, but in computer sience. Hitler would like it"
    :likes 5}])

(def projects (reagent/atom []))

(defn sucking-list [things]
  (for [t things]
    [:div 
      [:h3 (:name t)]
      [:div.description (:description t)]]))

(defn list-projects [projects]
  (for [p projects]
    (let [name (second (first p))]
      (.log js/console name)
      [:div
        [:a {:href (str "/" name)} name]])))

(defn home-page []
  (let [get-stuff (fn [] (GET "/api/projects" {:handler 
                    (fn [response]
                      (reset! projects response))}))]
    (get-stuff)
    (fn []
      [:div 
      [:h1 "itsucks.io" [:b "/welcome"]]
      [:p "Welcome to it sucks, a place where we can give feedback in peace. 
          Please add your rants or create a new itsucks-project."]
      [:div
        [:h3 "Top projects"]
        (list-projects @projects)]
      [:p
        [:small "Please be nice to each other."]]])))
    

(defn about-page [name]
  (fn [_]
    [:div 
    [:h1 "itsucks.io" [:b "/" name]]
    (sucking-list things-that-suck)
    [:div 
     [:a {:href "/"} "go to home page"]]]))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page home-page))

(secretary/defroute "/:name" [name]
  (session/put! :current-page (about-page name)))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
