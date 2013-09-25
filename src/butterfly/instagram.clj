(ns butterfly.instagram
  (:require [instagram.oauth :as oauth]
            [instagram.callbacks :as callbacks]
            [instagram.callbacks.handlers :as handlers]
            [instagram.api.endpoint :as endpoint])
  (:import (instagram.callbacks.protocols SyncSingleCallback)))

(defn make-oauth-creds 
  [oauth-map]
  (oauth/make-oauth-creds 
   (:consumer-key oauth-map)
   (:consumer-secret oauth-map)
   (:redirect-uri oauth-map)))

(defn get-tags
  [tag creds]
  (let [results (endpoint/get-tagged-medias 
                 :oauth creds
                 :params {:tag_name tag})]
    (get-in results [:body "data"])))

(defn start-streaming
  ([tag handler creds] (start-streaming tag handler creds 10000))
  ([tag handler creds sleeping]
     (let [oauth-map (make-oauth-creds creds)]
       (loop [previous-id nil]
         (let [results (reverse (sort-by #(get % "created_time") (get-tags tag oauth-map)))
               most-recent-id (get (first results) "id")
               recent (if previous-id 
                        (take-while #(not= previous-id (get % "id")) results)
                        results)]
           (doseq [result recent]
             (handler result))
           (Thread/sleep sleeping)
           (recur most-recent-id))))))
