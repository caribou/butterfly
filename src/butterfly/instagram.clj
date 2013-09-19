(ns butterfly.instagram
  (:require [instagram.oauth :as oauth]
            [instagram.callbacks :as callbacks]
            [instagram.callbacks.handlers :as handlers]
            [instagram.api.endpoint :as endpoint])
  (:import (instagram.callbacks.protocols SyncSingleCallback)))

(def oauth-map
  {:consumer-key "6bd3b25920394891bcd71b933ee948cc"
   :consumer-secret "2782e585026f4371bb8e47bcf5079926"
   :redirect-uri "http://weareinstrument.com"})

(def oauth-creds 
  (oauth/make-oauth-creds 
   (:consumer-key oauth-map)
   (:consumer-secret oauth-map)
   (:redirect-uri oauth-map)))

(defn get-tags
  [tag]
  (-> (endpoint/get-tagged-medias 
       :oauth oauth-creds
       :params {:tag_name tag})
      :body :data))

(defn start-streaming
  ([tag handler] (start-streaming tag handler 60000))
  ([tag handler sleeping]
     (loop [previous-id nil]
       (let [results (reverse (sort-by :created_time (get-tags tag)))
             most-recent-id (:id (first results))
             recent (if previous-id 
                      (take-while #(not= previous-id (:id %)) results)
                      results)]
         (doseq [result recent]
           (handler result))
         (Thread/sleep sleeping)
         (recur most-recent-id)))))
