(defproject chat_cljs_ws_coreasync "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
		             [http-kit "2.2.0"]
                 [compojure "1.5.2"]
                 [javax.servlet/javax.servlet-api "3.1.0"]
                 [clj-time "0.13.0"]]
  :main ^:skip-aot chat-cljs-ws-coreasync.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
