(ns itsucks.database
  (:require korma.db))

(def db-connection-info (korma.db/postgres {:db "itsucks"
                   :user "itsucks"
                   :password "itsucks"
                   :host "192.168.99.100"}))

(korma.db/defdb db db-connection-info)