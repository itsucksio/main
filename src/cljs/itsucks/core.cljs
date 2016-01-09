(ns itsucks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljsjs.react-bootstrap]
              [ajax.core :refer [GET POST]]))

;; -------------------------
;; Wrappers

(def nav-bar (reagent/adapt-react-class (aget js/ReactBootstrap "Navbar")))
(def nav (reagent/adapt-react-class (aget js/ReactBootstrap "Nav")))
(def nav-item (reagent/adapt-react-class (aget js/ReactBootstrap "NavItem")))


;; -------------------------
;; Views

(defn navigation []
  [nav-bar {:brand "It Sucks!" :inverse true}
   [nav
    [nav-item {:href "/"} "Home"]
    [nav-item {:href "/about"} "About"]]])

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
    (let [name (:name p)]
      (.log js/console name)
      [:div
        [:a {:href (str "/" name)} name]])))

(defn home-page []
  (let [get-stuff (fn [] (GET "/api/projects" {:keywords? true :response-format :json :handler
                    (fn [response]
                      (.log js/console (str response))
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
    

(defn project-page [name]
  (fn [_]
    [:div 
    [:h1 "itsucks.io" [:b "/" name]]
    (sucking-list things-that-suck)
    [:div 
     [:a {:href "/"} "go to home page"]]]))

(defn about-page []
  [:div
   [:h1 "itsucks.io" [:b "/about"]]
   [:p "Read all about us"]])

(defn current-page []
  [:div
   [navigation]
   [:div {:class "container"} [(session/get :current-page)]]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page home-page))

(secretary/defroute "/about" []
                    (session/put! :current-page about-page))

(secretary/defroute "/:name" [name]
                    (session/put! :current-page (project-page name)))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
