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
  (-> (endpoint/get-tagged-medias 
       :oauth creds
       :params {:tag_name tag})
      :body :data))

(defn start-streaming
  ([tag handler creds] (start-streaming tag handler creds 60000))
  ([tag handler creds sleeping]
     (let [oauth-map (make-oauth-creds creds)]
       (loop [previous-id nil]
         (let [results (reverse (sort-by :created_time (get-tags tag oauth-map)))
               most-recent-id (:id (first results))
               recent (if previous-id 
                        (take-while #(not= previous-id (:id %)) results)
                        results)]
           (doseq [result recent]
             (handler result))
           (Thread/sleep sleeping)
           (recur most-recent-id))))))
