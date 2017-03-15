(ns cljs.login
  (:require [domina.core :refer [append!
                                 by-class
                                 by-id
                                 destroy!
                                 prepend!
                                 value
                                 attr
                                 set-value!]]
            [domina.events :refer [listen! prevent-default]]
            [hiccups.runtime]
            [shoreleave.remotes.http-rpc :refer [remote-callback]])
  (:require-macros [hiccups.core :refer [html]]
                   [shoreleave.remotes.macros :as shore-macros]))

(def channel_opened (atom #{}))

(defn add_to_html
  [data]
  (do (append! (by-id "history") (str "<li id=print>" (.-data data) "</li>"))
      (set-value! (by-id "where_type_text") " ")))

(defn initial_procedure
  [channel_list]
  (if (empty? @channel_list)
      (do (swap! channel_list conj (js/WebSocket. "ws://localhost:3000/ws"))
          (set! (.-onmessage (first @channel_list)) #(add_to_html %))
          )))

(defn send-message
    [msg evt]
    (do
        (prevent-default evt)
        (.send (first @channel_opened) msg)
        (append! (by-id "history") (str "<li id=self_print>" msg "</li>"))
        false))


(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [text (by-id "where_type_text")]
      (initial_procedure channel_opened)
      (listen! (by-id "submit") :click (fn [evt] (send-message (str "|-----" (value text) "----|" ) evt))))))
