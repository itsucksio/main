(ns itsucks.database
  (:require [korma.db :as korma]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(def db-spec-string "jdbc:postgresql://itsucks:itsucks@192.168.99.100:5432/itsucks")

; Run migrations
(println "Running migrations if need be...")
(repl/migrate
  {:datastore  (jdbc/sql-database db-spec-string)
   :migrations (jdbc/load-resources "migrations")})

; Setup korma
(korma/defdb db db-spec-string)