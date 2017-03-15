(ns chat-cljs-ws-coreasync.core
     (:gen-class)
     (:require [compojure.core :refer [GET defroutes]]
               [compojure.route :refer [not-found files resources]]
               [compojure.handler :refer [site]]
               [org.httpkit.server
                :refer [send! with-channel on-close on-receive run-server]]
               ))

(defonce channels (atom #{}))

(defn get_client
  "Self made function that cut the begin of the channel and return only the end point
  Used only to make code more readibily"
  [channel]
  (clojure.string/replace (str channel) #"/127.0.0.1:3000<->/" ""))

(defn connect!
  [channel request]
  (swap! channels conj channel)
  (println (str
                "Channel connected! There is a client on: "
                (get_client channel)
            )))

(defn handler [request]
  (with-channel request channel
    (connect! channel request)
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (fn [data] ;; echo it back
                          (do
                              (println "data received: " data "from client: " (get_client channel))
                              (doseq [chan (filter #(not= channel %) @channels)]
                                (send! chan data)))))))

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
