(ns itsucks.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [itsucks.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [cheshire.core :as json]))

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    (json/generate-string data)})

(defn get-projects [n]
  (json-response (map (fn [n] {:name n}) ["php" "ziggo" "recruiters"])))

(defn get-complaints [project]
  (json-response [{:name "array_split" 
                   :description "seriously, who made this, someone with bipolar disease?" 
                   :likes 0}
                  {:name "magic_quotes"
                   :description "it's like gencide, but in computer sience. Hitler would like it"
                   :likes 5}]))

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
  (GET "/api/projects" [] get-projects)
  (GET "/api/projects/:name/complains" [name] (get-complaints name))
  (GET "/about" [] loading-page)
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
