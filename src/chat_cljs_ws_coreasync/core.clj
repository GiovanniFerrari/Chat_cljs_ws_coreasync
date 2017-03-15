(ns chat-cljs-ws-coreasync.core
     (:gen-class)
     (:require [compojure.core :refer [GET defroutes]]
               [compojure.route :refer [not-found files resources]]
               [compojure.handler :refer [site]]
               [org.httpkit.server
                :refer [send! with-channel on-close on-receive run-server]]
               ))

(defonce channels (atom #{}))

(defn connect!
  [channel]
  (swap! channels conj channel))

(defn handler [request]
  (with-channel request channel
    (connect! channel)
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (fn [data] ;; echo it back
                          (do
                              (println "data received: " data)
                              (doseq [channel @channels]
                                (send! channel data)))))))

;(run-server http_handler {:port 3002})
;(run-server handler {:port 9092})

(defroutes http_handler
  (GET "/" [] "Hello from Compojure!")  ;; for testing only
  (GET "/ws" request (handler request)) ;; Web socket managing
  (files "/" {:root "target"})          ;; to serve static resources
  (resources "/" {:root "target"})      ;; to serve anything else
  (not-found "Page Not Found"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
