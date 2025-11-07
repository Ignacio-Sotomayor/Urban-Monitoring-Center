package com.controller;

import com.view.IssuedTable;

public class RegistryNotifier {
    private IssuedTable issuedTable;
    private static RegistryNotifier instance = null;

    private RegistryNotifier() {

    }

    public void setIssuedTable(IssuedTable finesTable) {
        if(issuedTable == null)
            this.issuedTable = finesTable;
    }

    public static RegistryNotifier getNotifier(){
        if(instance == null)
            instance = new RegistryNotifier();
        return instance;
    }

}