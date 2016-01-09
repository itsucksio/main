(ns itsucks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljsjs.react-bootstrap]
              [ajax.core :as ajax]))

;; -------------------------
;; Wrappers

(def nav-bar (reagent/adapt-react-class (aget js/ReactBootstrap "Navbar")))
(def nav (reagent/adapt-react-class (aget js/ReactBootstrap "Nav")))
(def nav-item (reagent/adapt-react-class (aget js/ReactBootstrap "NavItem")))
(def input (reagent/adapt-react-class (aget js/ReactBootstrap "Input")))
(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))

(defn GET [url handler] 
  (ajax/GET url {:handler handler :keywords? true :response-format :json}))

;; -------------------------
;; Views

(defn navigation []
  [nav-bar {:brand "It Sucks!" :inverse true}
   [nav
    [nav-item {:href "/"} "Home"]
    [nav-item {:href "/about"} "About"]]])

(def projects (reagent/atom []))

(defn sucking-list [things]
  (for [t things]
    [:div 
      [:h3 (:name t)]
      [:div.description.realz (:description t)]]))

(defn list-projects [projects]
  (for [p projects]
    (let [name (:name p)]
      (.log js/console name)
      [:div
        [:a {:href (str "/" name)} name]])))

(defn add-project-form []
  (let [new-project-name (reagent/atom nil)]
    (fn []
      [:div
      [input {:type "text" 
            :value @new-project-name 
            :placeholder "New project name"
            :on-change #(reset! new-project-name (-> % .-target .-value))}]
      [button { :on-click (fn [_] (js/alert @new-project-name)) } "Add project"]])))

(defn home-page []
  (GET "/api/projects" #(reset! projects %))
  (fn []
    [:div
     [:h1 "itsucks.io" [:b "/welcome"]]
     [:p "Welcome to it sucks, a place where we can give feedback in peace.
          Please add your rants or create a new itsucks-project."]
      [:div
        [:h3 "Top projects"]
        (list-projects @projects)]
      [:div
        [:h4 "Add new project"]
        [add-project-form]]
      [:p
        [:small "Please be nice to each other."]]]))

(def things-that-suck (reagent/atom []))

(defn project-page [name]
  (do
    (reset! things-that-suck [])
    (GET (reduce str ["/api/projects/" name "/complains"]) #(reset! things-that-suck %)))  
  (fn [_]
    [:div 
    [:h1 "itsucks.io" [:b "/" name]]
    (sucking-list @things-that-suck)
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
