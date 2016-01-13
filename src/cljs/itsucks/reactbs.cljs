(ns itsucks.reactbs
  (:require [reagent.core :as reagent]
            [cljsjs.react-bootstrap]))

(def navbar (reagent/adapt-react-class (aget js/ReactBootstrap "Navbar")))
(def navbar-brand (reagent/adapt-react-class (aget js/ReactBootstrap "NavbarBrand")))
(def nav (reagent/adapt-react-class (aget js/ReactBootstrap "Nav")))
(def nav-item (reagent/adapt-react-class (aget js/ReactBootstrap "NavItem")))
(def input (reagent/adapt-react-class (aget js/ReactBootstrap "Input")))
(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))
(def alert (reagent/adapt-react-class (aget js/ReactBootstrap "Alert")))
(def nav-dropdown (reagent/adapt-react-class (aget js/ReactBootstrap "NavDropdown")))
(def menu-item (reagent/adapt-react-class (aget js/ReactBootstrap "MenuItem")))
