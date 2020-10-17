import Felgo 3.0
import QtQuick 2.9
import QtQuick.Controls 1.4
import QtQuick.Layouts 1.3


// Client: {id, name, phone_num, city, (cday, cmonth, cyear)}
// Product: {id, name, }

App
{
    property ListModel customers_list: ListModel
    {
        ListElement{cid: 0; name: "Youssef Harmal"; phone_num: "0614894399"; city: "Casablanca"}
    }

    property StackViewDelegate default_stack_transition: StackViewDelegate {

        pushTransition: StackViewTransition {
          NumberAnimation {
            target: enterItem
            property: "opacity"
            from: 0
            to: 1
            duration: 200
          }
        }

        popTransition: StackViewTransition {
          NumberAnimation {
            target: exitItem
            property: "opacity"
            from: 1
            to: 0
            duration: 200
          }
        }
      }

    NotificationManager
    {
        id: notification
    }

    Navigation
    {
        NavigationItem // Orders
        {
            title: "Commandes"
            icon: IconType.bus
            NavigationStack
            {
                transitionDelegate: default_stack_transition
                Page
                {
                    title: "Commandes"
                }

            }
        }
        NavigationItem // Stock
        {
            title: "Stock"
            icon: IconType.book
            NavigationStack
            {
                transitionDelegate: default_stack_transition
                Page
                {
                    title: "Stock"
                }
            }
        }
        NavigationItem // Customers
        {
            title: "Clients"
            icon: IconType.users
            NavigationStack
            {
                id: customers_navigation_stack
                transitionDelegate: default_stack_transition
                Page {
                    title: "Clients"
                    AppListView
                    {
                        model: customers_list
                        delegate: SimpleRow
                        {
                            text: name
                            detailText: phone_num + ", " + city
                            onSelected: Math.round((new Date()).getTime() / 1000)
                        }

                        section.property: "name.charAt(0).toUpperCase()"
                        section.delegate: SimpleSection { }
                    }
                    FloatingActionButton
                    {
                        icon: IconType.plus
                        onClicked: customers_navigation_stack.push(add_customer_page)
                    }
                }
                Component
                {
                    id: add_customer_page
                    Page
                    {
                        title: "Ajouter un client"

                        FloatingActionButton
                        {
                            icon: IconType.save
                            onClicked:
                            {

                            }
                        }
                        GridLayout {
                            columns: 2
                            anchors.horizontalCenter: parent.horizontalCenter

                            AppText         { text: "Nom: " }
                            AppTextField    { anchors.verticalCenter: parent.verticalCenter; text: "Hello"; clearsOnBeginEditing: true }

                        }
                    }
                }
            }
        }
        NavigationItem // Products
        {
            title: "Produits"
            icon: IconType.shoppingcart
            NavigationStack
            {
                transitionDelegate: default_stack_transition
                Page
                {
                    title: "Produits"
                }
            }
        }
    }
}
