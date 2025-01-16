(ns linkpage.frontend.screen
  (:require
   [linkpage.frontend.store :as store]
   [linkpage.frontend.route :as route]))

(def fallback [:screen/login])

(defn- get-route! []
  (or (route/get!) fallback))

(store/reg!
 :store/initialized
 (fn [i]
   (-> i
       (update-in [:store/state] assoc ::screen nil)
       (update-in [:store/effs] concat [[::get-route!]
                                        [::subscribe-route!]])))

 ::got-route
 (fn [i]
   (-> i
       (assoc-in [:store/state ::screen] (store/msg-payload i))))

 ::route-changed
 (fn [i]
   (-> i
       (assoc-in [:store/state ::screen] (store/msg-payload i))))

 :screen/clicked-link
 (fn [i]
   (let [screen-new (store/msg-payload i)]
     (-> i
         (assoc-in [:store/state ::screen] screen-new)
         (update-in [:store/effs] conj [::push! screen-new]))))

 :screen/push
 (fn [i]
   (let [screen-new (store/msg-payload i)]
     (-> i
         (assoc-in [:store/state ::screen] screen-new)
         (update-in [:store/effs] conj [::push! screen-new])))))


(store/reg-eff!
 ::get-route!
 (fn [i]
   (store/put! i [::got-route (get-route!)]))

 ::push!
 (fn [i]
   (let [route (store/eff-payload i)
         encoded (route/encode route)]
     (js/window.history.pushState nil nil encoded)))

 ::subscribe-route!
 (fn [i]
   (doseq [event ["popstate" "pushstate" "replacestate"]]
     (js/window.addEventListener event #(store/put! i [::route-changed (get-route!)])))))


(defn screen-payload [i]
  (-> i :store/state ::screen second))

(def view-screen-by-name! (atom {}))

(defn reg! [name view-screen]
  (println "reg! name" name)
  (swap! view-screen-by-name! assoc name view-screen))

(defn view [i]
  (let [screen (-> i :store/state ::screen (or fallback))
        screen-name (first screen)
        view-screen (@view-screen-by-name! screen-name)]
    (if view-screen
      [view-screen i]
      [:div "No screen found for " (pr-str screen-name)])))
