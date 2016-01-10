(ns itsucks.handler
  (:require [compojure.core :refer [GET defroutes]]
            [ring.util.response :refer [response]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [itsucks.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [cheshire.core :as j]
            [itsucks.queries :as q]))

(defn get-projects []
  (q/get-projects))

(defn get-project [slug]
  (q/get-project slug))

(defn get-complaints [id]
  (q/get-complaints (read-string id)))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def loading-page
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "bower_components/bootstrap/dist/css/bootstrap.min.css")
     (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
    [:body
     mount-target
     (include-js "js/app.js")]]))


(defroutes routes
  (GET "/" [] loading-page)
  (GET "/api/projects" [] (response (get-projects)))
  (GET "/api/projects/:slug" [slug] (response (get-project slug)))
  (GET "/api/projects/:id/complaints" [id] (response (get-complaints id)))
  (GET "*" [] loading-page)
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
