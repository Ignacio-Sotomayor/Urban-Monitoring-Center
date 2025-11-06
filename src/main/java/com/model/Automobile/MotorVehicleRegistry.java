package com.model.Automobile;
import com.DAO.*;
import com.model.Fines.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class MotorVehicleRegistry {
    private static final String name = "Mar del Plata Car Dealership";
    private final Map<Automobile,List<Fine>> automobilesInformation;
    private static MotorVehicleRegistry instance = null;

    private MotorVehicleRegistry(){
        this.automobilesInformation = new TreeMap<Automobile,List<Fine>>();
    }

    public static MotorVehicleRegistry getMotorVehicleRegistry(){
        if(instance == null)
            instance = new MotorVehicleRegistry();
        return instance;
    }

    public Automobile getRandomAutomobile() {
        AutomobileDAO automobileDao = new AutomobileDAO();
        try {
            Set<Automobile> autos = automobileDao.getAllAutomobiles();
            if(autos.isEmpty())
                return null;

            List<Automobile> lista = new ArrayList<>(autos);
            Random random = new Random();
            return lista.get(random.nextInt(lista.size()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBrand(Brand brand) {
        BrandsDAO brandDao = new BrandsDAO();
        ModelsDAO modelDao = new ModelsDAO();
        try{
            Integer brandID = brandDao.insertBrand(brand.getName());
            for (Iterator it = brand.getModels(); it.hasNext(); ) {
                Model m = (Model) it.next();
                modelDao.insertModel(m.getName(), brandID);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void addAutomobile(Automobile a) {
        automobilesInformation.put(a,new ArrayList<>());
        AutomobileDAO automobileDao = new AutomobileDAO();
        ModelsDAO modelDao = new ModelsDAO();
        OwnersDAO ownerDao = new OwnersDAO();
        try{
            Integer modelId = modelDao.getModelIdByName(a.getModel().getName());
            Integer ownerId = ownerDao.getOwnerIdByLegalID(a.getOwner().getLegalIid());
            automobileDao.insertAutomobile(a.getLicensePlate(),a.getYear(),modelId,ownerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAllAutomobiles() {
        for (Automobile a : automobilesInformation.keySet()) {
            System.out.println(a.toString());
        }
    }
    public void loadAutomobilesFromDB(){
        AutomobileDAO automobileDAO = new AutomobileDAO();
        FineDAO fineDao = new FineDAO();
        int id;
        try {
            Set<Automobile> automobiles = automobileDAO.getAllAutomobiles();
            for (Automobile a : automobiles) {
                id = automobileDAO.getAutomobileIdByLicensePlate(a.getLicensePlate());
                automobilesInformation.put(a,fineDao.getAllFinesFromAutomobile(id).stream().toList() );
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public int getScoring(Automobile a) {
        int scoring = 20;
        List<Fine> fines = automobilesInformation.get(a);
        for (Fine f : fines) {
            scoring-=f.getScoring();
        }
        return scoring;
    }



    public void informFines() {
        Iterator<Automobile> it = automobilesInformation.keySet().iterator();
        while(it.hasNext()) {
            Automobile actualAutomobile = it.next();
            System.out.format("THE %s has the next fines: \n",actualAutomobile.toString() );
            for(Fine f : automobilesInformation.get(actualAutomobile)){
                System.out.format("\t %s \n", f.toString());
            }
            System.out.println('\n');
        }
    }
}