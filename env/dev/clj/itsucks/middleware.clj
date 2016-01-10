(ns itsucks.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :as json]))

(defn wrap-middleware [handler]
  (-> handler
      (json/wrap-json-body {:keywords? true})
      json/wrap-json-response
      (wrap-defaults api-defaults)
      wrap-exceptions
      wrap-reload
      json/wrap-json-params))
