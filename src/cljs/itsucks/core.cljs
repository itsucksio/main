(ns itsucks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [cljsjs.react-bootstrap]
              [ajax.core :as a]))

;; -------------------------
;; Wrappers

(def nav-bar (reagent/adapt-react-class (aget js/ReactBootstrap "Navbar")))
(def nav (reagent/adapt-react-class (aget js/ReactBootstrap "Nav")))
(def nav-item (reagent/adapt-react-class (aget js/ReactBootstrap "NavItem")))
(def input (reagent/adapt-react-class (aget js/ReactBootstrap "Input")))
(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))
(def alert (reagent/adapt-react-class (aget js/ReactBootstrap "Alert")))

(defn GET [url handler]
    (a/GET url {:handler handler :keywords? true :response-format :json}))

;; -------------------------
;; Views

(defn navigation []
  [nav-bar {:brand "It Sucks!" :inverse true}
   [nav
    [nav-item {:href "/"} "Home"]
    [nav-item {:href "/about"} "About"]]])

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
        [:a {:href (str "/" (:slug p))} name]])))

(defn add-project-form [success-message]
  (let [new-project-name (reagent/atom nil)]
    (fn []
      [:div
      [input {:type "text" 
            :value @new-project-name 
            :placeholder "New project name"
            :on-change #(reset! new-project-name (-> % .-target .-value))}]
      [button { :on-click (fn [_]
                            (a/POST "/api/projects"
                                    {:format :json
                                     :params {:name @new-project-name}
                                     :handler #(reset! success-message "Project sucessfully added!")}))} "Add project"]])))

(defn home-page []
  (let [success-message (reagent/atom nil) projects (reagent/atom [])]
    (GET "/api/projects" #(reset! projects %))
    (fn []
      [:div
       (if (not (nil? @success-message))
         [alert {:bsStyle "success" :dismissAfter 2000 :onDismiss #(reset! success-message nil)} @success-message])
       [:h1 "itsucks.io" [:b "/welcome"]]
       [:p "Welcome to it sucks, a place where we can give feedback in peace.
            Please add your rants or create a new itsucks-project."]
        [:div
          [:h3 "Top projects"]
          (list-projects @projects)]
        [:div
          [:h4 "Add new project"]
          [add-project-form success-message]]
        [:p
          [:small "Please be nice to each other."]]])))

(defn project-page [slug]
  (let [current-project (reagent/atom [])]
    (GET (str "/api/projects/" slug) #(reset! current-project %))
    (fn [_]
      [:div
       [:h1 "itsucks.io" [:b "/" slug]]
       [:p (:description @current-project)]
       (sucking-list (:complaints @current-project))
       [:div
        [:a {:href "/"} "go to home page"]]])))

(defn about-page []
  [:div
   [:h1 "itsucks.io" [:b "/about"]]
   [:p "Read all about us"]])

(defn current-page []
  [:div
   [navigation]
   [:div.container [(session/get :current-page)]]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page home-page))

(secretary/defroute "/about" []
                    (session/put! :current-page about-page))

(secretary/defroute "/:slug" [slug]
                    (session/put! :current-page (project-page slug)))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
