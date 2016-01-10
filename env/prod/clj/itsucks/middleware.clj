(ns itsucks
    (:require
      [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
      [ring.middleware.json :as json] [ring.middleware.json :as json]))

(defn wrap-middleware [handler]
      (wrap-defaults handler api-defaults json/wrap-json-params json/wrap-json-response json/wrap-json-body {:keywords? true}))
