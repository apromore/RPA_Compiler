package compiler.builder;

import org.w3c.dom.Element;

public class Activity extends UIPathElement {
    private Element activity;

    public Activity() {
        activity = doc.createElement("Activity");
        setActivityAttributes();

        doc.appendChild(activity);
    }

    private void setActivityAttributes() {
        activity.setAttribute("mc:Ignorable", "sap sap2010");
        activity.setAttribute("xmlns", "http://schemas.microsoft.com/netfx/2009/xaml/activities");
        activity.setAttribute("xmlns:mc", "http://schemas.openxmlformats.org/markup-compatibility/2006");
        activity.setAttribute("xmlns:mva", "clr-namespace:Microsoft.VisualBasic.Activities;assembly=System.Activities");
        activity.setAttribute("xmlns:sap", "http://schemas.microsoft.com/netfx/2009/xaml/activities/presentation");
        activity.setAttribute("xmlns:sap2010", "http://schemas.microsoft.com/netfx/2010/xaml/activities/presentation");
        activity.setAttribute("xmlns:scg", "clr-namespace:System.Collections.Generic;assembly=mscorlib");
        activity.setAttribute("xmlns:sco", "clr-namespace:System.Collections.ObjectModel;assembly=mscorlib");
        activity.setAttribute("xmlns:ui", "http://schemas.uipath.com/workflow/activities");
        activity.setAttribute("xmlns:x", "http://schemas.microsoft.com/winfx/2006/xaml");
        activity.setAttribute("xmlns:sd", "clr-namespace:System.Data;assembly=System.Data");
    }

    @Override
    public Element getDomElement() {
        return activity;
    }
}
