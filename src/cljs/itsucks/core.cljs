(ns itsucks.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [reagent.dom.server]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [itsucks.reactbs :as bs]
            [ajax.core :as a]
            [cljsjs.auth0-lock]
            [alandipert.storage-atom :refer [local-storage]]))

(defn GET [url handler]
  (a/GET url {:handler handler :keywords? true :response-format :json}))

;; -------------------------
;; Views

(def token (local-storage (r/atom {}) :token))
(def user-profile (local-storage (r/atom {}) :user-profile))

(defn navigation [authenticated]
  [bs/navbar {:inverse true}
   [bs/navbar-brand
    [:a {:href "/"} "It Sucks!"]]
   [bs/nav
    [bs/nav-item {:href "/"} "Home"]
    [bs/nav-item {:href "/about"} "About"]]
   [bs/nav {:pullRight true}
    (if authenticated
      [bs/nav-dropdown {:title (.-name @user-profile) :id "login-menu"}
       [bs/menu-item "Profile"]
       [bs/menu-item "Action"]
       [bs/menu-item {:divider true}]
       [bs/menu-item {:onSelect (fn [_]
                                  (.log js/console "Logout")
                                  (reset! token nil))} "Logout"]]
      [bs/nav-item {:onClick (fn [_]
                               (let [lock (js/Auth0Lock. "tHsRnRDmBDNHIxHWjiHovQiP4qwFa3Ix" "itsucks.eu.auth0.com")]
                                 (.log js/console "Show dialog")
                                 (.show lock (fn [err profile id_token]
                                               (reset! token id_token)
                                               (.log js/console (str profile))
                                               (reset! user-profile profile)))))} "Login"])]])

(defn sucking-list [things]
  (for [t things]
    [:div {:key (:id t)}
     [:h3 (:name t)]
     [:div.description (:description t)]]))

(defn list-projects [projects]
  (for [p projects]
    (let [name (:name p)]
      (.log js/console name)
      [:div {:key (:id p)}
       [:a {:href (str "/" (:slug p))} name]])))

(defn add-project-form [success-message]
  (let [new-project-name (r/atom nil)]
    (fn []
      [:div
       [bs/input {:type        "text"
                  :value       @new-project-name
                  :placeholder "New project name"
                  :on-change   #(reset! new-project-name (-> % .-target .-value))}]
       [bs/button {:on-click (fn [_]
                               (a/POST "/api/projects"
                                       {:format  :json
                                        :params  {:name @new-project-name}
                                        :handler #(reset! success-message "Project sucessfully added!")}))} "Add project"]])))

(defn home-page []
  (let [success-message (r/atom nil) projects (r/atom [])]
    (GET "/api/projects" #(reset! projects %))
    (fn []
      [:div
       (if (not (nil? @success-message))
         [bs/alert {:bsStyle "success" :dismissAfter 2000 :onDismiss #(reset! success-message nil)} @success-message])
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
  (let [current-project (r/atom [])]
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
   [navigation (not (nil? @token))]
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
  (r/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
