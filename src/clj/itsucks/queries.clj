(ns itsucks.queries
  (:require [itsucks.database]
            [korma.core :refer :all]))

;; Entities
(declare projects complaints)

(defentity projects
           (has-many complaints {:fk :project_id}))

(defentity complaints
           (belongs-to projects {:fk :project_id}))

;; Queries

(defn get-projects []
  (select projects))

(defn get-project [slug]
  (first (select projects
                 (where {:slug slug})
                 (with complaints))))

(defn get-complaints [id]
  (select complaints
          (where {:project_id id})))