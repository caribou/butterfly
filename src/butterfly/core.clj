(ns butterfly.core
  (:require [butterfly.twitter :as twitter]
            [butterfly.instagram :as instagram]))

(defn start-streaming
  [handlers]
  {:twitter 
   (when-let [twitter (:twitter handlers)]
     (future (twitter/start-streaming (:tag twitter) (:handler twitter) (:creds twitter))))
   :instagram
   (when-let [instagram (:instagram handlers)]
     (future (instagram/start-streaming (:tag instagram) (:handler instagram) (:creds instagram))))})

;; (start-streaming 
;;  {:twitter 
;;   {:tag "instrumentrulz" 
;;    :handler (fn [item] ...)}
;;   :instagram 
;;   {:tag "instrumentrulz"
;;    :handler (fn [])}})
