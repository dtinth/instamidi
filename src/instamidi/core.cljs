(ns instamidi.core
    (:require
      [reagent.core :as r]))

(def app-state (r/atom {:loaded false
                        :notes []}))

(defn intro []
  [:div.intro
    "Drag and drop MIDI file"])

(defn home-page []
  [:div
    [:pre {:wrap true} (str @app-state)]])

(defn event->notepresses
  [events]
  (let [get-time #(.-playTime %)
        note-ons (filter #(= (.-subtype %) 9) (sort-by get-time events))
        note-offs (filter #(= (.-subtype %) 8) (sort-by get-time events))
        join-events (fn [on off]
                      {:velocity (.-param2 on)
                       :start (.-playTime on)
                       :end (.-playTime off)})]
    (map join-events note-ons note-offs)))

(defn map-vals
  [f map]
  (into {} (for [[k v] map] [k (f v)])))

(defn convert-midi
  [midi-file]
  (let [is-note-event #(or (= (.-subtype %) 8)
                           (= (.-subtype %) 9))
        note-value #(.-param1 %)]
    (->>
      (.getMidiEvents midi-file)
      (filter is-note-event)
      (group-by note-value)
      (map-vals event->notepresses)
      (vals)
      (flatten)
      (sort-by :start))))

(defn handle-loaded! [notes]
  (swap! app-state assoc :loaded true :notes notes))

(->
  (.fetch js/window "demo.mid")
  (.then #(.arrayBuffer %))
  (.then #(js/MIDIFile. %))
  (.then #(convert-midi %))
  (.then handle-loaded!))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
