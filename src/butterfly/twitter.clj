(ns butterfly.twitter
  (:require [clojure.data.json :as json]
            [http.async.client :as ac]
            [twitter.oauth :as oauth]
            [twitter.callbacks :as callbacks]
            [twitter.callbacks.handlers :as handlers]
            [twitter.api.streaming :as streaming])
  (:import (twitter.callbacks.protocols AsyncStreamingCallback)))

(def oauth-map
  {:consumer-key "oSaFVmBhOq4bLAHECqNw"
   :consumer-secret "fIJbwzJFqbhDKFFpAM4CD4wkFH3SgmBjAAPKJMlWxDs"
   :access-token "15369416-5axMbMX2r2asNJlSFI6yAQIuM1QDsnLcdJn6cwMxY"
   :access-secret "vowJnDC9knSzpaRAaM2fC3fbNIZOJQAaUrDIWjFEWc"})

(def oauth-creds
  (oauth/make-oauth-creds
   (:consumer-key oauth-map)
   (:consumer-secret oauth-map)
   (:access-token oauth-map)
   (:access-secret oauth-map)))
  
(def printing-callback
  (AsyncStreamingCallback. 
   (comp println :text json/read-json #(str %2))
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn make-handler-callback
  [handler]
  (AsyncStreamingCallback. 
   handler
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn start-streaming
  [tag handler]
  (streaming/statuses-filter 
   :params {:track tag}
   :oauth-creds oauth-creds
   :callbacks (make-handler-callback handler)))
