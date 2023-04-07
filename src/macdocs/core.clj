(ns macdocs.core
  (:require [clojure.core.logic :as l])
  (:gen-class))

;; Sample documentation database
(def docs
  [{:title "Introduction" :content "Welcome to the documentation!"}
   {:title "Getting Started" :content "Learn how to set up your project and write your first program."}
   {:title "Advanced Topics" :content "Explore advanced features, including concurrency and macros."}])

(defn search-docs
  [search-str]
  (let [search-str-lower (clojure.string/lower-case search-str)]
    (l/run* [q]
            (l/fresh [doc title content]
                     (l/membero doc docs)
                     (l/== {:title title :content content} doc)
                     (l/conde
                      [(l/fresh [title-lower]
                                (l/== title-lower (clojure.string/lower-case title))
                                (l/seqo search-str-lower title-lower))]
                      [(l/fresh [content-lower]
                                (l/== content-lower (clojure.string/lower-case content))
                                (l/seqo search-str-lower content-lower))])
                     (l/== q doc)))))

(defn -main
  [& args]
  (println "Searching for 'start':")
  (doseq [result (search-docs "start")]
    (println result)))
