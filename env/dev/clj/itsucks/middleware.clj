(ns itsucks.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-defaults api-defaults)
      wrap-exceptions
      wrap-reload))
