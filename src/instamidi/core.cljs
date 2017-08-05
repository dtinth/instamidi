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

(defn convert-midi [midi-file]
  (vec (.getMidiEvents midi-file)))

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
