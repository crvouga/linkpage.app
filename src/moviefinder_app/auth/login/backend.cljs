(ns moviefinder-app.auth.login.backend
  (:require
   [moviefinder-app.rpc.backend :as rpc]
   [moviefinder-app.auth.login.verify-sms.impl]
   [moviefinder-app.auth.login.verify-sms.interface :as verify-sms]
   [clojure.core.async :refer [go <!]]))

(defmethod rpc/rpc! :login-rpc/send-code [ctx req]
  (go
    (let [payload (second req)
          input (merge ctx payload)
          res (<! (verify-sms/send-code! input))]
      res)))

(defmethod rpc/rpc! :login-rpc/verify-code [ctx req]
  (go
    (let [payload (second req)
          input (merge ctx payload)
          res (<! (verify-sms/verify-code! input))]
      res)))

#_(rpc/reg!
   :login-rpc/send-code
   (fn [ctx req]
     (go
       (let [payload (second req)
             input (merge ctx payload)
             res (<! (verify-sms/send-code! input))]
         res)))

   :login-rpc/verify-code
   (fn [ctx req]
     (go
       (let [payload (second req)
             input (merge ctx payload)
             res (<! (verify-sms/verify-code! input))]
         res))))