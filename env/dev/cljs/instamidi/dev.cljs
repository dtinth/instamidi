(ns ^:figwheel-no-load instamidi.dev
  (:require
    [instamidi.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
