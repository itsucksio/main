(ns itsucks.middleware
    (:require
      [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn wrap-middleware [handler]
      (wrap-defaults handler api-defaults))
