(ns itsucks.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [itsucks.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [cheshire.core :as json]))

(def projects
  { :php [{:name "array_split" 
           :description "seriously, who made this, someone with bipolar disease?" 
           :likes 0}
          {:name "magic_quotes"
           :description "it's like gencide, but in computer sience. Hitler would like it"
           :likes 5}]
    :ziggo [{:name "internets" 
             :description "this thing sucks like a cat that didn't drink for a week" 
             :likes 3}
            {:name "they hire idiots"
             :description "it's anecdotal evidence, but still..."
             :likes 2}]
    :recruiters [{:name "they are stupid" 
                  :description "you think Donald Trump is stupid? Think again." 
                  :likes 3}]})

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body    (json/generate-string data)})

(defn get-projects [a]
  (json-response (map (fn [n] {:name n}) (keys projects))))

(defn get-complaints [project]
  (if (contains? projects (keyword project))
    (json-response (projects (keyword project)))
    (json-response {:error "not-found"} 404)))

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
