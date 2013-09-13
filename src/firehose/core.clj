(ns firehose.core
  (:require [firehose.twitter :as twitter]
            [firehose.instagram :as instagram]))

(defn start-streaming
  [handlers]
  (when-let [twitter (:twitter handlers)]
    (twitter/start-streaming (:tag twitter) (:handler twitter)))
  (when-let [instagram (:instagram handlers)]
    (instagram/start-streaming (:tag instagram) (:handler instagram))))

;; (start-streaming {:twitter {:tag "instrumentrulz" :handler (fn [item] ...)}})
