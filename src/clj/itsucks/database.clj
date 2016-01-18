(ns itsucks.database
  (:require [korma.db :as korma]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [environ.core :refer [env]]))

(def db-spec-string (str
                      "jdbc:postgresql://"
                      (env :database-user)
                      ":"
                      (env :database-password)
                      "@"
                      (env :database-host)
                      ":5432/"
                      (env :database-name)))

; Run migrations
(println "Running migrations if need be...")
(repl/migrate
  {:datastore  (jdbc/sql-database db-spec-string)
   :migrations (jdbc/load-resources "migrations")})

; Setup korma
(korma/defdb db db-spec-string)