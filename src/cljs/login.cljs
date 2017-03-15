(ns cljs.login
  (:require [domina.core :refer [append!
                                 by-class
                                 by-id
                                 destroy!
                                 prepend!
                                 value
                                 attr]]
            [domina.events :refer [listen! prevent-default]]
            [hiccups.runtime]
            [shoreleave.remotes.http-rpc :refer [remote-callback]])
  (:require-macros [hiccups.core :refer [html]]
                   [shoreleave.remotes.macros :as shore-macros]))

(def channel_opened (atom #{}))

(defn initial_procedure
  [channel_list]
  (if (empty? @channel_list)
      (do (swap! channel_list conj (js/WebSocket. "ws://localhost:9090"))
          (set! (.-onmessage (first @channel_list)) #(js/console.log %))
          )))

(defn send-message
    [msg evt]
    (do
        (prevent-default evt)
        (.send (first @channel_opened) msg)
        false))


(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [email (by-id "email")
          password (by-id "password")]
      (initial_procedure channel_opened)
      (listen! (by-id "submit") :click (fn [evt] (send-message (str "|" (value email) "----" (value password) "|") evt))))))
