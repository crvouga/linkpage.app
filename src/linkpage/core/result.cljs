(ns linkpage.core.result)


(defn ok? [result]
  (and (vector? result)
       (= :result/ok (first result))))
