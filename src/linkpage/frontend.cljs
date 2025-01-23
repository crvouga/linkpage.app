(ns linkpage.frontend
  (:require [reagent.dom.client :as rd]
            [linkpage.auth.frontend]
            [linkpage.auth.current-user.frontend :as current-user]
            [linkpage.rpc.frontend]
            [linkpage.frontend.toaster :as toaster]
            [linkpage.frontend.store :as store]
            [linkpage.frontend.screen :as screen]))

(defn view-root [i]
  [:div {:class "w-[100dvw] h-[100dvh] flex flex-col items-center justify-start"}
   (toaster/view i)
   (current-user/view-guard i screen/view)])

(defn view []
  (store/view view-root))

(defn -main []
  (store/initialize!)
  (let [dom-root (.getElementById js/document "root")
        react-root (rd/create-root dom-root)]
    (rd/render react-root [view])))
