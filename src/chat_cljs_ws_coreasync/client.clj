(ns chat-cljs-ws-coreasync.client
  (:require [org.httpkit.client :as http]))

;
(let [options {:form-params {:name "http-kit" :features ["async" "client" "server"]}}
      {:keys [status error]} @(http/post "http://localhost:3034" options)]
  (if error
    (println "Failed, exception is " error)
    (println "Async HTTP POST: " status)))
 ; [1] may not always true, since DNS lookup maybe slow
