(ns firehose.instagram
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
  [tag handler]
  (let [results (endpoint/get-tagged-medias 
                 :oauth oauth-creds
                 :params {:tag_name tag})]
    (doseq [result (-> results :body :data)]
      (handler result))))

(defn start-streaming
  [tag handler]
  (loop []))
