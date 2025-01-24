(ns moviefinder-app.profile.login-cta
  (:require
   [moviefinder-app.core.ui.icon :as icon]
   [moviefinder-app.core.ui.button :as button]
   [moviefinder-app.frontend.store :as store]))


(defn view [i]
  [:div.w-full.flex-1.flex.flex-col.items-center.justify-center.gap-4.p-6
   [icon/door-open {:class "size-20"}]
   [:p.font-bold.text-xl "Log in to access your profile"]
   [button/view
    {:button/label "Log in"
     :button/on-click #(store/put! i [:screen/clicked-link [:screen/login]])}]])
  