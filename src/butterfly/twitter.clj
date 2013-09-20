(ns butterfly.twitter
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [http.async.client :as ac]
            [twitter.oauth :as oauth]
            [twitter.callbacks :as callbacks]
            [twitter.callbacks.handlers :as handlers]
            [twitter.api.streaming :as streaming])
  (:import (twitter.callbacks.protocols AsyncStreamingCallback)))

(defn make-oauth-creds
  [oauth-map]
  (oauth/make-oauth-creds
   (:consumer-key oauth-map)
   (:consumer-secret oauth-map)
   (:access-token oauth-map)
   (:access-secret oauth-map)))

(def printing-callback
  (AsyncStreamingCallback. 
   (comp println :text json/read #(str %2))
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn handle-tweet
  [handler]
  (fn [_ raw]
    (try 
      (when-let [tweet (json/read-str (str raw))]
        (handler tweet))
      (catch Exception e nil))))

(defn make-handler-callback
  [handler]
  (AsyncStreamingCallback. 
   (handle-tweet handler)
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn start-streaming
  [tag handler creds]
  (streaming/statuses-filter 
   :params {:track tag}
   :oauth-creds (make-oauth-creds creds)
   :callbacks (make-handler-callback handler)))
