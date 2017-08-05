(ns instamidi.core
    (:require
      [reagent.core :as r]))

(defn intro []
  [:div.intro
    "Drag and drop MIDI file"])

(defn home-page []
  [:div
    [intro]])

(defn convert-midi [midi-file]
  (.getMidiEvents midi-file))

(->
  (.fetch js/window "demo.mid")
  (.then #(.arrayBuffer %))
  (.then #(js/MIDIFile. %))
  (.then #(convert-midi %))
  (.then #(.log js/console %)))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
